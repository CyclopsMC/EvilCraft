package evilcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Helpers for items.
 * @author rubensworks
 *
 */
public class ItemHelpers {
	
	/**
     * Check if the given item is activated.
     * @param itemStack The item to check
     * @return If it is an active container.
     */
    public static boolean isActivated(ItemStack itemStack) {
        return itemStack != null && itemStack.getTagCompound() != null && itemStack.getTagCompound().getBoolean("enabled");
    }
    
    /**
     * Toggle activation for the given item.
     * @param itemStack The item to toggle.
     */
    public static void toggleActivation(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if(tag == null) {
            tag = new NBTTagCompound();
            itemStack.setTagCompound(tag);
        }
        tag.setBoolean("enabled", !isActivated(itemStack));
    }
	
}
