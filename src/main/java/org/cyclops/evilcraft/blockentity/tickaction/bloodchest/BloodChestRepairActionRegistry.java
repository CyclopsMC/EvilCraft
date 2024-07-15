package org.cyclops.evilcraft.blockentity.tickaction.bloodchest;

import net.minecraft.core.HolderLookup;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import org.cyclops.evilcraft.block.BlockBloodChestConfig;

import java.util.LinkedList;
import java.util.List;

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
        register(new AnvilRepairAction());
    }

    @Override
    public void register(IBloodChestRepairAction repairAction) {
        repairActions.add(repairAction);
    }

    @Override
    public boolean isItemValidForSlot(ItemStack itemStack) {
        if(isNotBlacklisted(itemStack)) {
            for (IBloodChestRepairAction action : repairActions) {
                if (action.isItemValidForSlot(itemStack)) {
                    return true;
                }
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
    public Pair<Float, ItemStack> repair(ItemStack itemStack, RandomSource random, int actionID, boolean doAction, boolean isBulk, HolderLookup.Provider holderLookupProvider) {
        return repairActions.get(actionID).repair(itemStack, random, doAction, isBulk, holderLookupProvider);
    }

    protected boolean isNotBlacklisted(ItemStack itemStack) {
        if(itemStack.isEmpty()) return false;
        for(String name : BlockBloodChestConfig.itemBlacklist) {
            if(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString().matches(name)) {
                return false;
            }
        }
        return true;
    }

}
