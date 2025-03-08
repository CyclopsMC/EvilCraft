package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Poison Sac.
 * @author rubensworks
 *
 */
public class ItemPoisonSacConfig extends ItemConfigCommon<IModBase> {

    public ItemPoisonSacConfig() {
        super(
                EvilCraft._instance,
            "poison_sac",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
