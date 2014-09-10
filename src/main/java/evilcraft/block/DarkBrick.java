package evilcraft.block;
import net.minecraft.block.material.Material;
import evilcraft.core.config.configurable.ConfigurableBlock;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.item.DarkGem;

/**
 * Ore that drops {@link DarkGem}.
 * @author rubensworks
 *
 */
public class DarkBrick extends ConfigurableBlock {
    
    private static DarkBrick _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkBrick(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkBrick getInstance() {
        return _instance;
    }

    private DarkBrick(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(5.0F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
    }

}
