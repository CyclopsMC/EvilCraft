package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Golden String.
 * @author rubensworks
 *
 */
public class ItemGoldenStringConfig extends ItemConfigCommon<IModBase> {

    public ItemGoldenStringConfig() {
        super(
                EvilCraft._instance,
            "golden_string",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
