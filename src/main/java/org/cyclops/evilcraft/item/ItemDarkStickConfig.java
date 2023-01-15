package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemDarkStick}.
 * @author rubensworks
 *
 */
public class ItemDarkStickConfig extends ItemConfig {

    public ItemDarkStickConfig() {
        super(
                EvilCraft._instance,
                "dark_stick",
                eConfig -> new ItemDarkStick(new Item.Properties()
                        )
        );
    }

}
