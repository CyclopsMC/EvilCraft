package evilcraft.item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * A crushed dark gem, apparently the dark gem is purple on the inside!
 * @author rubensworks
 *
 */
public class DarkGemCrushed extends ConfigurableItem implements IFuelHandler {
    
    private static DarkGemCrushed _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkGemCrushed(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkGemCrushed getInstance() {
        return _instance;
    }

    private DarkGemCrushed(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

	@Override
	public int getBurnTime(ItemStack fuel) {
		if(this == fuel.getItem()) {
			return 16000;
		}
		return 0;
	}

}
