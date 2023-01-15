package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Werewolf Fur.
 * @author rubensworks
 *
 */
public class ItemWerewolfFurConfig extends ItemConfig {

    public ItemWerewolfFurConfig() {
        super(
                EvilCraft._instance,
                "werewolf_fur",
                eConfig -> new Item(new Item.Properties()
                        )
        );
    }

}
