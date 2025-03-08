package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Dark Spike.
 * @author rubensworks
 *
 */
public class ItemDarkSpikeConfig extends ItemConfigCommon<IModBase> {

    public ItemDarkSpikeConfig() {
        super(
                EvilCraft._instance,
                "dark_spike",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
