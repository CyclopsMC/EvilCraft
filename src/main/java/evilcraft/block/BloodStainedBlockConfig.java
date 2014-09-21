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
     * The blacklisted blood stained blocks, by block name.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK,
    		comment = "The blacklisted blood stained blocks, by block name.")
    public static String[] blockBlacklist = new String[]{
    	"minecraft:redstone_lamp",
    	"minecraft:crafting_table",
    };

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
