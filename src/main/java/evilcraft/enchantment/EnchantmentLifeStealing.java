package evilcraft.enchantment;

import evilcraft.core.config.configurable.ConfigurableEnchantment;
import evilcraft.core.config.extendedconfig.EnchantmentConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;

/**
 * Enchantment that steals the HP when hit another Entity.
 * @author rubensworks
 *
 */
public class EnchantmentLifeStealing extends ConfigurableEnchantment {
    
    private static EnchantmentLifeStealing _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<EnchantmentConfig> eConfig) {
        if(_instance == null)
            _instance = new EnchantmentLifeStealing(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EnchantmentLifeStealing getInstance() {
        return _instance;
    }

    private EnchantmentLifeStealing(ExtendedConfig<EnchantmentConfig> eConfig) {
        super(eConfig, 3, EnumEnchantmentType.WEAPON);
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
