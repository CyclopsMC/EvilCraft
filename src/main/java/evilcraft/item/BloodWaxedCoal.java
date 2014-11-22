package evilcraft.item;

import cpw.mods.fml.common.IFuelHandler;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import net.minecraft.item.ItemStack;

/**
 * Double coal efficiency.
 * @author rubensworks
 *
 */
public class BloodWaxedCoal extends ConfigurableItem implements IFuelHandler {

    private static BloodWaxedCoal _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodWaxedCoal(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodWaxedCoal getInstance() {
        return _instance;
    }

    private BloodWaxedCoal(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

	@Override
	public int getBurnTime(ItemStack fuel) {
		if(this == fuel.getItem()) {
			return 3200;
		}
		return 0;
	}

}
