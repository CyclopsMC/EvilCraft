package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import evilcraft.api.config.EnchantmentConfig;

/**
 * The action used for {@link EnchantmentConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class EnchantmentAction extends IElementTypeAction<EnchantmentConfig>{

    @Override
    public void preRun(EnchantmentConfig eConfig, Configuration config) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.NAMEDID, eConfig.ID);
        property.comment = eConfig.COMMENT;
        
        // Update the ID, it could've changed
        eConfig.ID = property.getInt();
    }

    @Override
    public void postRun(EnchantmentConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
    }

}
