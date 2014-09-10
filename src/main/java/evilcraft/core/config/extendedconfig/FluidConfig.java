package evilcraft.core.config.extendedconfig;

import evilcraft.core.config.ConfigurableType;
import net.minecraftforge.fluids.Fluid;

/**
 * Config for fluids.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class FluidConfig extends ExtendedConfig<FluidConfig> {

    /**
     * Make a new instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public FluidConfig(boolean enabled, String namedId,
            String comment, Class<? extends Fluid> element) {
        super(enabled, namedId, comment, element);
    }
    
    @Override
	public String getUnlocalizedName() {
		return "fluids." + getNamedId();
	}
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.FLUID;
	}
    
    @Override
    public boolean isDisableable() {
        return false;
    }

}
