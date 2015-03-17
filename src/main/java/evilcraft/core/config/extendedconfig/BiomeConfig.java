package evilcraft.core.config.extendedconfig;

import evilcraft.core.config.ConfigurableType;
import evilcraft.core.config.configurable.ConfigurableBiome;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

/**
 * Config for biomes.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BiomeConfig extends ExtendedConfig<BiomeConfig>{
	
	private int id;

    /**
     * Make a new instance.
     * @param defaultId The default ID for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public BiomeConfig(int defaultId, String namedId,
            String comment, Class<? extends BiomeGenBase> element) {
        super(defaultId > 0, namedId, comment, element);
        this.id = defaultId;
    }
    
    /**
     * @return The ID.
     */
    public int getId() {
		return id;
	}
    
    /**
     * Set the ID.
     * @param id The new ID.
     */
    public void setId(int id) {
    	this.id = id;
    }
    
    @Override
	public String getUnlocalizedName() {
		return "biomes." + getNamedId();
	}
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.BIOME;
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
    
    @Override
    public boolean isEnabled() {
    	return this.getId() != 0;
    }

}
