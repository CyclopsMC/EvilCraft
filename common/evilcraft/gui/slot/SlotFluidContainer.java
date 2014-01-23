package evilcraft.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class SlotFluidContainer extends Slot {
    
    private Fluid acceptedFluid = null;
    
    public SlotFluidContainer(IInventory inventory, int index, int x,
            int y) {
        super(inventory, index, x, y);
    }
    
    public SlotFluidContainer(IInventory inventory, int index, int x,
            int y, Fluid acceptedFluid) {
        this(inventory, index, x, y);
        this.acceptedFluid = acceptedFluid;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return checkIsItemValid(itemStack, acceptedFluid);
    }
    
    public static boolean checkIsItemValid(ItemStack itemStack, Fluid acceptedFluid) {
        if(itemStack != null && itemStack.stackSize == 1) {
            // This is done because the FluidContainerRegistry saves keys on item id and damage value,
            // but our containers can have different damage values for the same container.
            ItemStack itemStackEmptyCopy = itemStack.copy();
            itemStackEmptyCopy.setItemDamage(0);
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStackEmptyCopy);
            if(itemStack.getItem() instanceof ItemBucket) {
                if(itemStack.getItem().itemID != Item.bucketEmpty.itemID) {
                    if(acceptedFluid != null) {
                        if(fluidStack != null) {
                            return acceptedFluid.equals(fluidStack.getFluid());
                        } else {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            } else if(itemStack.getItem() instanceof IFluidContainerItem) {
                if(acceptedFluid != null) {
                    if(fluidStack != null) {
                        return acceptedFluid.equals(fluidStack.getFluid());   
                    } else {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
}
