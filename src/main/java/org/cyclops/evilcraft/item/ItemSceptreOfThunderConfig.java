package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemSceptreOfThunder}.
 * @author rubensworks
 *
 */
public class ItemSceptreOfThunderConfig extends ItemConfig {

    public ItemSceptreOfThunderConfig() {
        super(
                EvilCraft._instance,
            "sceptre_of_thunder",
                eConfig -> new ItemSceptreOfThunder(new Item.Properties()
                        )
        );
    }

}
