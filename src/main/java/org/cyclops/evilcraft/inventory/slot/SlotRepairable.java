package org.cyclops.evilcraft.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;

/**
 * A slot that only accepts repairable items.
 * @author rubensworks
 *
 */
public class SlotRepairable extends Slot {

    /**
     * Make a new instance.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public SlotRepairable(Container inventory, int index, int x,
            int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return checkIsItemValid(itemStack);
    }

    /**
     * Check if the given item is valid for this slot type.
     * @param itemStack The item that will be checked.
     * @return If the given item is valid.
     */
    public static boolean checkIsItemValid(ItemStack itemStack) {
        return !itemStack.isEmpty() && EvilCraft._instance.getRegistryManager().getRegistry(IBloodChestRepairActionRegistry.class).
                isItemValidForSlot(itemStack);
    }

}
