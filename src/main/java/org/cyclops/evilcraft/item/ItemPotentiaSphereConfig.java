package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Potentia Sphere.
 * @author rubensworks
 *
 */
public class ItemPotentiaSphereConfig extends ItemConfigCommon<IModBase> {

    public ItemPotentiaSphereConfig() {
        super(
                EvilCraft._instance,
            "potentia_sphere",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
