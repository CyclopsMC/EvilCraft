package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Dark Spike.
 * @author rubensworks
 *
 */
public class ItemDarkSpikeConfig extends ItemConfig {

    public ItemDarkSpikeConfig() {
        super(
                EvilCraft._instance,
                "dark_spike",
                eConfig -> new Item(new Item.Properties()
                        )
        );
    }

}
