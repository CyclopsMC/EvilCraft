package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Dull Dust.
 * @author rubensworks
 *
 */
public class ItemDullDustConfig extends ItemConfig {

    public ItemDullDustConfig() {
        super(
                EvilCraft._instance,
            "dull_dust",
                eConfig -> new Item(new Item.Properties()
                        )
        );
    }

}
