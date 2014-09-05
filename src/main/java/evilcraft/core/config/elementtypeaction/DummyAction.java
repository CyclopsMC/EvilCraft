package evilcraft.core.config.elementtypeaction;

import net.minecraftforge.common.config.Configuration;
import evilcraft.core.config.DummyConfig;

/**
 * Just a dummy action.
 * @author rubensworks
 *
 */
public class DummyAction extends IElementTypeAction<DummyConfig> {

    @Override
    public void preRun(DummyConfig eConfig, Configuration config, boolean startup) {
        
    }

    @Override
    public void postRun(DummyConfig eConfig, Configuration config) {
        
    }

}
