package evilcraft.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import evilcraft.api.config.EnchantmentConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableEnchantment;

/**
 * Enchantment that poisons the attacked entity.
 * @author rubensworks
 *
 */
public class EnchantmentPoisonTip extends ConfigurableEnchantment {
    
    private static EnchantmentPoisonTip _instance = null;
    
    private static final int POISON_BASE_DURATION = 2;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<EnchantmentConfig> eConfig) {
        if(_instance == null)
            _instance = new EnchantmentPoisonTip(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EnchantmentPoisonTip getInstance() {
        return _instance;
    }

    private EnchantmentPoisonTip(ExtendedConfig<EnchantmentConfig> eConfig) {
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
    
    /**
     * Poison the attacked entity according to the level of the enchant.
     * @param entity The entity was attacked.
     * @param level The level of the enchant.
     */
    public static void poison(EntityLivingBase entity, int level) {
        entity.addPotionEffect(new PotionEffect(Potion.poison.id, POISON_BASE_DURATION * 20 * (level + 1), 1));
    }

}
