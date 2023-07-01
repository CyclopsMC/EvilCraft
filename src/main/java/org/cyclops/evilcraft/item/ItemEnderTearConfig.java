package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Ender Tear.
 * @author rubensworks
 *
 */
public class ItemEnderTearConfig extends ItemConfig {

    public ItemEnderTearConfig() {
        super(
                EvilCraft._instance,
            "ender_tear",
                eConfig -> new Item(new Item.Properties()

                        .stacksTo(16))
        );
    }
}
