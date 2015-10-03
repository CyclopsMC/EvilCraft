package evilcraft.tileentity.tickaction.bloodchest;

import java.util.LinkedList;
import java.util.Random;

import net.minecraft.item.ItemStack;
import evilcraft.Configs;
import evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;
import evilcraft.block.BloodChestConfig;
import evilcraft.core.config.configurable.ConfigurableEnchantment;
import evilcraft.enchantment.EnchantmentBreaking;
import evilcraft.enchantment.EnchantmentBreakingConfig;

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
    public float repair(ItemStack itemStack, Random random, boolean doAction) {
        if(doAction) {
            // Repair the item
            int newDamage = itemStack.getItemDamage() - 1;
            itemStack.setItemDamage(newDamage);

            // Add bad enchant with a certain chance
            if (BloodChestConfig.addRandomBadEnchants && random.nextInt(CHANCE_RANDOM_ENCHANT) == 0
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
