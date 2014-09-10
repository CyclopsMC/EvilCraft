package evilcraft.block;

import net.minecraft.item.ItemBlock;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.item.ItemBlockMetadata;

/**
 * Config for the {@link BloodStainedBlock}.
 * @author rubensworks
 *
 */
public class BloodStainedBlockConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static BloodStainedBlockConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodStainedBlockConfig() {
        super(
        	true,
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
