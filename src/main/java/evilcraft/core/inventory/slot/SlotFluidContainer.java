package evilcraft.core.inventory.slot;

import evilcraft.core.fluid.SingleUseTank;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Slots that will accept buckets and {@link IFluidContainerItem}.
 * @author rubensworks
 *
 */
public class SlotFluidContainer extends Slot {
    
    private SingleUseTank tank = null;
    
    /**
     * Make a new instance that accepts containers for all fluids.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public SlotFluidContainer(IInventory inventory, int index, int x,
            int y) {
        super(inventory, index, x, y);
    }
    
    /**
     * Make a new instance that accepts containers for a given fluid.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param tank The accepting tank.
     */
    public SlotFluidContainer(IInventory inventory, int index, int x,
            int y, SingleUseTank tank) {
        this(inventory, index, x, y);
        this.tank = tank;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return checkIsItemValid(itemStack, tank);
    }
    
    /**
     * Check if the given item is valid and the fluid equals the fluid inside the
     * container (or the fluid in the container is null).
     * @param itemStack The item that will be checked.
     * @param tank The accepting tank.
     * @return If the given item is valid.
     */
    public static boolean checkIsItemValid(ItemStack itemStack, SingleUseTank tank) {
        if(itemStack != null
                && (itemStack.stackSize == 1 || FluidContainerRegistry.drainFluidContainer(itemStack) == null
                    || FluidContainerRegistry.drainFluidContainer(itemStack).stackSize == 0)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
            if(fluidStack == null && itemStack.getItem() instanceof IFluidContainerItem) {
            	IFluidContainerItem container = (IFluidContainerItem) itemStack.getItem();
            	fluidStack = container.getFluid(itemStack);
            }
            if(tank.getAcceptedFluid() != null && fluidStack != null) {
                return tank.canTankAccept(fluidStack.getFluid());
            }
        }
        return false;
    }
    
}
