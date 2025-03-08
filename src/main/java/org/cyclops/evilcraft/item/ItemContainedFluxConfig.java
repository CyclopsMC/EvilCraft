package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemContainedFlux}.
 * @author rubensworks
 *
 */
public class ItemContainedFluxConfig extends ItemConfigCommon<IModBase> {

    public ItemContainedFluxConfig() {
        super(
                EvilCraft._instance,
            "contained_flux",
                (eConfig, properties) -> new ItemContainedFlux(properties)
        );
    }

}
