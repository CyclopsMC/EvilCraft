package evilcraft.entities.tileentities.tickaction.bloodchest;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;

/**
 * Registry for {@link IBloodChestRepairAction} instances.
 * @author rubensworks
 *
 */
public class BloodChestRepairActionRegistry {

    private static final List<IBloodChestRepairAction> REPAIR_ACTIONS =
            new LinkedList<IBloodChestRepairAction>();
    static {
        BloodChestRepairActionRegistry.register(new DamageableItemRepairAction());
    }
    
    /**
     * Register a new repair action.
     * @param repairAction The repair action instance.
     */
    public static void register(IBloodChestRepairAction repairAction) {
        REPAIR_ACTIONS.add(repairAction);
    }

    /**
     * Check if the given item can be inserted into the Blood Chest.
     * @param itemStack The {@link ItemStack} that could be inserted.
     * @return If the given item can be inserted into the given slot of the Blood Chest.
     */
    public static boolean isItemValidForSlot(ItemStack itemStack) {
        for(IBloodChestRepairAction action : REPAIR_ACTIONS) {
            if(action.isItemValidForSlot(itemStack)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a given item can be repaired.
     * @param itemStack The item that could be repaired.
     * @param tick The current tick inside the Blood Chest.
     * @return -1 if none of the actions apply, otherwise the actionID that applies.
     */
    public static int canRepair(ItemStack itemStack, int tick) {
        for(int i = 0; i < REPAIR_ACTIONS.size(); i++) {
            if(REPAIR_ACTIONS.get(i).canRepair(itemStack, tick)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * The repair logic for a given item for one tick.
     * @param itemStack The item to repair.
     * @param random A random instance.
     * @param actionID the ID of the action to call.
     */
    public static void repair(ItemStack itemStack, Random random, int actionID) {
        REPAIR_ACTIONS.get(actionID).repair(itemStack, random);
    }
    
}
