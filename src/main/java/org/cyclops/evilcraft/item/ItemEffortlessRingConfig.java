package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Effortless Ring.
 * @author rubensworks
 *
 */
public class ItemEffortlessRingConfig extends ItemConfig {

    public ItemEffortlessRingConfig() {
        super(
                EvilCraft._instance,
            "effortless_ring",
                eConfig -> new ItemEffortlessRing(new Item.Properties()

                        .stacksTo(1)
                        .rarity(Rarity.UNCOMMON))
        );
    }

}
