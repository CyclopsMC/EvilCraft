package evilcraft.api.config.elementtypeaction;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.EvilCraftTab;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;

public class ItemAction extends IElementTypeAction<ItemConfig>{

    @Override
    public void preRun(ItemConfig eConfig, Configuration config) {
        // Get property in config file and set comment
        Property property = config.getItem(eConfig.getHolderType().getCategory(), eConfig.NAMEDID, eConfig.ID);
        property.comment = eConfig.COMMENT;
        
        // Update the ID, it could've changed
        eConfig.ID = property.getInt();
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
        
        // Add I18N
        LanguageRegistry.addName(eConfig.getSubInstance(), eConfig.NAME);
    }

}
