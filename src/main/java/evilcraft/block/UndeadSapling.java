package evilcraft.block;

import evilcraft.core.config.configurable.ConfigurableBlockSapling;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Sapling for the Undead Tree.
 * @author rubensworks
 *
 */
public class UndeadSapling extends ConfigurableBlockSapling {
    
    private static UndeadSapling _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static UndeadSapling getInstance() {
        return _instance;
    }

    public UndeadSapling(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.plants);
    }

}
