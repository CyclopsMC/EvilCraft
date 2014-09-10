package evilcraft.item;

import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link DarkGemCrushed}.
 * @author rubensworks
 *
 */
public class DarkGemCrushedConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static DarkGemCrushedConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkGemCrushedConfig() {
        super(
        	true,
            "darkGemCrushed",
            null,
            DarkGemCrushed.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_GEMDARKCRUSHED;
    }
    
    @Override
    public void onRegistered() {
    	GameRegistry.registerFuelHandler((DarkGemCrushed) getItemInstance());
    }
    
}
