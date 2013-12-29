package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class DarkStickConfig extends ExtendedConfig {
    
    public static DarkStickConfig _instance;

    public DarkStickConfig() {
        super(
            Reference.ITEM_DARKSTICK,
            "Dark Stick",
            "darkStick",
            null,
            DarkStick.class
        );
    }
    
    @Override
    public void onRegistered() {
        OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(DarkStick.getInstance()));
    }
    
    @Override
    public String getOreDictionaryId() {
        return "stickWood";
    }
    
}
