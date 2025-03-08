package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Hardened Blood Shard.
 * @author rubensworks
 *
 */
public class ItemHardenedBloodShardConfig extends ItemConfigCommon<IModBase> {

    public ItemHardenedBloodShardConfig() {
        super(
                EvilCraft._instance,
            "hardened_blood_shard",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
