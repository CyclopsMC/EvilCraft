package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Vengeance Essence.
 * @author rubensworks
 *
 */
public class ItemVengeanceEssenceConfig extends ItemConfig {

    public ItemVengeanceEssenceConfig() {
        super(
                EvilCraft._instance,
            "vengeance_essence",
                eConfig -> new Item(new Item.Properties()

                        .rarity(Rarity.EPIC))
        );
    }

}
