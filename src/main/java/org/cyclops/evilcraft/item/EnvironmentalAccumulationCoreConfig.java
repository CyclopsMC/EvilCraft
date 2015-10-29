package org.cyclops.evilcraft.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Environmental Accumulation Core.
 * @author rubensworks
 *
 */
public class EnvironmentalAccumulationCoreConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static EnvironmentalAccumulationCoreConfig _instance;

    /**
     * Make a new instance.
     */
    public EnvironmentalAccumulationCoreConfig() {
        super(
                EvilCraft._instance,
            true,
            "environmentalAccumulationCore",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this) {
            @Override
            public EnumRarity getRarity(ItemStack itemStack) {
                return EnumRarity.RARE;
            }


            @Override
            public boolean hasEffect(ItemStack stack) {
                return true;
            }
        };
    }
    
}
