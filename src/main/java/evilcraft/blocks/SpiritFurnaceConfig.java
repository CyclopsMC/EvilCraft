package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.item.ItemBlockNBT;

/**
 * Config for the {@link SpiritFurnace}.
 * @author rubensworks
 *
 */
public class SpiritFurnaceConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static SpiritFurnaceConfig _instance;

    /**
     * Make a new instance.
     */
    public SpiritFurnaceConfig() {
        super(
        	true,
            "spiritFurnace",
            null,
            SpiritFurnace.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }
    
}
