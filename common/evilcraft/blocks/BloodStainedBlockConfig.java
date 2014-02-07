package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.item.ItemBlockMetadata;

public class BloodStainedBlockConfig extends BlockConfig {
    
    public static BloodStainedBlockConfig _instance;

    public BloodStainedBlockConfig() {
        super(
            Reference.BLOCK_BLOODSTAINEDDIRT,
            "Blood Stained Block",
            "bloodStainedBlock",
            null,
            BloodStainedBlock.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockMetadata.class;
    }
    
}
