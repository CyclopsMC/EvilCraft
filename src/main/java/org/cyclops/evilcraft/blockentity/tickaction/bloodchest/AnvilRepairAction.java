package org.cyclops.evilcraft.blockentity.tickaction.bloodchest;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AnvilBlock;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;

/**
 * Repair action for anvils.
 * @author rubensworks
 *
 */
public class AnvilRepairAction implements IBloodChestRepairAction {

    @Override
    public boolean isItemValidForSlot(ItemStack itemStack) {
        return itemStack.getItem() instanceof BlockItem && ((BlockItem) itemStack.getItem()).getBlock() instanceof AnvilBlock;
    }

    @Override
    public boolean canRepair(ItemStack itemStack, int tick) {
        return itemStack.getItem() == Items.CHIPPED_ANVIL || itemStack.getItem() == Items.DAMAGED_ANVIL;
    }

    @Override
    public Pair<Float, ItemStack> repair(ItemStack itemStack, RandomSource random, boolean doAction, boolean isBulk) {
        return Pair.of(25F, new ItemStack(itemStack.getItem() == Items.CHIPPED_ANVIL ? Items.ANVIL : Items.CHIPPED_ANVIL));
    }

}
