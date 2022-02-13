package org.cyclops.evilcraft.item;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for a blood bucket.
 * @author rubensworks
 */
public class ItemBucketBloodConfig extends ItemConfig {

    public ItemBucketBloodConfig() {
        super(
                EvilCraft._instance,
                "bucket_blood",
                eConfig -> new BucketItem(() -> RegistryEntries.FLUID_BLOOD, new Item.Properties()
                        .craftRemainder(Items.BUCKET)
                        .stacksTo(1)
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

}
