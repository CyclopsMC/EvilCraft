package evilcraft.api.config.configurable;

import net.minecraftforge.fluids.Fluid;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

/**
 * Fluid that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableFluid extends Fluid implements Configurable{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.FLUID;
    
    /**
     * Make a new fluid instance.
     * @param eConfig Config for this block.
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableFluid(ExtendedConfig eConfig) {
        super(eConfig.NAMEDID);
        //eConfig.ID = this.getID(); // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getUniqueName() {
        return "fluids."+eConfig.NAMEDID;
    }
    
    @Override
    public boolean isEntity() {
        return false;
    }

}
