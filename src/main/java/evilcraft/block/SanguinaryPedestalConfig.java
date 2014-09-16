package evilcraft.block;

import evilcraft.client.render.block.RenderSanguinaryPedestal;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.proxy.ClientProxy;

/**
 * Config for the {@link SanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class SanguinaryPedestalConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static SanguinaryPedestalConfig _instance;
    
    /**
     * The amount of blood (mB) that will be gained as a result from extraction.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The amount of blood (mB) that will be gained as a result from extraction.")
    public static int extractMB = 750;

    /**
     * Make a new instance.
     */
    public SanguinaryPedestalConfig() {
        super(
        	true,
            "sanguinaryPedestal",
            null,
            SanguinaryPedestal.class
        );
    }
    
    @Override
    public void onRegistered() {
        if(MinecraftHelpers.isClientSide()) {
            ClientProxy.BLOCK_RENDERERS.add(new RenderSanguinaryPedestal());
        }
    }
    
}
