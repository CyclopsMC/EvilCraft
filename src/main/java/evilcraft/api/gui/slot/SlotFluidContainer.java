package evilcraft.api.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Slots that will accept buckets and {@link IFluidContainerItem}.
 * @author rubensworks
 *
 */
public class SlotFluidContainer extends Slot {
    
    private Fluid acceptedFluid = null;
    
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
     * @param acceptedFluid The accepted fluid.
     */
    public SlotFluidContainer(IInventory inventory, int index, int x,
            int y, Fluid acceptedFluid) {
        this(inventory, index, x, y);
        this.acceptedFluid = acceptedFluid;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return checkIsItemValid(itemStack, acceptedFluid);
    }
    
    /**
     * Check if the given item is valid and the fluid equals the fluid inside the
     * container (or the fluid in the container is null).
     * @param itemStack The item that will be checked.
     * @param acceptedFluid The fluid that must be matched.
     * @return If the given item is valid.
     */
    public static boolean checkIsItemValid(ItemStack itemStack, Fluid acceptedFluid) {
        if(itemStack != null && itemStack.stackSize == 1) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
            if(acceptedFluid != null && fluidStack != null) {
                return acceptedFluid.equals(fluidStack.getFluid());
            }
        }
        return false;
    }
    
}
