package evilcraft.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class SlotFluidContainer extends Slot {
    
    public SlotFluidContainer(IInventory inventory, int index, int x,
            int y) {
        super(inventory, index, x, y);
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return checkIsItemValid(itemStack);
    }
    
    public static boolean checkIsItemValid(ItemStack itemStack) {
        return itemStack != null
                && (itemStack.getItem() instanceof ItemBucket
                || itemStack.getItem() instanceof IFluidContainerItem)
                && itemStack.getItem().itemID != Item.bucketEmpty.itemID
                && itemStack.stackSize == 1;
    }
    
}
