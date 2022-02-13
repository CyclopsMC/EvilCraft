package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.block.component.ParticleDropBlockComponent;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.tileentity.TileBloodStain;

import java.util.Random;

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
    public int getLightBlock(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        particleDropBlockComponent.randomDisplayTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
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
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        super.randomTick(state, worldIn, pos, random);

        if (canFormBloodStains(state)) {
            // Find a position below this block with air above it (or being a blood stain)
            int attempts = 20;
            BlockPos itPos = pos.relative(Direction.DOWN);
            while (attempts-- > 0) {
                if (worldIn.getBlockState(itPos).getBlock() instanceof BlockBloodStain
                        || (worldIn.isEmptyBlock(itPos) && RegistryEntries.BLOCK_BLOOD_STAIN.canSurvive(state, worldIn, itPos))) {

                    // Set the air block to a blood stain
                    BlockState blockState = worldIn.getBlockState(itPos);
                    if (blockState.isAir(worldIn, pos)) {
                        blockState = RegistryEntries.BLOCK_BLOOD_STAIN.defaultBlockState();
                        worldIn.setBlockAndUpdate(itPos, blockState);
                    }

                    // Add blood to existing stain
                    if (blockState.getBlock() instanceof BlockBloodStain) {
                        TileHelpers.getSafeTile(worldIn, itPos, TileBloodStain.class)
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
}
