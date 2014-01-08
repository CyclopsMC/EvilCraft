package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.Configuration;
import evilcraft.api.config.DummyConfig;
import evilcraft.api.config.ExtendedConfig;

/**
 * Just a dummy action.
 * @author Ruben Taelman
 *
 */
public class DummyAction extends IElementTypeAction<DummyConfig> {

    @Override
    public void preRun(DummyConfig eConfig, Configuration config) {
        
    }

    @Override
    public void postRun(DummyConfig eConfig, Configuration config) {
        
    }

}
