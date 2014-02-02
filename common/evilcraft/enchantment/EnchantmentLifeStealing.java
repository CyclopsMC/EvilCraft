package evilcraft.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableEnchantment;

public class EnchantmentLifeStealing extends ConfigurableEnchantment {
    
    private static EnchantmentLifeStealing _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new EnchantmentLifeStealing(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static EnchantmentLifeStealing getInstance() {
        return _instance;
    }

    private EnchantmentLifeStealing(ExtendedConfig eConfig) {
        super(eConfig, 3, EnumEnchantmentType.weapon);
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
    
    public static void stealLife(EntityLivingBase entity, float damage, int level) {
        entity.heal(damage / EnchantmentLifeStealing._instance.getMaxLevel() * (level + 1));
    }

}
