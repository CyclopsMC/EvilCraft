package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemSceptreOfThunder}.
 * @author rubensworks
 *
 */
public class ItemSceptreOfThunderConfig extends ItemConfigCommon<IModBase> {

    public ItemSceptreOfThunderConfig() {
        super(
                EvilCraft._instance,
            "sceptre_of_thunder",
                (eConfig, properties) -> new ItemSceptreOfThunder(properties)
        );
    }

}
