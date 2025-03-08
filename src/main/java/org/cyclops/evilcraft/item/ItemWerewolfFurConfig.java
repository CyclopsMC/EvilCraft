package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Werewolf Fur.
 * @author rubensworks
 *
 */
public class ItemWerewolfFurConfig extends ItemConfigCommon<IModBase> {

    public ItemWerewolfFurConfig() {
        super(
                EvilCraft._instance,
                "werewolf_fur",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
