package evilcraft.item;

import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link evilcraft.item.BloodWaxedCoal}.
 * @author rubensworks
 *
 */
public class BloodWaxedCoalConfig extends ItemConfig {

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
            BloodWaxedCoal.class
        );
    }
    
    @Override
    public void onRegistered() {
    	GameRegistry.registerFuelHandler((BloodWaxedCoal) getItemInstance());
    }
    
}
