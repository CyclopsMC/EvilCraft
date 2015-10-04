package evilcraft.modcompat.tconstruct;

import java.util.Random;

import net.minecraft.item.ItemStack;
import tconstruct.library.tools.AbilityHelper;
import tconstruct.library.tools.ToolCore;
import evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;
import evilcraft.block.BloodChestConfig;

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
            ToolCore tool = (ToolCore) itemStack.getItem();
            return tool.getDamage(itemStack) > 0;
        }
        return false;
    }

    @Override
    public float repair(ItemStack itemStack, Random random, boolean doAction, boolean isBulk) {
        if(doAction) {
            AbilityHelper.healTool(itemStack, 1, null, true);
        }
        return 1;
    }

}
