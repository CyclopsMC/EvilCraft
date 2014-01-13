package evilcraft.api.config;

import net.minecraftforge.fluids.Fluid;

public abstract class FluidConfig extends ExtendedConfig<FluidConfig> {

    public FluidConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Fluid> element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }

}
