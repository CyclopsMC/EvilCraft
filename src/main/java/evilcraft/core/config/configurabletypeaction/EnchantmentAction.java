package evilcraft.core.config.configurabletypeaction;

import evilcraft.core.config.extendedconfig.EnchantmentConfig;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * The action used for {@link EnchantmentConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class EnchantmentAction extends ConfigurableTypeAction<EnchantmentConfig>{

    @Override
    public void preRun(EnchantmentConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(), eConfig.ID);
        property.setRequiresMcRestart(true);
        property.comment = eConfig.getComment();
        
        if(startup) {
	        // Update the ID, it could've changed
	        eConfig.ID = property.getInt();
        }
    }

    @Override
    public void postRun(EnchantmentConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
    }

}
