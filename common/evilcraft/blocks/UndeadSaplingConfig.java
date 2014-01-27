package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class UndeadSaplingConfig extends BlockConfig {
    
    public static UndeadSaplingConfig _instance;

    public UndeadSaplingConfig() {
        super(
            Reference.BLOCK_UNDEADSAPLING,
            "Undead Sapling",
            "undeadSapling",
            null,
            UndeadSapling.class
        );
    }
    
    @Override
    public void onRegistered() {
        OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(UndeadSapling.getInstance()));
    }
    
    public String getOreDictionaryId() {
        return "sapplingUndead";
    }
    
}
