package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class UndeadLogConfig extends BlockConfig {
    
    public static UndeadLogConfig _instance;

    public UndeadLogConfig() {
        super(
            Reference.BLOCK_UNDEADWOOD,
            "Undead Log",
            "undeadLog",
            null,
            UndeadLog.class
        );
    }
    
}
