package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Ender Tear.
 * @author rubensworks
 *
 */
public class ItemEnderTearConfig extends ItemConfigCommon<IModBase> {

    public ItemEnderTearConfig() {
        super(
                EvilCraft._instance,
            "ender_tear",
                (eConfig, properties) -> new Item(properties
                        .stacksTo(16))
        );
    }
}
