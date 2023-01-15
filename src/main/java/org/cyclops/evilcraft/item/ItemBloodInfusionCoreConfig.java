package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Infusion Core.
 * @author rubensworks
 *
 */
public class ItemBloodInfusionCoreConfig extends ItemConfig {

    public ItemBloodInfusionCoreConfig() {
        super(
                EvilCraft._instance,
            "blood_infusion_core",
                eConfig -> new Item(new Item.Properties()
                        )
        );
    }

}
