package org.cyclops.evilcraft.modcompat.tconstruct;

import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;
import org.cyclops.evilcraft.block.BloodChestConfig;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.ToolHelper;

import java.util.Random;

/**
 * A Blood Chest repair action for Tinkers' Construct tools.
 * @author rubensworks
 *
 */
public class TConstructToolRepairTickAction implements IBloodChestRepairAction {

    @Override
    public boolean isItemValidForSlot(ItemStack itemStack) {
        return BloodChestConfig.repairTConstructTools && itemStack.getItem() instanceof ToolCore;
    }

    @Override
    public boolean canRepair(ItemStack itemStack, int tick) {
        if(isItemValidForSlot(itemStack)) {
            return itemStack.getItem().getDamage(itemStack) > 0;
        }
        return false;
    }

    @Override
    public float repair(ItemStack itemStack, Random random, boolean doAction, boolean isBulk) {
        if(doAction) {
            ToolHelper.healTool(itemStack, 1, null);
        }
        return 1;
    }

}
