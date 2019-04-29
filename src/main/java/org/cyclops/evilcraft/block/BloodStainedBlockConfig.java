package org.cyclops.evilcraft.block;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.evilcraft.EvilCraft;

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
     * The amount of blood per HP (2HP = 1 heart) of the max mob health that will be added to this blockState when a mob dies from fall damage.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK, comment = "The amount of blood per HP (2HP = 1 heart) of the max mob health that will be added to this blockState when a mob dies from fall damage.", isCommandable = true)
    public static int bloodMBPerHP = 20;

    /**
     * The whitelisted blood stained blocks, by blockState name. (Java regular expressions are allowed). Only applicable if this list is not empty. Blacklist will still be checked after this.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK,
            comment = "The whitelisted blood stained blocks, by blockState name. (Java regular expressions are allowed). Only applicable if this list is not empty. Blacklist will still be checked after this.")
    public static String[] blockWhitelist = new String[]{
            // Empty by default
    };
    
    /**
     * The blacklisted blood stained blocks, by blockState name. (Java regular expressions are allowed).
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK,
    		comment = "The blacklisted blood stained blocks, by blockState name. (Java regular expressions are allowed)")
    public static String[] blockBlacklist = new String[]{
            "minecraft:bedrock",
            "minecraft:redstone_lamp",
            "minecraft:crafting_table",
            "minecraft:magma",
            "evilcraft:dark_blood_brick",
            "evilcraft:reinforced_undead_plank",
            "colossalchests:chest_wall"
    };

    /**
     * Make a new instance.
     */
    public BloodStainedBlockConfig() {
        super(
            EvilCraft._instance,
        	true,
            "blood_stained_block",
            null,
            BloodStainedBlock.class
        );
    }
    
}
