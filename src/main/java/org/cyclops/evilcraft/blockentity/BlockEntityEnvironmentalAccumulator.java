package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.block.BlockEnvironmentalAccumulator;
import org.cyclops.evilcraft.block.BlockEnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.client.particle.ParticleBlurTargettedData;
import org.cyclops.evilcraft.client.particle.ParticleBubbleExtendedData;
import org.cyclops.evilcraft.core.blockentity.BlockEntityBeacon;
import org.cyclops.evilcraft.core.degradation.DegradationExecutor;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Machine that can accumulate the weather and put it in a bottle.
 * @author immortaleeb
 *
 */
public class BlockEntityEnvironmentalAccumulator extends BlockEntityBeacon implements IDegradable {

    public static final int MAX_AGE = 50;
    public static final int SPREAD = 25;

    private static final int ITEM_MOVE_COOLDOWN_DURATION = 1;

    private static final double WEATHER_CONTAINER_MIN_DROP_HEIGHT = 0.0;
    private static final double WEATHER_CONTAINER_MAX_DROP_HEIGHT = 2.0;
    private static final double WEATHER_CONTAINER_SPAWN_HEIGHT =
            BlockEnvironmentalAccumulatorConfig.defaultProcessItemTickCount *
            BlockEnvironmentalAccumulatorConfig.defaultProcessItemSpeed + 1;

    private static final float ITEM_MIN_SPAWN_HEIGHT = 1.0f;

    private static final int DEGRADATION_RADIUS_BASE = 5;
    private static final int DEGRADATION_TICK_INTERVAL = 100;

    private DegradationExecutor degradationExecutor;
    // This number rises with the number of uses of the env. accum.
    private int degradation = 0;
    private BlockPos location = null;
    private static final BlockPos[] waterOffsets = new BlockPos[]{
            new BlockPos(-2, -1, -2),
            new BlockPos(-2, -1,  2),
            new BlockPos( 2, -1, -2),
            new BlockPos( 2, -1,  2),
    };
    private ServerBossEvent bossInfo = null;

    /**
     * Holds the state of the environmental accumulator.
     * The following states are possible: idle (the default case), cooling down,
     * processing an item and dropping an item. The different states can be found as
     * public static variables of {@link BlockEnvironmentalAccumulator}.
     */
    private int state = 0;
    private int tick = 0;

    private Inventory inventory;

    // The recipe we're currently working on
    @Nullable
    private RecipeEnvironmentalAccumulator recipe;
    private String recipeId;

    /**
     * Make a new instance.
     */
    public BlockEntityEnvironmentalAccumulator(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_ENVIRONMENTAL_ACCUMULATOR, blockPos, blockState);
        recreateBossInfo();

        degradationExecutor = new DegradationExecutor(this);

        inventory = new Inventory(this);
        addCapabilityInternal(ForgeCapabilities.ITEM_HANDLER, LazyOptional.of(inventory::getItemHandler));
        inventory.addDirtyMarkListener(this);

        if (MinecraftHelpers.isClientSide()) {
            setBeamColor(getOuterColorByState(state));
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    protected void recreateBossInfo() {
        this.bossInfo = (ServerBossEvent)(new ServerBossEvent(
                Component.translatable("chat.evilcraft.boss_display.charge"),
                BossEvent.BossBarColor.PURPLE,
                BossEvent.BossBarOverlay.PROGRESS))
                .setDarkenScreen(false);
    }

    protected Triple<Float, Float, Float> getBaseBeamColor() {
        if (getLevel() == null) {
            return Triple.of(0F, 0F, 0F);
        }
        Biome biome = getLevel().getBiome(getBlockPos()).value();
        return Helpers.intToRGB(biome.getGrassColor(getBlockPos().getX(), getBlockPos().getZ()));
    }

    @OnlyIn(Dist.CLIENT)
    private Vector4f getOuterColorByState(int state) {
        Triple<Float, Float, Float> baseColor = getBaseBeamColor();
        float coolFactor = (getMaxCooldownTick() - tick) / (float) getMaxCooldownTick();
        if (state == BlockEnvironmentalAccumulator.STATE_PROCESSING_ITEM)
            return new Vector4f(baseColor.getLeft(), baseColor.getMiddle(), baseColor.getRight(), 0.05f);
        if (state == BlockEnvironmentalAccumulator.STATE_IDLE)
            return new Vector4f(baseColor.getLeft(), baseColor.getMiddle(), baseColor.getRight(), 0.13f);
        else
            return new Vector4f(baseColor.getLeft() * coolFactor, baseColor.getMiddle() * coolFactor, baseColor.getRight() * coolFactor, 0.13f);
    }

    /**
     * Get the maximum cooldown tick for accumulating weather.
     * @return The maximum cooldown tick.
     */
    public int getMaxCooldownTick() {
        return (recipe == null) ? BlockEnvironmentalAccumulatorConfig.defaultTickCooldown : recipe.getCooldownTime();
    }

    /**
     * Get the Y coordinate of the current moving item.
     * @return The Y coordinate of the inner item.
     */
    @OnlyIn(Dist.CLIENT)
    public float getMovingItemY() {
        if (state == BlockEnvironmentalAccumulator.STATE_PROCESSING_ITEM)
            return ITEM_MIN_SPAWN_HEIGHT + (getItemMoveDuration() - tick) * getItemMoveSpeed();
        else
            return -1;
    }

    /**
     * Get the current recipe we're working on.
     * @return Returns the recipe being processed, or null in case we're
     *         not processing anything at the moment.
     */
    public RecipeEnvironmentalAccumulator getRecipe() {
        return recipe;
    }

    private int getItemMoveDuration() {
        if (recipe == null)
            return BlockEnvironmentalAccumulatorConfig.defaultProcessItemTickCount;
        else
            return recipe.getDuration();
    }

    private float getItemMoveSpeed() {
        if (recipe == null)
            return (float) BlockEnvironmentalAccumulatorConfig.defaultProcessItemSpeed;
        else
            return (float) recipe.getProcessingSpeed();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        getBossInfo().removeAllPlayers();
    }

    @OnlyIn(Dist.CLIENT)
    protected void showWaterBeams() {
        RandomSource random = level.random;
        BlockPos target = getBlockPos();
        for (int j = 0; j < waterOffsets.length; j++) {
            BlockPos offset = waterOffsets[j];
            BlockPos location = target.offset(offset);
            double x = location.getX() + 0.5;
            double y = location.getY() + 0.5;
            double z = location.getZ() + 0.5;

            float rotationYaw = (float) LocationHelpers.getYaw(location, target);
            float rotationPitch = (float) LocationHelpers.getPitch(location, target);

            for (int i = 0; i < random.nextInt(2); i++) {
                double particleX = x - 0.2 + random.nextDouble() * 0.4;
                double particleY = y - 0.2 + random.nextDouble() * 0.4;
                double particleZ = z - 0.2 + random.nextDouble() * 0.4;

                double speed = 2;

                double particleMotionX = Mth.sin(rotationPitch / 180.0F * (float) Math.PI) * Mth.cos(rotationYaw / 180.0F * (float) Math.PI) * speed;
                double particleMotionY = Mth.cos(rotationPitch / 180.0F * (float) Math.PI) * speed * 5;
                double particleMotionZ = Mth.sin(rotationPitch / 180.0F * (float) Math.PI) * Mth.sin(rotationYaw / 180.0F * (float)Math.PI) * speed;

                Minecraft.getInstance().levelRenderer.addParticle(
                        new ParticleBubbleExtendedData(0.02F), false,
                        particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void showAccumulatingParticles() {
        showAccumulatingParticles(level, getBlockPos().getX() + 0.5F, getBlockPos().getY() + 0.5F, getBlockPos().getZ() + 0.5F, SPREAD);
    }

    @OnlyIn(Dist.CLIENT)
    public static void showAccumulatingParticles(Level world, float centerX, float centerY, float centerZ, float spread) {
        RandomSource rand = world.random;
        for (int j = 0; j < rand.nextInt(20); j++) {
            float scale = 0.6F - rand.nextFloat() * 0.4F;
            float red = rand.nextFloat() * 0.1F + 0.2F;
            float green = rand.nextFloat() * 0.1F + 0.3F;
            float blue = rand.nextFloat() * 0.1F + 0.2F;
            float ageMultiplier = MAX_AGE + 10;

            float motionX = spread - rand.nextFloat() * 2 * spread;
            float motionY = spread - rand.nextFloat() * 2 * spread;
            float motionZ = spread - rand.nextFloat() * 2 * spread;

            Minecraft.getInstance().levelRenderer.addParticle(
                    new ParticleBlurTargettedData(red, green, blue, scale, ageMultiplier, centerX, centerY, centerZ), false,
                    centerX, centerY, centerZ, motionX, motionY, motionZ);
        }
    }

    protected RecipeType<RecipeEnvironmentalAccumulator> getRegistry() {
        return RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR;
    }

    private void updateEnvironmentalAccumulatorIdle() {
        // Look for items thrown into the beam
        List<ItemEntity> entityItems = level.getEntitiesOfClass(ItemEntity.class,
                new AABB(
                        getBlockPos().getX(), getBlockPos().getY() + WEATHER_CONTAINER_MIN_DROP_HEIGHT, getBlockPos().getZ(),
                        getBlockPos().getX() + 1.0, getBlockPos().getY() + WEATHER_CONTAINER_MAX_DROP_HEIGHT, getBlockPos().getZ() + 1.0)
                );

        // Loop over all recipes until we find an item dropped in the accumulator that matches a recipe
        for (RecipeEnvironmentalAccumulator recipe : CraftingHelpers.findRecipes(level, getRegistry())) {
            Ingredient recipeIngredient = recipe.getInputIngredient();
            WeatherType weatherType = recipe.getInputWeather();

            // Loop over all dropped items
            for (Object obj : entityItems) {
                ItemEntity entityItem = (ItemEntity) obj;
                ItemStack stack = entityItem.getItem();

                if (recipeIngredient.test(stack) && (weatherType == null || weatherType.isActive(level))) {

                    // Save the required input items in the inventory
                    this.getInventory().setItem(0, stack.copy());

                    // Save the recipe
                    this.recipe = recipe;

                    if (!level.isClientSide()) {
                        decreaseStackSize(entityItem, 1);
                    }

                    activateProcessingItemState();

                    return;
                }

            }
        }
    }

    private void decreaseStackSize(ItemEntity entityItem, int count) {
        entityItem.getItem().shrink(count);

        if (entityItem.getItem().getCount() == 0)
            entityItem.remove(Entity.RemovalReason.DISCARDED);
    }

    private void dropItemStack() {
        if (!level.isClientSide()) {
            // EntityItem that will contain the dropped itemstack
            ItemEntity entity = new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY() + WEATHER_CONTAINER_SPAWN_HEIGHT, getBlockPos().getZ(), ItemStack.EMPTY);

            if (recipe == null) {
                // No recipe found, throw the item stack in the inventory back
                // (NOTE: this can be caused because of weather changes)
                entity.setItem(this.getInventory().getItem(0));
            } else {
                // Recipe found, throw back the result
                entity.setItem(recipe.assemble(getInventory(), level.registryAccess()));

                // Change the weather to the resulting weather
                WeatherType weatherSource = recipe.getInputWeather();
                if (weatherSource != null)
                    weatherSource.deactivate((ServerLevel) level);

                WeatherType weatherResult = recipe.getOutputWeather();
                if (weatherResult != null)
                    weatherResult.activate((ServerLevel) level);
            }

            // Drop the items on the ground
            level.addFreshEntity(entity);
        }
    }

    private void activateIdleState() {
        tick = 0;
        state = BlockEnvironmentalAccumulator.STATE_IDLE;

        if (!level.isClientSide()) {
            sendUpdate();
            setChanged();
        }
    }

    private void activateProcessingItemState() {
        // Set the duration for processing the item
        if (recipe == null)
            tick = BlockEnvironmentalAccumulatorConfig.defaultProcessItemTickCount;
        else
            tick = recipe.getDuration();

        state = BlockEnvironmentalAccumulator.STATE_PROCESSING_ITEM;

        if (!level.isClientSide()) {
            sendUpdate();
            setChanged();
        }
    }

    private void activateFinishedProcessingItemState() {
        tick = ITEM_MOVE_COOLDOWN_DURATION;
        state = BlockEnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM;

        if (!level.isClientSide()) {
            sendUpdate();
            setChanged();
        }
    }

    private void activateCooldownState() {
        degradation++;
        degradationExecutor.setTickInterval(DEGRADATION_TICK_INTERVAL / degradation);

        tick = getMaxCooldownTick();
        state = BlockEnvironmentalAccumulator.STATE_COOLING_DOWN;

        recipe = null;

        if (!level.isClientSide()) {
            sendUpdate();
            setChanged();
        }
    }

    @Override
    public void onUpdateReceived() {
        // If we receive an update from the server and our new state is the
        // finished processing item state, show the corresponding effect
        if (level.isClientSide() && state == BlockEnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM) {
            // Show an effect indicating the item finished processing.
            this.level.globalLevelEvent(2002, getBlockPos().offset(0, (int) WEATHER_CONTAINER_SPAWN_HEIGHT, 0), 16428);
        }

        // Change the beam colors if we receive an update
        setBeamColor(state);
    }

    /**
     * Set the beam colors.
     * @param state The state to base the colors on.
     */
    public void setBeamColor(int state) {
        if (level.isClientSide()) {
            setBeamColor(getOuterColorByState(state));
        }
    }

    public int getState() {
        return state;
    }

    @Override
    public void read(CompoundTag compound) {
        super.read(compound);

        inventory.readFromNBT(compound, "inventory");

        degradation = compound.getInt("degradation");
        tick = compound.getInt("tick");
        state = compound.getInt("state");

        // Delay loading of recipe when world is set during ticking
        this.recipeId = compound.getString("recipe");

        degradationExecutor.read(compound);

        if (getLevel() != null && getLevel().isClientSide()) {
            setBeamColor(getOuterColorByState(state));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        inventory.writeToNBT(tag, "inventory");

        tag.putInt("degradation", degradation);
        tag.putInt("tick", tick);
        tag.putInt("state", state);

        if (recipe != null)
            tag.putString("recipe", recipe.getId().toString());

        degradationExecutor.write(tag);
    }

    public float getMaxHealth() {
        if (state == BlockEnvironmentalAccumulator.STATE_PROCESSING_ITEM)
            return getItemMoveDuration();

        if (state == BlockEnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM)
            return 0;

        return getMaxCooldownTick();
    }

    public float getHealth() {
        if (state == BlockEnvironmentalAccumulator.STATE_PROCESSING_ITEM)
            return tick;

        if (state == BlockEnvironmentalAccumulator.STATE_COOLING_DOWN)
            return getMaxCooldownTick() - tick;

        return getMaxCooldownTick();
    }

    @Override
    public BlockPos getLocation() {
        return getBlockPos();
    }

    @Override
    public int getRadius() {
        return DEGRADATION_RADIUS_BASE + degradation / 10;
    }

    @Override
    public List<Entity> getAreaEntities() {
        return EntityHelpers.getEntitiesInArea(getDegradationWorld(), getBlockPos(), getRadius());
    }

    @Override
    public double getDegradation() {
        return this.degradation;
    }

    @Override
    public Level getDegradationWorld() {
        return getLevel();
    }

    public ServerBossEvent getBossInfo() {
        return bossInfo;
    }

    public static class Inventory extends SimpleInventory implements RecipeEnvironmentalAccumulator.Inventory {
        private final BlockEntityEnvironmentalAccumulator tile;

        public Inventory(BlockEntityEnvironmentalAccumulator tile) {
            super(1, 64);
            this.tile = tile;
        }

        @Override
        public Level getWorld() {
            return this.tile.getLevel();
        }

        @Override
        public BlockPos getPos() {
            return this.tile.getBlockPos();
        }
    }

    public static class Ticker extends BlockEntityTickerDelayed<BlockEntityEnvironmentalAccumulator> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityEnvironmentalAccumulator blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            // Delayed loading of recipe when world is set
            if (blockEntity.recipeId != null && level != null) {
                blockEntity.recipe = (RecipeEnvironmentalAccumulator) level.getRecipeManager().byKey(new ResourceLocation(blockEntity.recipeId)).orElse(null);
                blockEntity.recipeId = null;
            }

            // Keep ticking if necessary
            if (blockEntity.tick > 0)
                blockEntity.tick--;

            if (blockEntity.state == BlockEnvironmentalAccumulator.STATE_IDLE) {
                blockEntity.updateEnvironmentalAccumulatorIdle();
            } // Are we processing an item?
            else if (blockEntity.state == BlockEnvironmentalAccumulator.STATE_PROCESSING_ITEM) {
                if (level.isClientSide()) {
                    blockEntity.showWaterBeams();
                    if (blockEntity.tick > MAX_AGE) {
                        blockEntity.showAccumulatingParticles();
                    }
                }
                // Are we done moving the item?
                if (blockEntity.tick == 0) {
                    blockEntity.dropItemStack();
                    blockEntity.activateFinishedProcessingItemState();
                    blockEntity.getBossInfo().removeAllPlayers();
                    blockEntity.recreateBossInfo(); // Needed to allow clients to show bar increase instead of reduce
                } else {
                    blockEntity.sendUpdate();
                }
            } // Have we just finished processing an item?
            else if (blockEntity.state == BlockEnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM) {
                // We stay in this state for a while so the client gets some time to
                // show the corresponding effect when an item is finished processing

                // Are we done waiting for the client to update?
                if (blockEntity.tick == 0) {
                    blockEntity.activateCooldownState();

                    // Remove the items in our inventory
                    blockEntity.getInventory().removeItem(0, blockEntity.getInventory().getMaxStackSize());
                } else {
                    blockEntity.sendUpdate();
                }
            } // Are we cooling down?
            else if (blockEntity.state == BlockEnvironmentalAccumulator.STATE_COOLING_DOWN) {
                blockEntity.setBeamColor(blockEntity.state);
                blockEntity.degradationExecutor.runRandomEffect(level.isClientSide());

                // Are we done cooling down?
                if (blockEntity.tick == 0) {
                    blockEntity.activateIdleState();
                } else {
                    blockEntity.sendUpdate();
                }
            }

            if (!level.isClientSide()) {
                // Update boss bars for nearby players
                blockEntity.getBossInfo().setProgress(blockEntity.getHealth() / blockEntity.getMaxHealth());
                Set<Integer> playerIds = Sets.newHashSet();
                if (blockEntity.getHealth() != blockEntity.getMaxHealth()) {
                    for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(32D))) {
                        blockEntity.getBossInfo().addPlayer((ServerPlayer) player);
                        playerIds.add(player.getId());
                    }
                }

                // Remove players that aren't in the range for this tick
                Collection<ServerPlayer> players = Lists.newArrayList(blockEntity.getBossInfo().getPlayers());
                for (ServerPlayer playerMP : players) {
                    if (!playerIds.contains(playerMP.getId()) || blockEntity.getHealth() == 0) {
                        blockEntity.getBossInfo().removePlayer(playerMP);
                    }
                }
            }
        }
    }
}
