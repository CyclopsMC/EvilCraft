package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.block.component.ParticleDropBlockComponent;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodStain;


/**
 * Leaves for the Undead Tree.
 * @author rubensworks
 *
 */
public class BlockUndeadLeaves extends LeavesBlock {

    private ParticleDropBlockComponent particleDropBlockComponent;

    public BlockUndeadLeaves(Block.Properties properties) {
        super(properties);

        if (MinecraftHelpers.isClientSide()) {
            particleDropBlockComponent = new ParticleDropBlockComponent(1.0F, 0.0F, 0.0F);
            particleDropBlockComponent.setOffset(0);
            particleDropBlockComponent.setChance(50);
        }
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        particleDropBlockComponent.randomDisplayTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return new ItemStack(this);
    }

    protected boolean canFormBloodStains(BlockState state) {
        return BlockUndeadLeavesConfig.maxBloodStainAmount > 0 && !state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 2;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) || canFormBloodStains(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        super.randomTick(state, worldIn, pos, random);

        if (canFormBloodStains(state)) {
            // Find a position below this block with air above it (or being a blood stain)
            int attempts = 20;
            BlockPos itPos = pos.relative(Direction.DOWN);
            while (attempts-- > 0) {
                if (worldIn.getBlockState(itPos).getBlock() instanceof org.cyclops.evilcraft.block.BlockBloodStain
                        || (worldIn.isEmptyBlock(itPos) && RegistryEntries.BLOCK_BLOOD_STAIN.canSurvive(state, worldIn, itPos))) {

                    // Set the air block to a blood stain
                    BlockState blockState = worldIn.getBlockState(itPos);
                    if (worldIn.isEmptyBlock(itPos)) {
                        blockState = RegistryEntries.BLOCK_BLOOD_STAIN.defaultBlockState();
                        worldIn.setBlockAndUpdate(itPos, blockState);
                    }

                    // Add blood to existing stain
                    if (blockState.getBlock() instanceof org.cyclops.evilcraft.block.BlockBloodStain) {
                        BlockEntityHelpers.get(worldIn, itPos, BlockEntityBloodStain.class)
                                .ifPresent(tile -> tile.addAmount(1 + worldIn.random.nextInt(BlockUndeadLeavesConfig.maxBloodStainAmount)));
                    }

                    break;
                }

                // Stop if we find any other block blocking the way
                if (!worldIn.isEmptyBlock(itPos)) {
                    break;
                } else {
                    itPos = itPos.relative(Direction.DOWN);
                }
            }
        }
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }
}
