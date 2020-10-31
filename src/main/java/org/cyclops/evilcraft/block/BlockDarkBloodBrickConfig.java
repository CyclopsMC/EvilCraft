package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockDarkBloodBrick}.
 * @author rubensworks
 *
 */
public class BlockDarkBloodBrickConfig extends BlockConfig {

    public BlockDarkBloodBrickConfig() {
        super(
                EvilCraft._instance,
            "dark_blood_brick",
                eConfig -> new BlockDarkBloodBrick(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(5.0F)
                        .sound(SoundType.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(2)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
}
