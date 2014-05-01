package evilcraft.api.config;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import evilcraft.api.config.configurable.ConfigurableBiome;

/**
 * Config for biomes.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BiomeConfig extends ExtendedConfig<BiomeConfig>{
	
	/**
	 * The ID.
	 */
	public int ID;

    /**
     * Make a new instance.
     * @param defaultId The default ID for the configurable.
     * @param name The name for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public BiomeConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends BiomeGenBase> element) {
        super(defaultId > 0, namedId, comment, element);
        this.ID = defaultId;
    }
    
    /**
     * Get the biome configurable
     * @return The biome.
     */
    public ConfigurableBiome getBiome() {
        return (ConfigurableBiome) this.getSubInstance();
    }
    
    /**
     * Register the biome instance into the biome dictionary.
     * @see BiomeDictionary
     */
    public void registerBiomeDictionary() {
        BiomeDictionary.makeBestGuess(getBiome());
    }

}
