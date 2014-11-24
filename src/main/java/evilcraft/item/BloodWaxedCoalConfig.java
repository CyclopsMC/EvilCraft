package evilcraft.item;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ItemConfig;
import net.minecraft.item.ItemStack;

/**
 * Config for the Blood-Waxed Coal.
 * @author rubensworks
 *
 */
public class BloodWaxedCoalConfig extends ItemConfig implements IFuelHandler {

    /**
     * The unique instance.
     */
    public static BloodWaxedCoalConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodWaxedCoalConfig() {
        super(
        	true,
            "bloodWaxedCoal",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this);
    }
    
    @Override
    public void onRegistered() {
    	GameRegistry.registerFuelHandler(this);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        if(getItemInstance() == fuel.getItem()) {
            return 3200;
        }
        return 0;
    }

}
