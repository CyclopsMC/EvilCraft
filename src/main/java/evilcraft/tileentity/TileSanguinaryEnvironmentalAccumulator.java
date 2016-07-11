package evilcraft.tileentity;

import com.google.common.collect.Lists;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.ILocation;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.EnvironmentalAccumulator;
import evilcraft.block.SanguinaryEnvironmentalAccumulator;
import evilcraft.client.particle.EntityBloodBubbleFX;
import evilcraft.core.algorithm.Location;
import evilcraft.core.algorithm.SingleCache;
import evilcraft.core.fluid.SingleUseTank;
import evilcraft.core.fluid.VirtualTank;
import evilcraft.core.helper.LocationHelpers;
import evilcraft.core.helper.WorldHelpers;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.tickaction.TickComponent;
import evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.core.weather.WeatherType;
import evilcraft.fluid.Blood;
import evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;
import lombok.Getter;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

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
    private List<ILocation> invalidLocations = Lists.newArrayList();

    private static final Map<Class<?>, ITickAction<TileSanguinaryEnvironmentalAccumulator>> ACCUMULATE_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileSanguinaryEnvironmentalAccumulator>>();
    static {
        ACCUMULATE_TICK_ACTIONS.put(Item.class, new AccumulateItemTickAction());
    }

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();

    private static final ILocation[] tankOffsets = new ILocation[]{
            new Location(-3, 0, -1),
            new Location(-3, 0, 1),
            new Location(3, 0, -1),
            new Location(3, 0, 1),
            new Location(-1, 0, -3),
            new Location(-1, 0, 3),
            new Location(1, 0, -3),
            new Location(1, 0, 3),
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
        addSlotsToSide(ForgeDirection.EAST, inSlots);
        addSlotsToSide(ForgeDirection.UP, inSlots);
        addSlotsToSide(ForgeDirection.NORTH, inSlots);
        addSlotsToSide(ForgeDirection.DOWN, outSlots);
        addSlotsToSide(ForgeDirection.SOUTH, outSlots);
        addSlotsToSide(ForgeDirection.WEST, outSlots);

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
        TileEnvironmentalAccumulator.showAccumulatingParticles(worldObj, xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, TileEnvironmentalAccumulator.SPREAD);
    }

    @SideOnly(Side.CLIENT)
    protected void showTankBeams() {
        Random random = worldObj.rand;
        ILocation target = new Location(xCoord, yCoord, zCoord);
        for (int j = 0; j < tankOffsets.length; j++) {
            ILocation offset = tankOffsets[j];
            ILocation location = target.add(offset);
            double x = location.getCoordinates()[0] + 0.5;
            double y = location.getCoordinates()[1] + 0.5;
            double z = location.getCoordinates()[2] + 0.5;

            float rotationYaw = (float) LocationHelpers.getYaw(location, target);
            float rotationPitch = (float) LocationHelpers.getPitch(location, target);

            for (int i = 0; i < 1 + random.nextInt(5); i++) {
                double particleX = x - 0.2 + random.nextDouble() * 0.4;
                double particleY = y - 0.2 + random.nextDouble() * 0.4;
                double particleZ = z - 0.2 + random.nextDouble() * 0.4;

                double speed = 0.5;

                double particleMotionX = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * speed;
                double particleMotionY = MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * -speed;
                double particleMotionZ = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * speed;

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
            for (ILocation location : invalidLocations) {
                double x = location.getCoordinates()[0] + 0.5;
                double y = location.getCoordinates()[1] + 0.5;
                double z = location.getCoordinates()[2] + 0.5;

                for (int i = 0; i < 1 + random.nextInt(5); i++) {
                    double particleX = x - 0.2 + random.nextDouble() * 0.4;
                    double particleY = y - 0.2 + random.nextDouble() * 0.4;
                    double particleZ = z - 0.2 + random.nextDouble() * 0.4;

                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                            new EntitySmokeFX(worldObj, particleX, particleY, particleZ, 0, 0, 0)
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
        return slot == SLOT_ACCUMULATE && canConsume(itemStack);
    }

    @Override
    public void onStateChanged() {
        sendUpdate();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord); // Update light
    }

	@Override
	public boolean canWork() {
        if(!forceLoadTanks && invalidLocations != null && !WorldHelpers.efficientTick(worldObj, TANK_CHECK_TICK_OFFSET, xCoord, yCoord, zCoord)) {
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
            ILocation offset = tankOffsets[i];
            ILocation location = new Location(xCoord, yCoord, zCoord).add(offset);
            TileEntity tile = LocationHelpers.getTile(worldObj, location);
            if (tile == null || !(tile instanceof IFluidHandler)) {
                invalidLocations.add(location);
                continue;
            }
            IFluidHandler handler = (IFluidHandler) tile;
            FluidTankInfo[] info = handler.getTankInfo(VirtualTank.TARGETSIDE);
            if (info == null) {
                invalidLocations.add(location);
                continue;
            }
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
