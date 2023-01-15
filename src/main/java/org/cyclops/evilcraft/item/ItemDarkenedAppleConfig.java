package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Darkened Apple.
 * @author rubensworks
 *
 */
public class ItemDarkenedAppleConfig extends ItemConfig {

    public ItemDarkenedAppleConfig() {
        super(
                EvilCraft._instance,
            "darkened_apple",
                eConfig -> new ItemDarkenedApple(new Item.Properties()
                        )
        );
    }

}
