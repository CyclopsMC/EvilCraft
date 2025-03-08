package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Materialized Vengeance Essence.
 * @author rubensworks
 *
 */
public class ItemVengeanceEssenceMaterializedConfig extends ItemConfigCommon<IModBase> {

    public ItemVengeanceEssenceMaterializedConfig() {
        super(
                EvilCraft._instance,
            "vengeance_essence_materialized",
                (eConfig, properties) -> new Item(properties
                        .rarity(Rarity.EPIC))
        );
    }

}
