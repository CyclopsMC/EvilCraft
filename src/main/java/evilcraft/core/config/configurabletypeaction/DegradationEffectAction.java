package evilcraft.core.config.configurabletypeaction;

import evilcraft.api.RegistryManager;
import evilcraft.api.degradation.IDegradationRegistry;
import evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeAction;

/**
 * The action used for {@link DegradationEffectConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class DegradationEffectAction extends ConfigurableTypeAction<DegradationEffectConfig> {

    @Override
    public void preRun(DegradationEffectConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(), eConfig.isEnabled());
        property.setRequiresMcRestart(true);
        property.comment = eConfig.getComment();
        
        if(startup) {
	        // Update the ID, it could've changed
	        eConfig.setEnabled(property.getBoolean(false));
        }
    }

    @Override
    public void postRun(DegradationEffectConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register effect
        RegistryManager.getRegistry(IDegradationRegistry.class).registerDegradationEffect(
                eConfig.getNamedId(), eConfig.getDegradationEffect(), eConfig.getWeight());
    }

}
