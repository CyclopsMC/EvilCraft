package evilcraft.block;

import net.minecraft.item.ItemBlock;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.item.ItemBlockNBT;

/**
 * Config for the {@link BloodInfuser}.
 * @author rubensworks
 *
 */
public class BloodInfuserConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static BloodInfuserConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodInfuserConfig() {
        super(
        	true,
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
