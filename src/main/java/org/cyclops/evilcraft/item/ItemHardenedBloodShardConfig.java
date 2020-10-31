package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Hardened Blood Shard.
 * @author rubensworks
 *
 */
public class ItemHardenedBloodShardConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The minimum amount of shards from when using flint 'n steel on Hardened Blood.", isCommandable = true)
    public static int minimumDropped = 5;

    @ConfigurableProperty(category = "item", comment = "The additional random amount of shards from when using flint 'n steel on Hardened Blood.", isCommandable = true)
    public static int additionalDropped = 4;

    public ItemHardenedBloodShardConfig() {
        super(
                EvilCraft._instance,
            "hardened_blood_shard",
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

    /*
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_DYERED; TODO
    }*/
    
}
