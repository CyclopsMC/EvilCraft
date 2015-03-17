package evilcraft.core.config.configurabletypeaction;

import evilcraft.core.config.extendedconfig.DummyConfig;
import net.minecraftforge.common.config.Configuration;

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
