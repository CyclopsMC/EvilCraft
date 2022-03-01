package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentVengeance}.
 * @author rubensworks
 *
 */
public class EnchantmentVengeanceConfig extends EnchantmentConfig {

    @ConfigurableProperty(category = "item", comment = "The area of effect in blocks in which this tool could enable vengeance spirits.", isCommandable = true)
    public static int areaOfEffect = 5;

    @ConfigurableProperty(category = "item", comment = "The ^-1 chance for which vengeance spirits could be toggled.", isCommandable = true)
    public static int vengeanceChance = 3;

    public EnchantmentVengeanceConfig() {
        super(
                EvilCraft._instance,
                "vengeance",
                eConfig -> new EnchantmentVengeance()
        );
    }

}
