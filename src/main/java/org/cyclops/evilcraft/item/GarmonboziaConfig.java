package org.cyclops.evilcraft.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Garmonbozia.
 * @author rubensworks
 *
 */
public class GarmonboziaConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static GarmonboziaConfig _instance;

    /**
     * Make a new instance.
     */
    public GarmonboziaConfig() {
        super(
                EvilCraft._instance,
            true,
            "garmonbozia",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this) {
            @Override
            public EnumRarity getRarity(ItemStack itemStack) {
                return EnumRarity.EPIC;
            }

            @Override
            public boolean hasEffect(ItemStack stack) {
                return true;
            }
        };
    }
}
