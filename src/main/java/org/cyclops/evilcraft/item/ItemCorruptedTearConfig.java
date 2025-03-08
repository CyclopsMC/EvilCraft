package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Corrupted Tear.
 * @author rubensworks
 *
 */
public class ItemCorruptedTearConfig extends ItemConfigCommon<IModBase> {

    public ItemCorruptedTearConfig() {
        super(
                EvilCraft._instance,
            "corrupted_tear",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
