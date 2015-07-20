package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
                EvilCraft._instance,
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
