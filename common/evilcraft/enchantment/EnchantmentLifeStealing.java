package evilcraft.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import evilcraft.EvilCraft;
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
        super(eConfig, 1, EnumEnchantmentType.all);
    }
    
    public int getMinEnchantability(int par1) {
        return 20;
    }
    
    public int getMaxEnchantability(int par1) {
        return 50;
    }
    
    public int getMaxLevel() {
        return 3;
    }
    
    public static void stealLife(EntityPlayer player, float damage, int level) {
        player.heal(damage / EnchantmentLifeStealing._instance.getMaxLevel() * (level + 1));
    }

}
