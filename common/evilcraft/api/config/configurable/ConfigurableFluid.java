package evilcraft.api.config.configurable;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

/**
 * Fluid that can hold ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public abstract class ConfigurableFluid extends Fluid implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.FLUID;
    
    protected ConfigurableFluid(ExtendedConfig eConfig) {
        super(eConfig.NAME);
        //eConfig.ID = this.getID(); // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    // Set a configuration for this item
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    public String getUniqueName() {
        return "fluids."+eConfig.NAMEDID;
    }
    
    public boolean isEntity() {
        return false;
    }

}
