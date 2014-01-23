package evilcraft.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRepairable extends Slot {

    public SlotRepairable(IInventory inventory, int index, int x,
            int y) {
        super(inventory, index, x, y);
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem().isRepairable();
    }
    
    public static boolean checkIsItemValid(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem().isRepairable();
    }

}
