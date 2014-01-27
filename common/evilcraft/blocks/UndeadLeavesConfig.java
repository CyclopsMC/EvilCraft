package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class UndeadLeavesConfig extends BlockConfig {
    
    public static UndeadLeavesConfig _instance;

    public UndeadLeavesConfig() {
        super(
            Reference.BLOCK_UNDEADLEAVES,
            "Undead Leaves",
            "undeadleaves",
            null,
            UndeadLeaves.class
        );
    }
    
}
