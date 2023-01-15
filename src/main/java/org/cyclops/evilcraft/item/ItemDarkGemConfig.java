package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemDarkGem}.
 * @author rubensworks
 *
 */
public class ItemDarkGemConfig extends ItemConfig {

    public ItemDarkGemConfig() {
        super(
                EvilCraft._instance,
                "dark_gem",
                eConfig -> new ItemDarkGem(new Item.Properties()
                        )
        );
    }

}
