package evilcraft.core.config.elementtypeaction;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.EvilCraftTab;
import evilcraft.core.config.ItemConfig;

/**
 * The action used for {@link ItemConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class ItemAction extends IElementTypeAction<ItemConfig>{

    @Override
    public void preRun(ItemConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.NAMEDID,
        		eConfig.isEnabled());
        property.setRequiresMcRestart(true);
        property.comment = eConfig.COMMENT;
        
        if(startup) {
	        // Update the ID, it could've changed
	        eConfig.setEnabled(property.getBoolean(true));
        }
    }

    @Override
    public void postRun(ItemConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        Item item = (Item) eConfig.getSubInstance();
        
        // Register
        GameRegistry.registerItem(
                item,
                eConfig.getSubUniqueName()
        );
        
        // Set creative tab
        item.setCreativeTab(EvilCraftTab.getInstance());
    }

}
