package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import evilcraft.api.config.DegradationEffectConfig;
import evilcraft.api.degradation.DegradationRegistry;

/**
 * The action used for {@link DegradationEffectConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class DegradationEffectAction extends IElementTypeAction<DegradationEffectConfig>{

    @Override
    public void preRun(DegradationEffectConfig eConfig, Configuration config) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.NAMEDID, eConfig.isEnabled());
        property.comment = eConfig.COMMENT;
        
        // Update the ID, it could've changed
        eConfig.setEnabled(property.getBoolean(false));
    }

    @Override
    public void postRun(DegradationEffectConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register effect
        DegradationRegistry.registerDegradationEffect(
                eConfig.NAMEDID, eConfig.getDegradationEffect(), eConfig.getWeight());
    }

}
