package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemVengeanceFocus}.
 * @author rubensworks
 *
 */
public class ItemVengeanceFocusConfig extends ItemConfig {

    public ItemVengeanceFocusConfig() {
        super(
                EvilCraft._instance,
                "vengeance_focus",
                eConfig -> new ItemVengeanceFocus(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

}
