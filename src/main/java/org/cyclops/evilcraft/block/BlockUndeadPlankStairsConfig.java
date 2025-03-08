package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the Undead Plank Stairs.
 * @author rubensworks
 *
 */
public class BlockUndeadPlankStairsConfig extends BlockConfigCommon<IModBase> {

    public BlockUndeadPlankStairsConfig() {
        super(
                EvilCraft._instance,
                "undead_planks_stairs",
                (eConfig, properties) -> new StairBlock(RegistryEntries.BLOCK_DARK_BLOOD_BRICK.get().defaultBlockState(),
                        properties
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
