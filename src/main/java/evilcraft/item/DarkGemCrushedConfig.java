package evilcraft.item;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ItemConfig;
import net.minecraft.item.ItemStack;

/**
 * Config for the Crushed Dark Gem.
 * @author rubensworks
 *
 */
public class DarkGemCrushedConfig extends ItemConfig implements IFuelHandler {
    
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
            null
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_GEMDARKCRUSHED;
    }
    
    @Override
    public void onRegistered() {
    	GameRegistry.registerFuelHandler(this);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        if(getItemInstance() == fuel.getItem()) {
            return 16000;
        }
        return 0;
    }
    
}
