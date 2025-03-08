package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Darkened Apple.
 * @author rubensworks
 *
 */
public class ItemDarkenedAppleConfig extends ItemConfigCommon<IModBase> {

    public ItemDarkenedAppleConfig() {
        super(
                EvilCraft._instance,
            "darkened_apple",
                (eConfig, properties) -> new ItemDarkenedApple(properties)
        );
    }

}
