package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ItemConfig;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

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
                return EnumRarity.rare;
            }

            @Override
            public boolean hasEffect(ItemStack par1ItemStack, int pass) {
                return true;
            }
        };
    }
    
}
