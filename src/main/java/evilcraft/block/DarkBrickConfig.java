package evilcraft.block;

import evilcraft.core.config.configurable.ConfigurableBlock;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.BlockConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Config for the Dark Brick.
 * @author rubensworks
 *
 */
public class DarkBrickConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static DarkBrickConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkBrickConfig() {
        super(
        	true,
            "darkBrick",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return (ConfigurableBlock) new ConfigurableBlock(this, Material.rock).
                setHarvestLevelDefined("pickaxe", 2).setHardness(5.0F).
                setStepSound(Block.soundTypeStone);
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
