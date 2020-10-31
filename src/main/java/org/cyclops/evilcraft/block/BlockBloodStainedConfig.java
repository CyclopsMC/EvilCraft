package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockBloodStained}.
 * @author rubensworks
 *
 */
public class BlockBloodStainedConfig extends BlockConfig {

    @ConfigurableProperty(category = "block", comment = "The amount of blood per HP (2HP = 1 heart) of the max mob health that will be added to this blockState when a mob dies from fall damage.", isCommandable = true)
    public static int bloodMBPerHP = 20;

    public BlockBloodStainedConfig() {
        super(
                EvilCraft._instance,
                "blood_stained",
                eConfig -> new BlockBloodStained(Block.Properties.create(Material.CLAY)
                        .hardnessAndResistance(0.5F)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
}
