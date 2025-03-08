package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemLightningGrenade}.
 * @author rubensworks
 *
 */
public class ItemLightningGrenadeConfig extends ItemConfigCommon<IModBase> {

    public ItemLightningGrenadeConfig() {
        super(
                EvilCraft._instance,
                "lightning_grenade",
                (eConfig, properties) -> new ItemLightningGrenade(properties
                        .stacksTo(16))
        );
    }

}
