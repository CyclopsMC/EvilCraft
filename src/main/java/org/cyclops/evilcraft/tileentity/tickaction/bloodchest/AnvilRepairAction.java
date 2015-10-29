package org.cyclops.evilcraft.tileentity.tickaction.bloodchest;

import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;

import java.util.Random;

/**
 * Repair action for anvils.
 * @author rubensworks
 *
 */
public class AnvilRepairAction implements IBloodChestRepairAction {
    
    @Override
    public boolean isItemValidForSlot(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemAnvilBlock;
    }

    @Override
    public boolean canRepair(ItemStack itemStack, int tick) {
        return itemStack.getItemDamage() > 0;
    }

    @Override
    public float repair(ItemStack itemStack, Random random, boolean doAction, boolean isBulk) {
        if(doAction) {
            // Repair the item
            int newDamage = itemStack.getItemDamage() - 1;
            itemStack.setItemDamage(newDamage);
        }
        return 25;
    }

}
