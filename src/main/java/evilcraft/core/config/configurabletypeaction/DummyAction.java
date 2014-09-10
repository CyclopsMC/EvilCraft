package evilcraft.core.config.configurabletypeaction;

import net.minecraftforge.common.config.Configuration;
import evilcraft.core.config.extendedconfig.DummyConfig;

/**
 * Just a dummy action.
 * @author rubensworks
 *
 */
public class DummyAction extends ConfigurableTypeAction<DummyConfig> {

    @Override
    public void preRun(DummyConfig eConfig, Configuration config, boolean startup) {
        
    }

    @Override
    public void postRun(DummyConfig eConfig, Configuration config) {
        
    }

}
