package org.cyclops.evilcraft.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for a blood bucket.
 * @author rubensworks
 */
public class ItemBucketBloodConfig extends ItemConfigCommon<IModBase> {

    public ItemBucketBloodConfig() {
        super(
                EvilCraft._instance,
                "bucket_blood",
                (eConfig, properties) -> new BucketItem(RegistryEntries.FLUID_BLOOD.get(), properties
                        .craftRemainder(Items.BUCKET)
                        .stacksTo(1)
                        )
        );
    }

}
