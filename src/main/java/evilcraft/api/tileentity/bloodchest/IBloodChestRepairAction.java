package evilcraft.api.tileentity.bloodchest;

import java.util.Random;

import net.minecraft.item.ItemStack;

/**
 * Actions that can be registered in ...
 * @author rubensworks
 *
 */
public interface IBloodChestRepairAction {
    
    /**
     * Check if the given item can be inserted into the Blood Chest.
     * @param itemStack The {@link ItemStack} that could be inserted.
     * @return If the given item can be inserted into the given slot of the Blood Chest.
     */
    public boolean isItemValidForSlot(ItemStack itemStack);

    /**
     * Check if a given item can be repaired.
     * @param itemStack The item that could be repaired.
     * @param tick The current tick inside the Blood Chest.
     * @return If the given item can be repaired.
     */
    public boolean canRepair(ItemStack itemStack, int tick);
    
    /**
     * The repair logic for a given item for one tick.
     * @param itemStack The item to repair.
     * @param random A random instance.
     * @return Blood usage multiplier
     */
    public float repair(ItemStack itemStack, Random random);
    
}
