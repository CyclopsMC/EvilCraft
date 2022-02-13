package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
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
                        .tab(EvilCraft._instance.getDefaultItemGroup())
                        .rarity(Rarity.EPIC))
        );
    }
    
}
