package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Corrupted Tear.
 * @author rubensworks
 *
 */
public class ItemCorruptedTearConfig extends ItemConfig {

    public ItemCorruptedTearConfig() {
        super(
                EvilCraft._instance,
            "corrupted_tear",
                eConfig -> new Item(new Item.Properties()
                        )
        );
    }

}
