package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Spectral Glasses.
 * @author rubensworks
 *
 */
public class ItemSpectralGlassesConfig extends ItemConfig {

    public ItemSpectralGlassesConfig() {
        super(
                EvilCraft._instance,
            "spectral_glasses",
                eConfig -> new ItemSpectralGlasses(new Item.Properties()
                        )
        );
    }

}
