package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class HardenedBloodShardConfig extends ItemConfig {
    
    public static HardenedBloodShardConfig _instance;

    public HardenedBloodShardConfig() {
        super(
            Reference.ITEM_DARKSTICK,
            "Hardened Blood Shard",
            "hardenedBloodShard",
            null,
            HardenedBloodShard.class
        );
    }
    
    @Override
    public void onRegistered() {
        OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(HardenedBloodShard.getInstance()));
    }
    
    @Override
    public String getOreDictionaryId() {
        return "shardBlood";
    }
    
}
