package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Infusion Core.
 * @author rubensworks
 *
 */
public class ItemBloodInfusionCoreConfig extends ItemConfigCommon<IModBase> {

    public ItemBloodInfusionCoreConfig() {
        super(
                EvilCraft._instance,
            "blood_infusion_core",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
