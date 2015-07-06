package evilcraft.block;


import evilcraft.EvilCraft;
import evilcraft.core.config.configurable.ConfigurableBlockTorch;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

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
                EvilCraft._instance,
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
