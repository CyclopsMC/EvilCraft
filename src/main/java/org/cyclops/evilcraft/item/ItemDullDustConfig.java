package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Dull Dust.
 * @author rubensworks
 *
 */
public class ItemDullDustConfig extends ItemConfigCommon<IModBase> {

    public ItemDullDustConfig() {
        super(
                EvilCraft._instance,
            "dull_dust",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
