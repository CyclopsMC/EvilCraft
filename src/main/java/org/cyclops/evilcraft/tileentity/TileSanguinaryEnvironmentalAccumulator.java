package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.block.SanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.client.particle.EntityBloodBubbleFX;
import org.cyclops.evilcraft.core.fluid.VirtualTank;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;

import javax.annotation.Nullable;
import java.util.*;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class TileSanguinaryEnvironmentalAccumulator extends TileWorking<TileSanguinaryEnvironmentalAccumulator, MutableInt> implements VirtualTank.ITankProvider {

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
    /**
     * The fluid that is accepted in the tank.
     */
    public static final Fluid ACCEPTED_FLUID = Blood.getInstance();

    private static final int TANK_CHECK_TICK_OFFSET = 60;

    private int accumulateTicker;
    private SingleCache<Triple<ItemStack, FluidStack, WeatherType>,
            IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>> recipeCache;
    private VirtualTank virtualTank;
    private boolean forceLoadTanks;
    @Getter
    private List<BlockPos> invalidLocations = Lists.newArrayList();

    private static final Map<Class<?>, ITickAction<TileSanguinaryEnvironmentalAccumulator>> ACCUMULATE_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileSanguinaryEnvironmentalAccumulator>>();
    static {
        ACCUMULATE_TICK_ACTIONS.put(Item.class, new AccumulateItemTickAction());
    }

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

    /**
     * Make a new instance.
     */
    public TileSanguinaryEnvironmentalAccumulator() {
        super(
                SLOTS,
                SanguinaryEnvironmentalAccumulator.getInstance().getLocalizedName(),
                0,
                "",
                ACCEPTED_FLUID);
        accumulateTicker = addTicker(
                new TickComponent<
                                        TileSanguinaryEnvironmentalAccumulator,
                                    ITickAction<TileSanguinaryEnvironmentalAccumulator>
                                >(this, ACCUMULATE_TICK_ACTIONS, SLOT_ACCUMULATE)
                );

        // The slots side mapping
        List<Integer> inSlots = new LinkedList<Integer>();
        inSlots.add(SLOT_ACCUMULATE);
        List<Integer> outSlots = new LinkedList<Integer>();
        outSlots.add(SLOT_ACCUMULATE_RESULT);
        addSlotsToSide(EnumFacing.EAST, inSlots);
        addSlotsToSide(EnumFacing.UP, inSlots);
        addSlotsToSide(EnumFacing.NORTH, inSlots);
        addSlotsToSide(EnumFacing.DOWN, outSlots);
        addSlotsToSide(EnumFacing.SOUTH, outSlots);
        addSlotsToSide(EnumFacing.WEST, outSlots);

        // Upgrade behaviour
        upgradeBehaviour.put(UPGRADE_EFFICIENCY, new UpgradeBehaviour<TileSanguinaryEnvironmentalAccumulator, MutableInt>(2) {
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
        upgradeBehaviour.put(UPGRADE_SPEED, new UpgradeBehaviour<TileSanguinaryEnvironmentalAccumulator, MutableInt>(1) {
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
        recipeCache = new SingleCache<Triple<ItemStack, FluidStack, WeatherType>,
                IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>>(
                new SingleCache.ICacheUpdater<Triple<ItemStack, FluidStack, WeatherType>,
                        IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>>() {
            @Override
            public IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> getNewValue(Triple<ItemStack, FluidStack, WeatherType> key) {
                EnvironmentalAccumulatorRecipeComponent recipeInput = new EnvironmentalAccumulatorRecipeComponent(key.getLeft(),
                        key.getRight());
                for(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe :
                        EnvironmentalAccumulator.getInstance().getRecipeRegistry().findRecipesByInput(recipeInput)) {
                    if(recipe.getInput().getWeatherType().isActive(worldObj)) {
                        return recipe;
                    }
                }
                return null;
            }

            @Override
            public boolean isKeyEqual(Triple<ItemStack, FluidStack, WeatherType> cacheKey, Triple<ItemStack, FluidStack, WeatherType> newKey) {
                return cacheKey == null || newKey == null ||
                        (ItemStack.areItemStacksEqual(cacheKey.getLeft(), newKey.getLeft()) &&
                        FluidStack.areFluidStackTagsEqual(cacheKey.getMiddle(), newKey.getMiddle()) &&
                        cacheKey.getRight() == newKey.getRight());
            }
        });

        this.virtualTank = new VirtualTank(this, true);
        this.forceLoadTanks = true;
    }

    protected SingleUseTank newTank(String tankName, int tankSize) {
        return new SingleUseTank("noTank", 0, this); // Dummy tank
    }

    /**
     * Get the recipe for the maximum current tier available.
     * @param itemStack The input item.
     * @return The recipe.
     */
    public IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>
        getRecipe(ItemStack itemStack) {
        return recipeCache.get(new ImmutableTriple<ItemStack, FluidStack, WeatherType>(
                itemStack == null ? null : itemStack.copy(),
                getTank().getFluid() == null ? null : getTank().getFluid().copy(),
                WeatherType.getActiveWeather(worldObj)));
    }

    @Override
    public void updateTileEntity() {
        super.updateTileEntity();
        if(worldObj.isRemote && isVisuallyWorking()) {
            showTankBeams();
            if((getRequiredWorkTicks() - getWorkTick()) > TileEnvironmentalAccumulator.MAX_AGE) {
                showAccumulatingParticles();
            }

        } else if(worldObj.isRemote && !canWork()) {
            showMissingTanks();
        }
    }

    @SideOnly(Side.CLIENT)
    protected void showAccumulatingParticles() {
        TileEnvironmentalAccumulator.showAccumulatingParticles(worldObj, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, TileEnvironmentalAccumulator.SPREAD);
    }

    @SideOnly(Side.CLIENT)
    protected void showTankBeams() {
        Random random = worldObj.rand;
        BlockPos target = getPos();
        for (int j = 0; j < tankOffsets.length; j++) {
            BlockPos offset = tankOffsets[j];
            BlockPos location = target.add(offset);
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

                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new EntityBloodBubbleFX(worldObj, particleX, particleY, particleZ,
                                particleMotionX, particleMotionY, particleMotionZ)
                );
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void showMissingTanks() {
        if(worldObj.getTotalWorldTime() % 10 == 0) {
            Random random = worldObj.rand;
            for (BlockPos location : invalidLocations) {
                double x = location.getX() + 0.5;
                double y = location.getY() + 0.5;
                double z = location.getZ() + 0.5;

                for (int i = 0; i < 1 + random.nextInt(5); i++) {
                    double particleX = x - 0.2 + random.nextDouble() * 0.4;
                    double particleY = y - 0.2 + random.nextDouble() * 0.4;
                    double particleZ = z - 0.2 + random.nextDouble() * 0.4;

                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                            new EntitySmokeFX.Factory().getEntityFX(0, worldObj, particleX, particleY, particleZ, 0, 0, 0)
                    );
                }
            }
        }
    }

    @Override
    public boolean canConsume(ItemStack itemStack) {
        // Valid custom recipe
        IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe =
                getRecipe(itemStack);
        if(recipe != null)
            return true;

        // In all other cases: false
        return false;
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

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == SLOT_ACCUMULATE) {
            return canConsume(itemStack);
        }
        return super.isItemValidForSlot(slot, itemStack);
    }

    @Override
    public void onStateChanged() {
        sendUpdate();
        IBlockState blockState = worldObj.getBlockState(getPos()).withProperty(SanguinaryEnvironmentalAccumulator.ON, isWorking());
        worldObj.setBlockState(getPos(), blockState);
        worldObj.notifyBlockUpdate(getPos(), blockState, blockState, MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT); // Update light
    }

	@Override
	public boolean canWork() {
        if(!forceLoadTanks && invalidLocations != null && !WorldHelpers.efficientTick(worldObj, TANK_CHECK_TICK_OFFSET, getPos())) {
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
            BlockPos location = getPos().add(offset);
            TileEntity tile = worldObj.getTileEntity(location);
            if (tile == null || !(tile instanceof IFluidHandler)) {
                invalidLocations.add(location);
                continue;
            }
            IFluidHandler handler = (IFluidHandler) tile;
            FluidTankInfo[] info = handler.getTankInfo(VirtualTank.TARGETSIDE);
            boolean oneValid = false;
            for(FluidTankInfo tank : info) {
                if (tank.fluid != null && tank.fluid.getFluid() == ACCEPTED_FLUID) {
                    oneValid = true;
                    break;
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
}
