package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood-Waxed Coal.
 * @author rubensworks
 *
 */
public class BloodWaxedCoalConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BloodWaxedCoalConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodWaxedCoalConfig() {
        super(
                EvilCraft._instance,
        	true,
            "blood_waxed_coal",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this) {
            @Override
            public int getItemBurnTime(ItemStack itemStack) {
                return 3200;
            }
        };
    }

}
