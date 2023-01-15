package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Eternal Water Bucket.
 * @author rubensworks
 *
 */
public class ItemBucketEternalWaterConfig extends ItemConfig {

    public ItemBucketEternalWaterConfig() {
        super(
                EvilCraft._instance,
            "bucket_eternal_water",
                eConfig -> new ItemBucketEternalWater(new Item.Properties()
                        )
        );
    }

}
