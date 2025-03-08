package org.cyclops.evilcraft.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for a poison bucket.
 * @author rubensworks
 */
public class ItemBucketPoisonConfig extends ItemConfigCommon<IModBase> {

    public ItemBucketPoisonConfig() {
        super(
                EvilCraft._instance,
                "bucket_poison",
                (eConfig, properties) -> new BucketItem(RegistryEntries.FLUID_POISON.get(), properties
                        .craftRemainder(Items.BUCKET)
                        .stacksTo(1)
                        )
        );
    }

}
