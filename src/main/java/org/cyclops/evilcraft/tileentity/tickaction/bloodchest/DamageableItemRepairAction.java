package org.cyclops.evilcraft.tileentity.tickaction.bloodchest;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableEnchantment;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;
import org.cyclops.evilcraft.block.BloodChestConfig;

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
    public static final LinkedList<ConfigurableEnchantment> BAD_ENCHANTS = new LinkedList<ConfigurableEnchantment>();
    
    @Override
    public boolean isItemValidForSlot(ItemStack itemStack) {
        return itemStack.getItem().isRepairable();
    }

    @Override
    public boolean canRepair(ItemStack itemStack, int tick) {
        return itemStack.isItemDamaged() && itemStack.getItem().isRepairable();
    }

    @Override
    public float repair(ItemStack itemStack, Random random, boolean doAction, boolean isBulk) {
        if(doAction) {
            // Repair the item
            int newDamage = itemStack.getItemDamage() - 1;
            itemStack.setItemDamage(newDamage);

            // Add bad enchant with a certain chance
            if (!isBulk && BloodChestConfig.addRandomBadEnchants && random.nextInt(CHANCE_RANDOM_ENCHANT) == 0
                    && BAD_ENCHANTS.size() > 0) {
                ConfigurableEnchantment enchantment = BAD_ENCHANTS.get(random.nextInt(BAD_ENCHANTS.size()));
                itemStack.addEnchantment(
                        enchantment,
                        enchantment.getMinLevel() + random.nextInt(
                                enchantment.getMaxLevel() - enchantment.getMinLevel())
                );
            }
        }
        return 1;
    }

}
