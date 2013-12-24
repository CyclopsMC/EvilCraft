package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class BloodyCobblestoneConfig extends ExtendedConfig {
    
    public static BloodyCobblestoneConfig _instance;

    public BloodyCobblestoneConfig() {
        super(
            Reference.BLOCK_DARKBLOCK,
            "Bloody Cobblestone",
            "bloodycobblestone",
            null,
            BloodyCobblestone.class
        );
    }
    
    @Override
    public void onRegistered() {
        OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(BloodyCobblestone.getInstance()));
    }
    
    @Override
    public String getOreDictionaryId() {
        return "cobblestone";
    }
    
}
