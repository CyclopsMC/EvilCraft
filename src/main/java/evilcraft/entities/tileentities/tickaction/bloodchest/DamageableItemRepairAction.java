package evilcraft.entities.tileentities.tickaction.bloodchest;

import java.util.LinkedList;
import java.util.Random;

import evilcraft.Configs;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.core.config.configurable.ConfigurableEnchantment;
import evilcraft.enchantment.EnchantmentBreaking;
import evilcraft.enchantment.EnchantmentBreakingConfig;

import net.minecraft.item.ItemStack;

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
    static {
        if(Configs.isEnabled(EnchantmentBreakingConfig.class)) {
            BAD_ENCHANTS.add(EnchantmentBreaking.getInstance());
        }
    }
    
    @Override
    public boolean isItemValidForSlot(ItemStack itemStack) {
        return itemStack.getItem().isRepairable();
    }

    @Override
    public boolean canRepair(ItemStack itemStack, int tick) {
        return itemStack.isItemDamaged() && itemStack.getItem().isRepairable();
    }

    @Override
    public void repair(ItemStack itemStack, Random random) {
        // Repair the item
        int newDamage = itemStack.getItemDamage() - 1;
        itemStack.setItemDamage(newDamage);
        
        // Add bad enchant with a certain chance
        if(BloodChestConfig.addRandomBadEnchants && random.nextInt(CHANCE_RANDOM_ENCHANT) == 0) {
            ConfigurableEnchantment enchantment = BAD_ENCHANTS.get(random.nextInt(BAD_ENCHANTS.size()));
            itemStack.addEnchantment(
                    enchantment,
                    enchantment.getMinLevel() + random.nextInt(
                            enchantment.getMaxLevel() - enchantment.getMinLevel())
                    );
        }
    }

}
