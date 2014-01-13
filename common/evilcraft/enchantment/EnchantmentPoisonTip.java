package evilcraft.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableEnchantment;

public class EnchantmentPoisonTip extends ConfigurableEnchantment {
    
    private static EnchantmentPoisonTip _instance = null;
    
    private static final int POISON_BASE_DURATION = 2;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new EnchantmentPoisonTip(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static EnchantmentPoisonTip getInstance() {
        return _instance;
    }

    private EnchantmentPoisonTip(ExtendedConfig eConfig) {
        super(eConfig, 1, EnumEnchantmentType.weapon);
    }
    
    public int getMinEnchantability(int par1) {
        return 20;
    }
    
    public int getMaxEnchantability(int par1) {
        return 50;
    }
    
    public int getMaxLevel() {
        return 1;
    }
    
    public static void poison(EntityLivingBase entity, int level) {
        entity.addPotionEffect(new PotionEffect(Potion.poison.id, POISON_BASE_DURATION * 20 * (level + 1), 1));
    }

}
