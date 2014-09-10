package evilcraft.block;
import net.minecraft.block.material.Material;
import evilcraft.core.config.configurable.ConfigurableBlockSapling;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;

/**
 * Sapling for the Undead Tree.
 * @author rubensworks
 *
 */
public class UndeadSapling extends ConfigurableBlockSapling {
    
    private static UndeadSapling _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new UndeadSapling(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static UndeadSapling getInstance() {
        return _instance;
    }

    private UndeadSapling(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.plants);
    }

}
