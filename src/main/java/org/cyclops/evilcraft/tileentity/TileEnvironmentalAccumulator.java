package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.block.BlockEnvironmentalAccumulator;
import org.cyclops.evilcraft.block.BlockEnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.client.particle.ParticleBlurTargettedData;
import org.cyclops.evilcraft.client.particle.ParticleBubbleExtendedData;
import org.cyclops.evilcraft.core.degradation.DegradationExecutor;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.tileentity.EvilCraftBeaconTileEntity;
import org.cyclops.evilcraft.core.weather.WeatherType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Machine that can accumulate the weather and put it in a bottle.
 * @author immortaleeb
 *
 */
public class TileEnvironmentalAccumulator extends EvilCraftBeaconTileEntity implements IDegradable {

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

    @Delegate
    private final CyclopsTileEntity.ITickingTile tickingTileComponent = new CyclopsTileEntity.TickingTileComponent(this);
    
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
    private ServerBossInfo bossInfo = null;

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
	public TileEnvironmentalAccumulator() {
	    super(RegistryEntries.TILE_ENTITY_ENVIRONMENTAL_ACCUMULATOR);
	    recreateBossInfo();
	    
	    degradationExecutor = new DegradationExecutor(this);
	    
	    inventory = new Inventory(this);
        addCapabilityInternal(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, LazyOptional.of(inventory::getItemHandler));
        inventory.addDirtyMarkListener(this);
	    
	    if (MinecraftHelpers.isClientSide()) {
	        setBeamColor(getOuterColorByState(state));
	    }
	}

    public Inventory getInventory() {
        return inventory;
    }

    protected void recreateBossInfo() {
        this.bossInfo = (ServerBossInfo)(new ServerBossInfo(
                new TranslationTextComponent("chat.evilcraft.boss_display.charge"),
                BossInfo.Color.PURPLE,
                BossInfo.Overlay.PROGRESS))
                .setDarkenSky(false);
    }

    protected Triple<Float, Float, Float> getBaseBeamColor() {
        if (getWorld() == null) {
            return Triple.of(0F, 0F, 0F);
        }
        Biome biome = getWorld().getBiome(getPos());
        return Helpers.intToRGB(biome.getGrassColor(getPos().getX(), getPos().getZ()));
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
	public void updateTileEntity() {
		super.updateTileEntity();

		// Delayed loading of recipe when world is set
		if (recipeId != null && getWorld() != null) {
            recipe = (RecipeEnvironmentalAccumulator) world.getRecipeManager().getRecipe(new ResourceLocation(recipeId)).orElse(null);
            recipeId = null;
        }

		if (!getWorld().isRemote()) {
            // Keep ticking if necessary
            if (tick > 0)
                tick--;

            if (state == BlockEnvironmentalAccumulator.STATE_IDLE) {
                updateEnvironmentalAccumulatorIdle();
            } // Are we processing an item?
            else if (state == BlockEnvironmentalAccumulator.STATE_PROCESSING_ITEM) {
                if (world.isRemote()) {
                    showWaterBeams();
                    if (tick > MAX_AGE) {
                        showAccumulatingParticles();
                    }
                }
                // Are we done moving the item?
                if (tick == 0) {
                    dropItemStack();
                    activateFinishedProcessingItemState();
                    getBossInfo().removeAllPlayers();
                    recreateBossInfo(); // Needed to allow clients to show bar increase instead of reduce
                } else {
                    sendUpdate();
                }
            } // Have we just finished processing an item?
            else if (state == BlockEnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM) {
                // We stay in this state for a while so the client gets some time to
                // show the corresponding effect when an item is finished processing

                // Are we done waiting for the client to update?
                if (tick == 0) {
                    activateCooldownState();

                    // Remove the items in our inventory
                    this.getInventory().decrStackSize(0, this.getInventory().getInventoryStackLimit());
                } else {
                    sendUpdate();
                }
            } // Are we cooling down?
            else if (state == BlockEnvironmentalAccumulator.STATE_COOLING_DOWN) {
                setBeamColor(state);
                degradationExecutor.runRandomEffect(world.isRemote());

                // Are we done cooling down?
                if (tick == 0) {
                    activateIdleState();
                } else {
                    sendUpdate();
                }
            }

            if (!getWorld().isRemote()) {
                // Update boss bars for nearby players
                getBossInfo().setPercent(this.getHealth() / this.getMaxHealth());
                Set<Integer> playerIds = Sets.newHashSet();
                if (getHealth() != getMaxHealth()) {
                    for (PlayerEntity player : getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(getPos()).grow(32D))) {
                        getBossInfo().addPlayer((ServerPlayerEntity) player);
                        playerIds.add(player.getEntityId());
                    }
                }

                // Remove players that aren't in the range for this tick
                Collection<ServerPlayerEntity> players = Lists.newArrayList(getBossInfo().getPlayers());
                for (ServerPlayerEntity playerMP : players) {
                    if (!playerIds.contains(playerMP.getEntityId()) || this.getHealth() == 0) {
                        getBossInfo().removePlayer(playerMP);
                    }
                }
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        getBossInfo().removeAllPlayers();
    }

    @OnlyIn(Dist.CLIENT)
    protected void showWaterBeams() {
        Random random = world.rand;
        BlockPos target = getPos();
        for (int j = 0; j < waterOffsets.length; j++) {
            BlockPos offset = waterOffsets[j];
            BlockPos location = target.add(offset);
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

                double particleMotionX = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * speed;
                double particleMotionY = MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * speed * 5;
                double particleMotionZ = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * speed;

                Minecraft.getInstance().worldRenderer.addParticle(
                        new ParticleBubbleExtendedData(0.02F), false,
                        particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void showAccumulatingParticles() {
        showAccumulatingParticles(world, getPos().getX() + 0.5F, getPos().getY() + 0.5F, getPos().getZ() + 0.5F, SPREAD);
    }

    @OnlyIn(Dist.CLIENT)
    public static void showAccumulatingParticles(World world, float centerX, float centerY, float centerZ, float spread) {
        Random rand = world.rand;
        for (int j = 0; j < rand.nextInt(20); j++) {
            float scale = 0.6F - rand.nextFloat() * 0.4F;
            float red = rand.nextFloat() * 0.1F + 0.2F;
            float green = rand.nextFloat() * 0.1F + 0.3F;
            float blue = rand.nextFloat() * 0.1F + 0.2F;
            float ageMultiplier = MAX_AGE + 10;

            float motionX = spread - rand.nextFloat() * 2 * spread;
            float motionY = spread - rand.nextFloat() * 2 * spread;
            float motionZ = spread - rand.nextFloat() * 2 * spread;

            Minecraft.getInstance().worldRenderer.addParticle(
                    new ParticleBlurTargettedData(red, green, blue, scale, ageMultiplier, centerX, centerY, centerZ), false,
                    centerX, centerY, centerZ, motionX, motionY, motionZ);
        }
    }

    protected IRecipeType<RecipeEnvironmentalAccumulator> getRegistry() {
        return RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR;
    }
	
	private void updateEnvironmentalAccumulatorIdle() {
        // Look for items thrown into the beam
        @SuppressWarnings("rawtypes")
        List entityItems = world.getEntitiesWithinAABB(ItemEntity.class,
                new AxisAlignedBB(
                        getPos().getX(), getPos().getY() + WEATHER_CONTAINER_MIN_DROP_HEIGHT, getPos().getZ(),
                        getPos().getX() + 1.0, getPos().getY() + WEATHER_CONTAINER_MAX_DROP_HEIGHT, getPos().getZ() + 1.0)
                );

        // Loop over all recipes until we find an item dropped in the accumulator that matches a recipe
        for (RecipeEnvironmentalAccumulator recipe : CraftingHelpers.findServerRecipes(getRegistry())) {
            Ingredient recipeIngredient = recipe.getInputIngredient();
            WeatherType weatherType = recipe.getInputWeather();

            // Loop over all dropped items
            for (Object obj : entityItems) {
                ItemEntity entityItem = (ItemEntity) obj;
                ItemStack stack = entityItem.getItem();

                if (recipeIngredient.test(stack) && (weatherType == null || weatherType.isActive(world))) {

                    // Save the required input items in the inventory
                    this.getInventory().setInventorySlotContents(0, stack.copy());

                    // Save the recipe
                    this.recipe = recipe;

                    if (!world.isRemote()) {
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
	        entityItem.remove();
	}
	
	private void dropItemStack() {
	    if (!world.isRemote()) {
	        // EntityItem that will contain the dropped itemstack
	        ItemEntity entity = new ItemEntity(world, getPos().getX(), getPos().getY() + WEATHER_CONTAINER_SPAWN_HEIGHT, getPos().getZ());
	        
	        if (recipe == null) {
	            // No recipe found, throw the item stack in the inventory back
	            // (NOTE: this can be caused because of weather changes)
	            entity.setItem(this.getInventory().getStackInSlot(0));
	        } else {
	            // Recipe found, throw back the result
	            entity.setItem(recipe.getCraftingResult(getInventory()));
	            
	            // Change the weather to the resulting weather
	            WeatherType weatherSource = recipe.getInputWeather();
                if (weatherSource != null)
                    weatherSource.deactivate(world);

                WeatherType weatherResult = recipe.getOutputWeather();
                if (weatherResult != null)
                    weatherResult.activate(world);
	        }
	        
    	    // Drop the items on the ground
            world.addEntity(entity);
	    }
	}
	
	private void activateIdleState() {
        tick = 0;
        state = BlockEnvironmentalAccumulator.STATE_IDLE;
        
        if (!world.isRemote()) {
            sendUpdate();
            markDirty();
        }
    }
	
	private void activateProcessingItemState() {
	    // Set the duration for processing the item
	    if (recipe == null)
	        tick = BlockEnvironmentalAccumulatorConfig.defaultProcessItemTickCount;
	    else
	        tick = recipe.getDuration();
	    
	    state = BlockEnvironmentalAccumulator.STATE_PROCESSING_ITEM;

        if (!world.isRemote()) {
            sendUpdate();
            markDirty();
        }
	}
	
	private void activateFinishedProcessingItemState() {
	    tick = ITEM_MOVE_COOLDOWN_DURATION;
	    state = BlockEnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM;

        if (!world.isRemote()) {
            sendUpdate();
            markDirty();
        }
	}
	
	private void activateCooldownState() {
	    degradation++;
	    degradationExecutor.setTickInterval(DEGRADATION_TICK_INTERVAL / degradation);
	    
	    tick = getMaxCooldownTick();
	    state = BlockEnvironmentalAccumulator.STATE_COOLING_DOWN;

        recipe = null;

        if (!world.isRemote()) {
            sendUpdate();
            markDirty();
        }
	}
	
	@Override
	public void onUpdateReceived() {
	    // If we receive an update from the server and our new state is the
	    // finished processing item state, show the corresponding effect
	    if (world.isRemote() && state == BlockEnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM) {
	        // Show an effect indicating the item finished processing.
	        this.world.playBroadcastSound(2002, getPos().add(0, WEATHER_CONTAINER_SPAWN_HEIGHT, 0), 16428);
	    }
	    
	    // Change the beam colors if we receive an update
	    setBeamColor(state);
	}
	
	/**
	 * Set the beam colors.
	 * @param state The state to base the colors on.
	 */
	public void setBeamColor(int state) {
        if (world.isRemote()) {
    	    setBeamColor(getOuterColorByState(state));
        }
	}

    public int getState() {
        return state;
    }

    @Override
	public void read(CompoundNBT compound) {
	    super.read(compound);

        inventory.readFromNBT(compound, "inventory");
	    
	    degradation = compound.getInt("degradation");
	    tick = compound.getInt("tick");
	    state = compound.getInt("state");

        // Delay loading of recipe when world is set during ticking
	    this.recipeId = compound.getString("recipe");
	    
	    degradationExecutor.read(compound);

        if (getWorld() != null && getWorld().isRemote()) {
            setBeamColor(getOuterColorByState(state));
        }
	}
	
	@Override
	public CompoundNBT write(CompoundNBT tag) {
	    tag = super.write(tag);

        inventory.writeToNBT(tag, "inventory");
	    
	    tag.putInt("degradation", degradation);
	    tag.putInt("tick", tick);
	    tag.putInt("state", state);
	    
	    if (recipe != null)
	        tag.putString("recipe", recipe.getId().toString());
	    
	    degradationExecutor.write(tag);
        return tag;
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
    	return getPos();
    }

    @Override
    public int getRadius() {
        return DEGRADATION_RADIUS_BASE + degradation / 10;
    }

    @Override
    public List<Entity> getAreaEntities() {
        return EntityHelpers.getEntitiesInArea(getDegradationWorld(), getPos(), getRadius());
    }

    @Override
    public double getDegradation() {
        return this.degradation;
    }

    @Override
    public World getDegradationWorld() {
        return getWorld();
    }

    public ServerBossInfo getBossInfo() {
        return bossInfo;
    }

    public static class Inventory extends SimpleInventory implements RecipeEnvironmentalAccumulator.Inventory {
	    private final TileEnvironmentalAccumulator tile;

        public Inventory(TileEnvironmentalAccumulator tile) {
            super(1, 64);
            this.tile = tile;
        }

        @Override
        public World getWorld() {
            return this.tile.getWorld();
        }

        @Override
        public BlockPos getPos() {
            return this.tile.getPos();
        }
    }
}
