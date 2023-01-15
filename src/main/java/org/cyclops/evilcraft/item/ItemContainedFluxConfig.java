package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemContainedFlux}.
 * @author rubensworks
 *
 */
public class ItemContainedFluxConfig extends ItemConfig {

    public ItemContainedFluxConfig() {
        super(
                EvilCraft._instance,
            "contained_flux",
                eConfig -> new ItemContainedFlux(new Item.Properties()
                        )
        );
    }

}
