package evilcraft.tileentity;

import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.BloodInfuser;
import evilcraft.core.fluid.BloodFluidConverter;
import evilcraft.core.fluid.ImplicitFluidConversionTank;
import evilcraft.core.fluid.SingleUseTank;
import evilcraft.core.inventory.slot.SlotFluidContainer;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.tickaction.TickComponent;
import evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.fluid.Blood;
import evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import evilcraft.tileentity.tickaction.EmptyItemBucketInTankTickAction;
import evilcraft.tileentity.tickaction.bloodinfuser.FluidContainerItemTickAction;
import evilcraft.tileentity.tickaction.bloodinfuser.InfuseItemTickAction;
import evilcraft.tileentity.tickaction.bloodinfuser.ItemBucketTickAction;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class TileBloodInfuser extends TileWorking<TileBloodInfuser, MutableInt> {
    
    /**
     * The total amount of slots in this machine.
     */
    public static final int SLOTS = 3;
    /**
     * The id of the fluid container drainer slot.
     */
    public static final int SLOT_CONTAINER = 0;
    /**
     * The id of the infusion slot.
     */
    public static final int SLOT_INFUSE = 1;
    /**
     * The id of the infusion result slot.
     */
    public static final int SLOT_INFUSE_RESULT = 2;
    
    /**
     * The name of the tank, used for NBT storage.
     */
    public static String TANKNAME = "bloodInfuserTank";
    /**
     * The capacity of the tank.
     */
    public static final int LIQUID_PER_SLOT = FluidContainerRegistry.BUCKET_VOLUME * 10;
    /**
     * The amount of ticks per mB the tank can accept per tick.
     */
    public static final int TICKS_PER_LIQUID = 2;
    /**
     * The fluid that is accepted in the tank.
     */
    public static final Fluid ACCEPTED_FLUID = Blood.getInstance();
    
    private int infuseTicker;
    
    private static final Map<Class<?>, ITickAction<TileBloodInfuser>> INFUSE_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileBloodInfuser>>();
    static {
        INFUSE_TICK_ACTIONS.put(ItemBucket.class, new ItemBucketTickAction());
        INFUSE_TICK_ACTIONS.put(IFluidContainerItem.class, new FluidContainerItemTickAction());
        INFUSE_TICK_ACTIONS.put(Item.class, new InfuseItemTickAction());
    }
    
    private static final Map<Class<?>, ITickAction<TileBloodInfuser>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileBloodInfuser>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(ItemBucket.class, new EmptyItemBucketInTankTickAction<TileBloodInfuser>());
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction<TileBloodInfuser>());
    }

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_FILLBLOODPERTICK = Upgrades.newUpgradeEventType();
    
    /**
     * Make a new instance.
     */
    public TileBloodInfuser() {
        super(
                SLOTS,
                BloodInfuser.getInstance().getLocalizedName(),
                LIQUID_PER_SLOT,
                TileBloodInfuser.TANKNAME,
                ACCEPTED_FLUID);
        infuseTicker = addTicker(
                new TickComponent<
                    TileBloodInfuser,
                    ITickAction<TileBloodInfuser>
                >(this, INFUSE_TICK_ACTIONS, SLOT_INFUSE)
                );
        addTicker(
                new TickComponent<
                    TileBloodInfuser,
                    ITickAction<TileBloodInfuser>
                >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false)
                );
        
        // The slots side mapping
        List<Integer> inSlots = new LinkedList<Integer>();
        inSlots.add(SLOT_INFUSE);
        List<Integer> inSlotsTank = new LinkedList<Integer>();
        inSlotsTank.add(SLOT_CONTAINER);
        List<Integer> outSlots = new LinkedList<Integer>();
        outSlots.add(SLOT_INFUSE_RESULT);
        addSlotsToSide(ForgeDirection.EAST, inSlotsTank);
        addSlotsToSide(ForgeDirection.UP, inSlots);
        addSlotsToSide(ForgeDirection.DOWN, outSlots);
        addSlotsToSide(ForgeDirection.SOUTH, outSlots);
        addSlotsToSide(ForgeDirection.WEST, outSlots);

        // Upgrade behaviour
        upgradeBehaviour.put(UPGRADE_SPEED, new UpgradeBehaviour<TileBloodInfuser, MutableInt>(3.2) {
            @Override
            public void applyUpgrade(TileBloodInfuser upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableInt> event) {
                if(event.getType() == UPGRADEEVENT_SPEED) {
                    int val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
        upgradeBehaviour.put(UPGRADE_EFFICIENCY, new UpgradeBehaviour<TileBloodInfuser, MutableInt>(6.4) {
            @Override
            public void applyUpgrade(TileBloodInfuser upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableInt> event) {
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    int val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
        upgradeBehaviour.put(UPGRADE_SPEED, new UpgradeBehaviour<TileBloodInfuser, MutableInt>(3.2) {
            @Override
            public void applyUpgrade(TileBloodInfuser upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableInt> event) {
                if(event.getType() == UPGRADEEVENT_FILLBLOODPERTICK) {
                    int val = event.getObject().getValue();
                    val *= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
    }
    
    @Override
    protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new ImplicitFluidConversionTank(tankName, tankSize, this, BloodFluidConverter.getInstance());
    }

    /**
     * Get the recipe for the maximum current tier available.
     * @param itemStack The input item.
     * @return The recipe.
     */
    public IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>
        getRecipe(ItemStack itemStack) {
        ItemFluidStackAndTierRecipeComponent recipeInput = new ItemFluidStackAndTierRecipeComponent(itemStack,
                getTank().getFluid(), -1);
        IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> maxRecipe = null;
        int maxRecipeTier = -1;
        for(IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe :
                BloodInfuser.getInstance().getRecipeRegistry().findRecipesByInput(recipeInput)) {
            if(recipe.getInput().getTier() > maxRecipeTier && getTier() >= recipe.getInput().getTier()) {
                maxRecipe = recipe;
            }
        }
        return maxRecipe;
    }
    
    @Override
    public boolean canConsume(ItemStack itemStack) {
        // Empty bucket
        if(itemStack.getItem() == Items.bucket
                && this.getTank().getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME)
            return true;
        
        // Valid fluid container
        if(itemStack.getItem() instanceof IFluidContainerItem) {
            IFluidContainerItem container = (IFluidContainerItem) itemStack.getItem();
            FluidStack fluidStack = container.getFluid(itemStack);
            if(fluidStack == null) {
                return true;
            } else {
                if(getTank().canTankAccept(fluidStack.getFluid())
                        && fluidStack.amount < container.getCapacity(itemStack)) {
                    return true;
                }
            }
        }
        
        // Valid custom recipe
        IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe =
                getRecipe(itemStack);
        if(recipe != null)
            return true;
        
        // In all other cases: false
        return false;
    }
    
    /**
     * Get the id of the infusion slot.
     * @return id of the infusion slot.
     */
    public int getConsumeSlot() {
        return SLOT_INFUSE;
    }

    /**
     * Get the id of the result slot.
     * @return id of the result slot.
     */
    public int getProduceSlot() {
        return SLOT_INFUSE_RESULT;
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == SLOT_INFUSE)
            return canConsume(itemStack);
        if(slot == SLOT_CONTAINER)
            return SlotFluidContainer.checkIsItemValid(itemStack, getTank());
        return false;
    }

    @Override
    public void onStateChanged() {
        sendUpdate();
    }

	@Override
	public boolean canWork() {
		return true;
	}

	@Override
	protected int getWorkTicker() {
		return infuseTicker;
	}

}
