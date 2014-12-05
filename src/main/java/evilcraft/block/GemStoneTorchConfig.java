package evilcraft.block;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.configurable.ConfigurableBlockTorch;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.BlockConfig;

/**
 * Config for the Burning Gemstone Torch.
 * @author rubensworks
 *
 */
public class GemStoneTorchConfig extends BlockConfig {

    /**
     * The unique instance.
     */
    public static GemStoneTorchConfig _instance;

    /**
     * The radius that will be kept spirit-proof.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK, comment = "The radius that will be kept spirit-proof.", isCommandable = true)
    public static int area = 15;

    /**
     * Make a new instance.
     */
    public GemStoneTorchConfig() {
        super(
        	true,
            "gemStoneTorch",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableBlockTorch(this);
    }
    
}
