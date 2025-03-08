package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemVengeanceFocus}.
 * @author rubensworks
 *
 */
public class ItemVengeanceFocusConfig extends ItemConfigCommon<IModBase> {

    public ItemVengeanceFocusConfig() {
        super(
                EvilCraft._instance,
                "vengeance_focus",
                (eConfig, properties) -> new ItemVengeanceFocus(properties)
        );
    }

}
