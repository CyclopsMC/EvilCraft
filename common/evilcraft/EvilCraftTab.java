package evilcraft;
import net.minecraft.creativetab.CreativeTabs;

public class EvilCraftTab extends CreativeTabs{
    
    private static EvilCraftTab _instance = null;
    
    public static EvilCraftTab getInstance() {
        if(_instance == null)
            _instance = new EvilCraftTab();
        return _instance;
    }

    private EvilCraftTab() {
        super(Reference.MOD_NAME);
    }
}
