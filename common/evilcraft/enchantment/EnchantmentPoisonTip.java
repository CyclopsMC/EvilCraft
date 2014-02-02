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
    
    @Override
    public int getMinEnchantability(int level) {
        return 10 + (level - 1) * 10;
    }
    
    @Override
    public int getMaxEnchantability(int level) {
        return super.getMinEnchantability(level) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    public static void poison(EntityLivingBase entity, int level) {
        entity.addPotionEffect(new PotionEffect(Potion.poison.id, POISON_BASE_DURATION * 20 * (level + 1), 1));
    }

}
