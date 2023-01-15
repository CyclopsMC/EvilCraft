package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Materialized Vengeance Essence.
 * @author rubensworks
 *
 */
public class ItemVengeanceEssenceMaterializedConfig extends ItemConfig {

    public ItemVengeanceEssenceMaterializedConfig() {
        super(
                EvilCraft._instance,
            "vengeance_essence_materialized",
                eConfig -> new Item(new Item.Properties()

                        .rarity(Rarity.EPIC))
        );
    }

}
