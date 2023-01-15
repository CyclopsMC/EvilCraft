package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemBloodPotash}.
 * @author rubensworks
 *
 */
public class ItemBloodPotashConfig extends ItemConfig {

    public ItemBloodPotashConfig() {
        super(
                EvilCraft._instance,
            "blood_potash",
                eConfig -> new ItemBloodPotash(new Item.Properties()
                        )
        );
    }

}
