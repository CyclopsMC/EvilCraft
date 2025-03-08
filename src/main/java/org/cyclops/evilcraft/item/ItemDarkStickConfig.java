package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemDarkStick}.
 * @author rubensworks
 *
 */
public class ItemDarkStickConfig extends ItemConfigCommon<IModBase> {

    public ItemDarkStickConfig() {
        super(
                EvilCraft._instance,
                "dark_stick",
                (eConfig, properties) -> new ItemDarkStick(properties)
        );
    }

}
