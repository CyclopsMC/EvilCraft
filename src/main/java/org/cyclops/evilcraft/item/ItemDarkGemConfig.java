package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemDarkGem}.
 * @author rubensworks
 *
 */
public class ItemDarkGemConfig extends ItemConfigCommon<IModBase> {

    public ItemDarkGemConfig() {
        super(
                EvilCraft._instance,
                "dark_gem",
                (eConfig, properties) -> new ItemDarkGem(properties)
        );
    }

}
