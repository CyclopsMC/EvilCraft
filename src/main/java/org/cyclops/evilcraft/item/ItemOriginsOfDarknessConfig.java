package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Orb.
 * @author rubensworks
 *
 */
public class ItemOriginsOfDarknessConfig extends ItemConfig {

    public ItemOriginsOfDarknessConfig() {
        super(
                EvilCraft._instance,
            "origins_of_darkness",
                eConfig -> new ItemOriginsOfDarkness(new Item.Properties()

                        .stacksTo(1)
                        .rarity(Rarity.UNCOMMON))
        );
    }
}
