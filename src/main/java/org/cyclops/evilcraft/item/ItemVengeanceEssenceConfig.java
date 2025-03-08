package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Vengeance Essence.
 * @author rubensworks
 *
 */
public class ItemVengeanceEssenceConfig extends ItemConfigCommon<IModBase> {

    public ItemVengeanceEssenceConfig() {
        super(
                EvilCraft._instance,
            "vengeance_essence",
                (eConfig, properties) -> new Item(properties
                        .rarity(Rarity.EPIC))
        );
    }

}
