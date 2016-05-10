package org.cyclops.evilcraft.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableEnchantment;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.DamageableItemRepairAction;

import java.util.Random;

/**
 * Enchantment for letting tools break tools faster.
 * @author rubensworks
 *
 */
public class EnchantmentBreaking extends ConfigurableEnchantment {
    
    private static EnchantmentBreaking _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EnchantmentBreaking getInstance() {
        return _instance;
    }

    public EnchantmentBreaking(ExtendedConfig<EnchantmentConfig> eConfig) {
        super(eConfig, Rarity.COMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        DamageableItemRepairAction.BAD_ENCHANTS.add(this);
    }
    
    @Override
    public int getMinEnchantability(int level) {
        return 1 + (level - 1) * 8;
    }
    
    @Override
    public int getMaxEnchantability(int level) {
        return super.getMinEnchantability(level) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks()
    {
        return true;
    }
    
    /**
     * Used by ItemStack.attemptDamageItem. Randomly determines if a point of damage should be amplified using the
     * enchantment level. If the ItemStack is Armor then there is a flat 60% chance for damage to be amplified no
     * matter the enchantment level, otherwise there is a 1-(level/1) chance for damage to be amplified.
     * @param itemStack The ItemStack.
     * @param enchantmentListID Enchantments.
     * @param random A random object.
     */
    public static void amplifyDamage(ItemStack itemStack, int enchantmentListID, Random random) {
        if(enchantmentListID > -1) {
            int level = EnchantmentHelpers.getEnchantmentLevel(itemStack, enchantmentListID);
            int newDamage = itemStack.getItemDamage() + 2;
            if(itemStack.getItem() instanceof ItemArmor
                    && random.nextFloat() < 0.6F ? false : random.nextInt(level + 1) > 0
                    && newDamage <= itemStack.getMaxDamage()) {
                itemStack.setItemDamage(newDamage);
            }
        }
    }

}
