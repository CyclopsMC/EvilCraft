package org.cyclops.evilcraft.modcompat.thaumcraft;

import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Blood Infused Golden Wand cap that has a slightly higher discount.
 * Textures are based on the ones from Thaumcraft.
 * @author rubensworks
 *
 */
public class BloodWandCapConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BloodWandCapConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodWandCapConfig() {
        super(
            EvilCraft._instance,
        	true,
            "bloodWandCap",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this);
    }
    
}
