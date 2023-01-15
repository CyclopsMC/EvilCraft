package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Hardened Blood Shard.
 * @author rubensworks
 *
 */
public class ItemHardenedBloodShardConfig extends ItemConfig {

    public ItemHardenedBloodShardConfig() {
        super(
                EvilCraft._instance,
            "hardened_blood_shard",
                eConfig -> new Item(new Item.Properties()
                        )
        );
    }

}
