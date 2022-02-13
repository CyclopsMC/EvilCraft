package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemInvertedPotentia}.
 * @author rubensworks
 *
 */
public class ItemInvertedPotentiaConfig extends ItemConfig {

    public ItemInvertedPotentiaConfig(boolean empowered) {
        super(
                EvilCraft._instance,
                "inverted_potentia" + (empowered ? "_empowered" : ""),
                eConfig -> new ItemInvertedPotentia(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup())
                        .stacksTo(16), empowered)
        );
    }
    
}
