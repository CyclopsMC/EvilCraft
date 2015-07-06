package evilcraft.core.config.configurabletypeaction;

import evilcraft.core.config.extendedconfig.BiomeConfig;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * The action used for {@link BiomeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BiomeAction extends ConfigurableTypeAction<BiomeConfig>{

    @Override
    public void preRun(BiomeConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(), eConfig.getId());
        property.setRequiresWorldRestart(true);
        property.setRequiresMcRestart(true);
        property.comment = eConfig.getComment();
        
        if(startup) {
        	// Update the ID, it could've changed
        	eConfig.setId(property.getInt());
        }
    }

    @Override
    public void postRun(BiomeConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register biome
        //BiomeManager.addSpawnBiome(eConfig.getBiome());
        eConfig.registerBiomeDictionary();
    }

}
