package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Undead Plank.
 * @author rubensworks
 *
 */
public class BlockUndeadPlankConfig extends BlockConfig {

    public BlockUndeadPlankConfig() {
        super(
                EvilCraft._instance,
            "undead_planks",
                eConfig -> new Block(Block.Properties.of()
                        .strength(2.0F)
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
