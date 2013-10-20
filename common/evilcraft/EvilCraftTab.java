package evilcraft;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.items.DarkGem;

public class EvilCraftTab extends CreativeTabs{
    
    private static EvilCraftTab _instance = null;
    
    public static EvilCraftTab getInstance() {
        if(_instance == null)
            _instance = new EvilCraftTab();
        return _instance;
    }

    private EvilCraftTab() {
        super(Reference.MOD_NAME);
        LanguageRegistry.instance().addStringLocalization("itemGroup." + Reference.MOD_NAME, "en_US", Reference.MOD_NAME);
    }
    
    @Override
    public int getTabIconItemIndex() {
        return DarkGem.getInstance().itemID;
    }
}
