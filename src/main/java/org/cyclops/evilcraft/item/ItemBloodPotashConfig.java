package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemBloodPotash}.
 * @author rubensworks
 *
 */
public class ItemBloodPotashConfig extends ItemConfigCommon<IModBase> {

    public ItemBloodPotashConfig() {
        super(
                EvilCraft._instance,
            "blood_potash",
                (eConfig, properties) -> new ItemBloodPotash(properties)
        );
    }

}
