package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Undead Slab.
 * @author rubensworks
 *
 */
public class BlockUndeadSlabConfig extends BlockConfig {

    public BlockUndeadSlabConfig() {
        super(
                EvilCraft._instance,
            "undead_slab",
                eConfig -> new SlabBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_ORANGE)
                        .strength(2.0F, 3.0F)
                        .sound(SoundType.WOOD)) {
                    @Override
                    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                        return 5;
                    }

                    @Override
                    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                        return 20;
                    }
                },
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
}
