package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the Dark Blood Brick Stairs.
 * @author rubensworks
 *
 */
public class BlockDarkBloodBrickStairsConfig extends BlockConfig {

    public BlockDarkBloodBrickStairsConfig() {
        super(
                EvilCraft._instance,
                "dark_blood_brick_stairs",
                eConfig -> new StairsBlock(() -> RegistryEntries.BLOCK_DARK_BLOOD_BRICK.getDefaultState(),
                        Block.Properties.create(Material.ROCK)
                                .hardnessAndResistance(5.0F)
                                .sound(SoundType.STONE)
                                .harvestTool(ToolType.PICKAXE)
                                .harvestLevel(2)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
