package evilcraft.biomes;

import evilcraft.Reference;
import evilcraft.api.config.BiomeConfig;

/**
 * Config for {@link BiomeDegraded}.
 * @author rubensworks
 *
 */
public class BiomeDegradedConfig extends BiomeConfig {
    
    /**
     * The unique instance.
     */
    public static BiomeDegradedConfig _instance;

    /**
     * Make a new instance.
     */
    public BiomeDegradedConfig() {
        super(
            Reference.BIOME_DEGRADED,
            "Degraded",
            "biomeDegraded",
            null,
            BiomeDegraded.class
        );
    }
    
    @Override
    public void registerBiomeDictionary() {
        // Do not register this biome in the dictionary to avoid worldgen with this.
    }
    
}
