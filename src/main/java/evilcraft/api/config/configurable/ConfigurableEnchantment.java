package evilcraft.api.config.configurable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.EnchantmentConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.helpers.L10NHelpers;

/**
 * A simple configurable for Enchantments, will auto-register itself after construction.
 * @author rubensworks
 *
 */
public class ConfigurableEnchantment extends Enchantment implements Configurable {

    protected ExtendedConfig<EnchantmentConfig> eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.ENCHANTMENT;
    
    /**
     * Make a new Enchantment instance
     * @param eConfig Config for this enchantment.
     * @param weight The weight in which this enchantment should occurd
     * @param type The type of enchantment
     */
    protected ConfigurableEnchantment(ExtendedConfig<EnchantmentConfig> eConfig, int weight,
            EnumEnchantmentType type) {
        super(eConfig.downCast().ID, weight, type);
        this.setConfig(eConfig);
        this.setName(this.getUniqueName());
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getUniqueName() {
        return "enchantments."+eConfig.NAMEDID;
    }
    
    @Override
    public boolean isEntity() {
        return false;
    }
    
    @Override
    public String getTranslatedName(int level) {
        String enchantmentName = L10NHelpers.localize("enchantment." + eConfig.downCast().NAMEDID);
        return enchantmentName + " " + L10NHelpers.localize("enchantment.level." + level);
    }

}
