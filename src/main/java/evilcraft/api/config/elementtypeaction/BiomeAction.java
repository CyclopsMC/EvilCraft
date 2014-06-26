package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import evilcraft.api.config.BiomeConfig;

/**
 * The action used for {@link BiomeConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class BiomeAction extends IElementTypeAction<BiomeConfig>{

    @Override
    public void preRun(BiomeConfig eConfig, Configuration config) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.NAMEDID, eConfig.ID);
        property.comment = eConfig.COMMENT;
        
        // Update the ID, it could've changed
        eConfig.ID = property.getInt();
    }

    @Override
    public void postRun(BiomeConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register biome
        BiomeManager.addSpawnBiome(eConfig.getBiome());
        eConfig.registerBiomeDictionary();
    }

}
