package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemSpikeyClaws}.
 * @author rubensworks
 *
 */
public class ItemSpikeyClawsConfig extends ItemConfigCommon<IModBase> {

    public ItemSpikeyClawsConfig() {
        super(
                EvilCraft._instance,
                "spikey_claws",
                (eConfig, properties) -> new ItemSpikeyClaws(properties
                        .durability(256))
        );
    }

}
