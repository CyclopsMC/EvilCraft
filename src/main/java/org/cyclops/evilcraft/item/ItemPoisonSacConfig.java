package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Poison Sac.
 * @author rubensworks
 *
 */
public class ItemPoisonSacConfig extends ItemConfig {

    public ItemPoisonSacConfig() {
        super(
                EvilCraft._instance,
            "poison_sac",
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }
    
}
