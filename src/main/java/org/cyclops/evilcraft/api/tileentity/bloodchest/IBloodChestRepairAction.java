package org.cyclops.evilcraft.api.tileentity.bloodchest;

import net.minecraft.core.HolderLookup;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Actions that can be registered in the {@link IBloodChestRepairActionRegistry}.
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
     *
     * @param itemStack            The item to repair.
     * @param random               A random instance.
     * @param doAction             If the actual repair action should be performed, otherwise this is just a simulation.
     * @param isBulk               If the repairing container repairs things in bulk.
     * @param holderLookupProvider
     * @return Pair of blood usage multiplier and new itemstack.
     */
    public Pair<Float, ItemStack> repair(ItemStack itemStack, RandomSource random, boolean doAction, boolean isBulk, HolderLookup.Provider holderLookupProvider);

}
