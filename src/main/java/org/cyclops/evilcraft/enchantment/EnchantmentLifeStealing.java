package org.cyclops.evilcraft.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.cyclops.cyclopscore.config.configurable.ConfigurableEnchantment;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Enchantment that steals the HP when hit another Entity.
 * @author rubensworks
 *
 */
public class EnchantmentLifeStealing extends ConfigurableEnchantment {
    
    private static EnchantmentLifeStealing _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EnchantmentLifeStealing getInstance() {
        return _instance;
    }

    public EnchantmentLifeStealing(ExtendedConfig<EnchantmentConfig> eConfig) {
        super(eConfig, Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
    }
    
    @Override
    public int getMinEnchantability(int level) {
        return 15 + (level - 1) * 15;
    }
    
    @Override
    public int getMaxEnchantability(int level) {
        return super.getMinEnchantability(level) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    /**
     * Transfer the damage dealt as HP to the attacking entity.
     * @param entity The entity that attacked.
     * @param damage The damage that was dealt.
     * @param level The level of the enchant.
     */
    public static void stealLife(EntityLivingBase entity, float damage, int level) {
        entity.heal(damage / EnchantmentLifeStealing._instance.getMaxLevel()
        		* (level + 1) * (float) EnchantmentLifeStealingConfig.healModifier);
    }

}
