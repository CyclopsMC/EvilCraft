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
 * Config for the Reinforced Undead Plank Stairs.
 * @author rubensworks
 *
 */
public class BlockReinforcedUndeadPlankStairsConfig extends BlockConfig {

    public BlockReinforcedUndeadPlankStairsConfig() {
        super(
                EvilCraft._instance,
                "reinforced_undead_plank_stairs",
                eConfig -> new StairsBlock(() -> RegistryEntries.BLOCK_REINFORCED_UNDEAD_PLANK.getDefaultState(),
                        Block.Properties.create(Material.ROCK)
                                .hardnessAndResistance(1.5F)
                                .sound(SoundType.WOOD)
                                .harvestTool(ToolType.PICKAXE)
                                .harvestLevel(2)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
