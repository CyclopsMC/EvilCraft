package evilcraft.core.config.configurable;

import net.minecraftforge.fluids.Fluid;
import evilcraft.core.config.extendedconfig.ExtendedConfig;

/**
 * Fluid that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableFluid extends Fluid implements IConfigurable{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * Make a new fluid instance.
     * @param eConfig Config for this block.
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableFluid(ExtendedConfig eConfig) {
        super(eConfig.getNamedId());
        //eConfig.ID = this.getID(); // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

}
