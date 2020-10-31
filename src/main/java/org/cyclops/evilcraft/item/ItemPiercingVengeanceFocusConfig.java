package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemPiercingVengeanceFocus}.
 * @author rubensworks
 *
 */
public class ItemPiercingVengeanceFocusConfig extends ItemConfig {

    public ItemPiercingVengeanceFocusConfig() {
        super(
                EvilCraft._instance,
            "piercing_vengeance_focus",
                eConfig -> new ItemPiercingVengeanceFocus(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .rarity(Rarity.RARE))
        );
    }
    
}
