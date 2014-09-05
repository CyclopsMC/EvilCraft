package evilcraft.tileentity.tickaction.bloodchest;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;
import evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;

/**
 * Registry for {@link IBloodChestRepairAction} instances.
 * @author rubensworks
 *
 */
public class BloodChestRepairActionRegistry implements IBloodChestRepairActionRegistry {

    private final List<IBloodChestRepairAction> repairActions =
            new LinkedList<IBloodChestRepairAction>();
    
    /**
     * Make a new instance.
     */
    public BloodChestRepairActionRegistry() {
    	register(new DamageableItemRepairAction());
    }
    
    @Override
    public void register(IBloodChestRepairAction repairAction) {
        repairActions.add(repairAction);
    }

    @Override
    public boolean isItemValidForSlot(ItemStack itemStack) {
        for(IBloodChestRepairAction action : repairActions) {
            if(action.isItemValidForSlot(itemStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int canRepair(ItemStack itemStack, int tick) {
        for(int i = 0; i < repairActions.size(); i++) {
            if(repairActions.get(i).canRepair(itemStack, tick)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void repair(ItemStack itemStack, Random random, int actionID) {
        repairActions.get(actionID).repair(itemStack, random);
    }
    
}
