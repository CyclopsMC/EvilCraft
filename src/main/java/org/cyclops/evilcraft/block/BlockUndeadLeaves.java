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
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

    /*
    @Override
    public Item getItemDropped(BlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(Blocks.DEADBUSH);
    }
    TODO: loot table
     */

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

    /*
    @Override
    protected ItemStack getSilkTouchDrop(BlockState state) {
        return new ItemStack(this);
    }
    TODO: loot tables
     */

    protected boolean canFormBloodStains(BlockState state) {
        return BlockUndeadLeavesConfig.maxBloodStainAmount > 0 && !state.get(PERSISTENT) && state.get(DISTANCE) == 2;
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return super.ticksRandomly(state) || canFormBloodStains(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        super.randomTick(state, worldIn, pos, random);

        if (canFormBloodStains(state)) {
            // Find a position below this block with air above it (or being a blood stain)
            int attempts = 20;
            BlockPos itPos = pos.offset(Direction.DOWN);
            while (attempts-- > 0) {
                if (worldIn.getBlockState(itPos).getBlock() instanceof BlockBloodStain
                        || (worldIn.isAirBlock(itPos) && RegistryEntries.BLOCK_BLOOD_STAIN.isValidPosition(state, worldIn, itPos))) {

                    // Set the air block to a blood stain
                    BlockState blockState = worldIn.getBlockState(itPos);
                    if (blockState.isAir(worldIn, pos)) {
                        blockState = RegistryEntries.BLOCK_BLOOD_STAIN.getDefaultState();
                        worldIn.setBlockState(itPos, blockState);
                    }

                    // Add blood to existing stain
                    if (blockState.getBlock() instanceof BlockBloodStain) {
                        TileHelpers.getSafeTile(worldIn, itPos, TileBloodStain.class)
                                .ifPresent(tile -> tile.addAmount(1 + worldIn.rand.nextInt(BlockUndeadLeavesConfig.maxBloodStainAmount)));
                    }

                    break;
                }

                // Stop if we find any other block blocking the way
                if (!worldIn.isAirBlock(itPos)) {
                    break;
                } else {
                    itPos = itPos.offset(Direction.DOWN);
                }
            }
        }
    }
}
