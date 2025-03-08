package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemVengeanceRing}.
 * @author rubensworks
 *
 */
public class ItemVengeanceRingConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "The area of effect in # blocks of this ring.", isCommandable = true)
    public static int areaOfEffect = 10;

    public ItemVengeanceRingConfig() {
        super(
                EvilCraft._instance,
            "vengeance_ring",
                (eConfig, properties) -> new ItemVengeanceRing(properties
                        .stacksTo(1))
        );
    }

}
