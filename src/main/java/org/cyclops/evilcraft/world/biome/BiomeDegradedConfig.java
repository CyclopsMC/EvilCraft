package org.cyclops.evilcraft.world.biome;

import org.cyclops.cyclopscore.config.extendedconfig.BiomeConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for {@link BiomeDegraded}.
 * @author rubensworks
 *
 */
public class BiomeDegradedConfig extends BiomeConfig {

    public BiomeDegradedConfig() {
        super(
                EvilCraft._instance,
                "degraded",
                eConfig -> new BiomeDegraded()
        );
    }
    
}
