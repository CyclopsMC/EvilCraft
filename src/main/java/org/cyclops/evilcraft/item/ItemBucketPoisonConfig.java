package org.cyclops.evilcraft.item;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for a poison bucket.
 * @author rubensworks
 */
public class ItemBucketPoisonConfig extends ItemConfig {

    public ItemBucketPoisonConfig() {
        super(
                EvilCraft._instance,
                "bucket_poison",
                eConfig -> new BucketItem(() -> RegistryEntries.FLUID_POISON, new Item.Properties()
                        .containerItem(Items.BUCKET)
                        .maxStackSize(1)
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

}
