package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import evilcraft.api.config.ExtendedConfig;

public class EnchantmentAction extends IElementTypeAction{

    @Override
    public void run(ExtendedConfig eConfig, Configuration config) {
        // Get property in config file and set comment
        Property property = config.get(CATEGORIES.get(eConfig.getHolderType()), eConfig.NAME, eConfig.ID);
        property.comment = eConfig.COMMENT;
        
        // Update the ID, it could've changed
        eConfig.ID = property.getInt();
        
        // Save the config inside the correct element
        eConfig.save();
    }

}
