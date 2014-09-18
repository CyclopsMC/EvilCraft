package evilcraft.block;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;

/**
 * Config for the {@link BloodStainedBlock}.
 * @author rubensworks
 *
 */
public class BloodStainedBlockConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static BloodStainedBlockConfig _instance;
    
    /**
     * The amount of blood per HP (2HP = 1 heart) of the max mob health that will be added to this block when a mob dies from fall damage.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK, comment = "The amount of blood per HP (2HP = 1 heart) of the max mob health that will be added to this block when a mob dies from fall damage.", isCommandable = true)
    public static int bloodMBPerHP = 20;

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
    
}
