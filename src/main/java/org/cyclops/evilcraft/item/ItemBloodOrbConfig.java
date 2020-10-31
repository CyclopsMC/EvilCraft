package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Orb.
 * @author rubensworks
 *
 */
public class ItemBloodOrbConfig extends ItemConfig {

    public ItemBloodOrbConfig(boolean filled) {
        super(
                EvilCraft._instance,
                "blood_orb_" + (filled ? "filled" : "empty"),
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }
    
}
