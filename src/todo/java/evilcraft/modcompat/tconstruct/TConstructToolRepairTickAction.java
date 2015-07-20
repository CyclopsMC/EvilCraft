package evilcraft.modcompat.tconstruct;

import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;
import org.cyclops.evilcraft.block.BloodChestConfig;
import net.minecraft.item.ItemStack;
import tconstruct.library.tools.AbilityHelper;
import tconstruct.library.tools.ToolCore;

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
            ToolCore tool = (ToolCore) itemStack.getItem();
            return tool.getDamage(itemStack) > 0;
        }
        return false;
    }

    @Override
    public void repair(ItemStack itemStack, Random random) {
        AbilityHelper.healTool(itemStack, 1, null, true);
    }

}
