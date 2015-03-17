package evilcraft;

import evilcraft.item.DarkGem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Creative tab for EvilCraft.
 * @author rubensworks
 *
 */
public class EvilCraftTab extends CreativeTabs{
    
    private static EvilCraftTab _instance = null;
    
    /**
     * Get the unique instance.
     * @return The unique instance.
     */
    public static EvilCraftTab getInstance() {
        if(_instance == null)
            _instance = new EvilCraftTab();
        return _instance;
    }

    private EvilCraftTab() {
        super(Reference.MOD_NAME);
    }
    
    @Override
    public Item getTabIconItem() {
        return DarkGem.getInstance();
    }
}
