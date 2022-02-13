package org.cyclops.evilcraft.tileentity.tickaction.bloodchest;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;
import org.cyclops.evilcraft.block.BlockBloodChestConfig;

import java.util.LinkedList;
import java.util.Random;

/**
 * Repair action for damageable items.
 * @author rubensworks
 *
 */
public class DamageableItemRepairAction implements IBloodChestRepairAction {

    private static final int CHANCE_RANDOM_ENCHANT = 10000;
    
    /**
     * All the possible bad enchantments
     */
    public static final LinkedList<Enchantment> BAD_ENCHANTS = new LinkedList<Enchantment>();
    
    @Override
    public boolean isItemValidForSlot(ItemStack itemStack) {
        return itemStack.isRepairable();
    }

    @Override
    public boolean canRepair(ItemStack itemStack, int tick) {
        return itemStack.isDamaged() && itemStack.isRepairable();
    }

    @Override
    public Pair<Float, ItemStack> repair(ItemStack itemStack, Random random, boolean doAction, boolean isBulk) {
        if(doAction) {
            // Repair the item
            int newDamage = itemStack.getDamageValue() - 1;
            itemStack.setDamageValue(newDamage);

            // Add bad enchant with a certain chance
            if (!isBulk && BlockBloodChestConfig.addRandomBadEnchants && random.nextInt(CHANCE_RANDOM_ENCHANT) == 0
                    && BAD_ENCHANTS.size() > 0) {
                Enchantment enchantment = BAD_ENCHANTS.get(random.nextInt(BAD_ENCHANTS.size()));
                itemStack.enchant(
                        enchantment,
                        enchantment.getMinLevel() + random.nextInt(
                                enchantment.getMaxLevel() - enchantment.getMinLevel())
                );
            }
        }
        return Pair.of(1F, itemStack);
    }

}
