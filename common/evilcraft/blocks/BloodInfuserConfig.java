package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.item.ItemBlockNBT;

/**
 * Config for the {@link BloodInfuser}.
 * @author rubensworks
 *
 */
public class BloodInfuserConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static BloodInfuserConfig _instance;

    /**
     * Make a new instance.
     */
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
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }
    
}
