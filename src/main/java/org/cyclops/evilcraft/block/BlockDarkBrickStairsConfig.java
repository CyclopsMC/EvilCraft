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
 * Config for the Dark Brick Stairs.
 * @author rubensworks
 *
 */
public class BlockDarkBrickStairsConfig extends BlockConfig {

    public BlockDarkBrickStairsConfig() {
        super(
                EvilCraft._instance,
                "dark_brick_stairs",
                eConfig -> new StairsBlock(() -> RegistryEntries.BLOCK_DARK_BRICK.defaultBlockState(),
                        Block.Properties.of(Material.STONE)
                                .strength(5.0F)
                                .sound(SoundType.STONE)
                                .harvestTool(ToolType.PICKAXE)
                                .harvestLevel(2)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
