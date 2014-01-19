package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.item.ItemBlockNBT;

public class BloodInfuserConfig extends BlockConfig {
    
    public static BloodInfuserConfig _instance;

    public BloodInfuserConfig() {
        super(
            Reference.BLOCK_BLOODINFUSER,
            "Blood Infuser",
            "bloodInfuser",
            null,
            BloodInfuser.class
        );
    }
    
    @Override
    public boolean hasSubTypes() {
        return true;
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }
    
}
