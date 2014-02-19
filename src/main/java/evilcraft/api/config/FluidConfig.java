package evilcraft.api.config;

import net.minecraftforge.fluids.Fluid;

/**
 * Config for fluids.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class FluidConfig extends ExtendedConfig<FluidConfig> {

    /**
     * Make a new instance.
     * @param defaultId The default ID for the configurable.
     * @param name The name for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public FluidConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Fluid> element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }

}
