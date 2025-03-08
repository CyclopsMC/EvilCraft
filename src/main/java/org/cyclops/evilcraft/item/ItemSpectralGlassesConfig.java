package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Spectral Glasses.
 * @author rubensworks
 *
 */
public class ItemSpectralGlassesConfig extends ItemConfigCommon<IModBase> {

    public ItemSpectralGlassesConfig() {
        super(
                EvilCraft._instance,
            "spectral_glasses",
                (eConfig, properties) -> new ItemSpectralGlasses(properties)
        );
    }

}
