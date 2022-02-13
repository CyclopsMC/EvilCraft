package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.capability.item.ItemHandlerSlotMasked;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.fluid.VirtualTank;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class TileSanguinaryEnvironmentalAccumulator extends TileWorking<TileSanguinaryEnvironmentalAccumulator, MutableInt>
        implements VirtualTank.ITankProvider, INamedContainerProvider {

    /**
     * The total amount of slots in this machine.
     */
    public static final int SLOTS = 2;
    /**
     * The id of the accumulation slot.
     */
    public static final int SLOT_ACCUMULATE = 0;
    /**
     * The id of the accumulation result slot.
     */
    public static final int SLOT_ACCUMULATE_RESULT = 1;

    public static Metadata METADATA = new Metadata();

    private static final int TANK_CHECK_TICK_OFFSET = 60;

    private int accumulateTicker;
    private SingleCache<Triple<ItemStack, FluidStack, WeatherType>, Optional<RecipeEnvironmentalAccumulator>> recipeCache;
    private VirtualTank virtualTank;
    private boolean forceLoadTanks;
    @Getter
    private List<BlockPos> invalidLocations = Lists.newArrayList();

    private static final Map<Class<?>, ITickAction<TileSanguinaryEnvironmentalAccumulator>> ACCUMULATE_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileSanguinaryEnvironmentalAccumulator>>();
    static {
        ACCUMULATE_TICK_ACTIONS.put(Item.class, new AccumulateItemTickAction());
    }
    public static int TICKERS = 1;

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();

    private static final BlockPos[] tankOffsets = new BlockPos[]{
            new BlockPos(-3, 0, -1),
            new BlockPos(-3, 0, 1),
            new BlockPos(3, 0, -1),
            new BlockPos(3, 0, 1),
            new BlockPos(-1, 0, -3),
            new BlockPos(-1, 0, 3),
            new BlockPos(1, 0, -3),
            new BlockPos(1, 0, 3),
    };

    public TileSanguinaryEnvironmentalAccumulator() {
        super(
                RegistryEntries.TILE_ENTITY_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR,
                SLOTS,
                64,
                0,
                RegistryEntries.FLUID_BLOOD);
        accumulateTicker = addTicker(
                new TickComponent<>(this, ACCUMULATE_TICK_ACTIONS, SLOT_ACCUMULATE)
                );
        assert getTickers().size() == TICKERS;

        // Upgrade behaviour
        upgradeBehaviour.put(Upgrades.UPGRADE_EFFICIENCY, new UpgradeBehaviour<TileSanguinaryEnvironmentalAccumulator, MutableInt>(2) {
            @Override
            public void applyUpgrade(TileSanguinaryEnvironmentalAccumulator upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableInt> event) {
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    int val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
        upgradeBehaviour.put(Upgrades.UPGRADE_SPEED, new UpgradeBehaviour<TileSanguinaryEnvironmentalAccumulator, MutableInt>(1) {
            @Override
            public void applyUpgrade(TileSanguinaryEnvironmentalAccumulator upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableInt> event) {
                if(event.getType() == UPGRADEEVENT_SPEED) {
                    int val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });

        // Efficient cache to retrieve the current craftable recipe.
        recipeCache = new SingleCache<>(
                new SingleCache.ICacheUpdater<Triple<ItemStack, FluidStack, WeatherType>, Optional<RecipeEnvironmentalAccumulator>>() {
                    @Override
                    public Optional<RecipeEnvironmentalAccumulator> getNewValue(Triple<ItemStack, FluidStack, WeatherType> key) {
                        Inventory recipeInput = new Inventory(1, 64, TileSanguinaryEnvironmentalAccumulator.this);
                        recipeInput.setItem(0, key.getLeft());
                        return CraftingHelpers.findServerRecipe(getRegistry(), recipeInput, getLevel());
                    }

                    @Override
                    public boolean isKeyEqual(Triple<ItemStack, FluidStack, WeatherType> cacheKey, Triple<ItemStack, FluidStack, WeatherType> newKey) {
                        return cacheKey == null || newKey == null ||
                                (ItemStack.matches(cacheKey.getLeft(), newKey.getLeft()) &&
                                        FluidStack.areFluidStackTagsEqual(cacheKey.getMiddle(), newKey.getMiddle()) &&
                                        cacheKey.getRight() == newKey.getRight());
                    }
                });

        this.virtualTank = new VirtualTank(this, true);
        this.forceLoadTanks = true;
    }

    @Override
    protected void addItemHandlerCapabilities() {
        LazyOptional<IItemHandler> itemHandlerAccumulate = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_ACCUMULATE));
        LazyOptional<IItemHandler> itemHandlerAccumulateResult = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_ACCUMULATE_RESULT));
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP, itemHandlerAccumulate);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN, itemHandlerAccumulateResult);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.NORTH, itemHandlerAccumulate);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.SOUTH, itemHandlerAccumulate);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.WEST, itemHandlerAccumulate);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.EAST, itemHandlerAccumulate);
    }

    @Override
    protected void addFluidHandlerCapabilities() {
        // Do not expose dummy tank
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory(inventorySize, stackSize, this) {
            @Override
            public boolean canPlaceItem(int slot, ItemStack itemStack) {
                if(slot == SLOT_ACCUMULATE) {
                    return getTileWorkingMetadata().canConsume(itemStack, getWorld());
                }
                return super.canPlaceItem(slot, itemStack);
            }
        };
    }

    @Override
    public Inventory getInventory() {
        return (Inventory) super.getInventory();
    }

    /**
     * Get the recipe for the maximum current tier available.
     * @param itemStack The input item.
     * @return The recipe.
     */
    public Optional<RecipeEnvironmentalAccumulator> getRecipe(ItemStack itemStack) {
        return recipeCache.get(new ImmutableTriple<>(
                itemStack.isEmpty() ? ItemStack.EMPTY : itemStack.copy(),
                getTank().getFluid().copy(),
                WeatherType.getActiveWeather(level)));
    }

    protected IRecipeType<RecipeEnvironmentalAccumulator> getRegistry() {
        return RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR;
    }

    @Override
    public void updateTileEntity() {
        super.updateTileEntity();
        if(level.isClientSide() && isVisuallyWorking()) {
            showTankBeams();
            if((getRequiredWorkTicks() - getWorkTick()) > TileEnvironmentalAccumulator.MAX_AGE) {
                showAccumulatingParticles();
            }

        } else if(level.isClientSide() && !canWork()) {
            showMissingTanks();
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void showAccumulatingParticles() {
        TileEnvironmentalAccumulator.showAccumulatingParticles(level, getBlockPos().getX() + 0.5F, getBlockPos().getY() + 0.5F, getBlockPos().getZ() + 0.5F, TileEnvironmentalAccumulator.SPREAD);
    }

    @OnlyIn(Dist.CLIENT)
    protected void showTankBeams() {
        Random random = level.random;
        BlockPos target = getBlockPos();
        for (int j = 0; j < tankOffsets.length; j++) {
            BlockPos offset = tankOffsets[j];
            BlockPos location = target.offset(offset);
            double x = location.getX() + 0.5;
            double y = location.getY() + 0.5;
            double z = location.getZ() + 0.5;

            float rotationYaw = (float) LocationHelpers.getYaw(location, target);
            float rotationPitch = (float) LocationHelpers.getPitch(location, target);

            for (int i = 0; i < 1 + random.nextInt(5); i++) {
                double particleX = x - 0.2 + random.nextDouble() * 0.4;
                double particleY = y - 0.2 + random.nextDouble() * 0.4;
                double particleZ = z - 0.2 + random.nextDouble() * 0.4;

                double speed = 0.5;

                double particleMotionX = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * speed;
                double particleMotionY = MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * -speed;
                double particleMotionZ = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * speed;

                Minecraft.getInstance().levelRenderer.addParticle(
                        RegistryEntries.PARTICLE_BLOOD_BUBBLE, false,
                        particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void showMissingTanks() {
        if(level.getGameTime() % 10 == 0) {
            Random random = level.random;
            for (BlockPos location : invalidLocations) {
                double x = location.getX() + 0.5;
                double y = location.getY() + 0.5;
                double z = location.getZ() + 0.5;

                for (int i = 0; i < 1 + random.nextInt(5); i++) {
                    double particleX = x - 0.2 + random.nextDouble() * 0.4;
                    double particleY = y - 0.2 + random.nextDouble() * 0.4;
                    double particleZ = z - 0.2 + random.nextDouble() * 0.4;

                    Minecraft.getInstance().levelRenderer.addParticle(
                            ParticleTypes.SMOKE, false,
                            particleX, particleY, particleZ, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void onStateChanged() {
        BlockState blockState = level.getBlockState(getBlockPos()).setValue(BlockSanguinaryEnvironmentalAccumulator.ON, isWorking());
        level.setBlockAndUpdate(getBlockPos(), blockState);
        level.sendBlockUpdated(getBlockPos(), blockState, blockState, MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT); // Update light
    }

    @Override
    public Metadata getTileWorkingMetadata() {
        return METADATA;
    }

    @Override
	public boolean canWork() {
        if(!forceLoadTanks && invalidLocations != null && !WorldHelpers.efficientTick(level, TANK_CHECK_TICK_OFFSET, getBlockPos())) {
            return invalidLocations.isEmpty();
        }
        forceLoadTanks = false;
		return getVirtualTankChildren() != null;
	}

	@Override
	protected int getWorkTicker() {
		return accumulateTicker;
	}

    public VirtualTank getVirtualTank() {
        return this.virtualTank;
    }

    @Nullable
    @Override
    public IFluidHandler[] getVirtualTankChildren() {
        IFluidHandler[] tanks = new IFluidHandler[tankOffsets.length];
        invalidLocations.clear();
        for (int i = 0; i < tankOffsets.length; i++) {
            BlockPos offset = tankOffsets[i];
            BlockPos location = getBlockPos().offset(offset);
            IFluidHandler handler = TileHelpers.getCapability(getLevel(), location, Direction.UP, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
            boolean oneValid = false;
            if (handler != null) {
                int tankAmount = handler.getTanks();
                for (int tank = 0; tank < tankAmount; tank++) {
                    if (!handler.getFluidInTank(tank).isEmpty() && handler.getFluidInTank(tank).getFluid() == RegistryEntries.FLUID_BLOOD) {
                        oneValid = true;
                        break;
                    }
                }
            }
            if(!oneValid) {
                invalidLocations.add(location);
            }
            tanks[i] = handler;
        }
        if(!invalidLocations.isEmpty()) {
            return null;
        }
        return tanks;
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerSanguinaryEnvironmentalAccumulator(id, playerInventory, this.getInventory(), Optional.of(this));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.evilcraft.sanguinary_environmental_accumulator");
    }

    public static class Inventory extends TileWorking.Inventory<TileSanguinaryEnvironmentalAccumulator> implements RecipeEnvironmentalAccumulator.Inventory {

        public Inventory(int size, int stackLimit, TileSanguinaryEnvironmentalAccumulator tile) {
            super(size, stackLimit, tile);
        }

        @Override
        public World getWorld() {
            return tile.getLevel();
        }

        @Override
        public BlockPos getPos() {
            return tile.getBlockPos();
        }

        // TODO: rm everything below here!

        @Override
        public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack p_180462_2_, @Nullable Direction p_180462_3_) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack p_180461_2_, Direction p_180461_3_) {
            return false;
        }

        @Override
        public int getContainerSize() {
            return 0;
        }

        @Override
        public ItemStack getItem(int p_70301_1_) {
            return null;
        }

        @Override
        public ItemStack removeItemNoUpdate(int p_70304_1_) {
            return null;
        }

        @Override
        public void setChanged() {

        }

        @Override
        public boolean stillValid(PlayerEntity p_70300_1_) {
            return false;
        }

        @Override
        public void clearContent() {

        }
    }

    public static class Metadata extends TileWorking.Metadata {
        private Metadata() {
            super(SLOTS);
        }

        @Override
        public boolean canInsertItem(IInventory inventory, int slot, ItemStack itemStack) {
            return slot != getProduceSlot() && super.canInsertItem(inventory, slot, itemStack);
        }

        @Override
        public boolean canConsume(ItemStack itemStack, World world) {
            // Valid custom recipe
            RecipeEnvironmentalAccumulator.Inventory recipeInput = new RecipeEnvironmentalAccumulator.InventoryDummy(itemStack);
            return world.getRecipeManager()
                    .getRecipeFor(RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR, recipeInput, world)
                    .isPresent();
        }

        @Override
        protected Block getBlock() {
            return RegistryEntries.BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR;
        }

        /**
         * Get the id of the accumulate slot.
         * @return id of the accumulate slot.
         */
        public int getConsumeSlot() {
            return SLOT_ACCUMULATE;
        }

        /**
         * Get the id of the result slot.
         * @return id of the result slot.
         */
        public int getProduceSlot() {
            return SLOT_ACCUMULATE_RESULT;
        }
    }

}
