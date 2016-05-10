package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockSapling;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.world.gen.WorldGeneratorUndeadTree;

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
        super(eConfig, Material.plants, new WorldGeneratorUndeadTree(true));
    }

}
