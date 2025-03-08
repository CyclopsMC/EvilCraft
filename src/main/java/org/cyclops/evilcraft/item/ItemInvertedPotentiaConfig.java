package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemInvertedPotentia}.
 * @author rubensworks
 *
 */
public class ItemInvertedPotentiaConfig extends ItemConfigCommon<IModBase> {

    public ItemInvertedPotentiaConfig(boolean empowered) {
        super(
                EvilCraft._instance,
                "inverted_potentia" + (empowered ? "_empowered" : ""),
                (eConfig, properties) -> new ItemInvertedPotentia(properties
                        .stacksTo(16), empowered)
        );
    }

}
