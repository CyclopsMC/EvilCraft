package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the Crushed Dark Gem.
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
                EvilCraft._instance,
        	true,
            "dark_gem_crushed",
            null,
            null
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_GEMDARKCRUSHED;
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this) {
            @Override
            public int getItemBurnTime(ItemStack itemStack) {
                return 16000;
            }
        };
    }
    
}
