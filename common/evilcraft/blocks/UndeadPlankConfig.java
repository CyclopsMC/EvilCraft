package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class UndeadPlankConfig extends BlockConfig {
    
    public static UndeadPlankConfig _instance;

    public UndeadPlankConfig() {
        super(
            Reference.BLOCK_UNDEADPLANK,
            "Undead Plank",
            "undeadPlank",
            null,
            UndeadPlank.class
        );
    }
    
    @Override
    public void onRegistered() {
        OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(UndeadPlank.getInstance()));
    }
    
    public String getOreDictionaryId() {
        return "plankWood";
    }
    
}
