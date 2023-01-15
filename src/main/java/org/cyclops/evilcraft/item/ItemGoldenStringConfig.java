package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Golden String.
 * @author rubensworks
 *
 */
public class ItemGoldenStringConfig extends ItemConfig {

    public ItemGoldenStringConfig() {
        super(
                EvilCraft._instance,
            "golden_string",
                eConfig -> new Item(new Item.Properties()
                        )
        );
    }

}
