package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class BloodyCobblestoneConfig extends BlockConfig {
    
    public static BloodyCobblestoneConfig _instance;

    public BloodyCobblestoneConfig() {
        super(
            Reference.BLOCK_DARKBLOCK,
            "Bloody Cobblestone",
            "bloodyCobblestone",
            null,
            BloodyCobblestone.class
        );
    }
    
    @Override
    public void onRegistered() {
        OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(BloodyCobblestone.getInstance()));
    }
    
    public String getOreDictionaryId() {
        return "blockStone";
    }
    
}
