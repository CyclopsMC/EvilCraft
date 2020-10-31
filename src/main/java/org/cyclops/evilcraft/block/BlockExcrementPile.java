package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.broom.IBroom;

import java.util.Random;

/**
 * The excrement that is dropped by animals.
 * @author rubensworks
 *
 */
public class BlockExcrementPile extends Block {

    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);
    
    private static final int CHANCE_DESPAWN = 1; // 1/CHANCE_DESPAWN per random tick chance for despawning and potentially triggering bonemeal event
    private static final int CHANCE_BONEMEAL = 3; // 1/CHANCE_BONEMEAL for ground below to be bonemealed or turn into grass from dirt
    private static final int POISON_DURATION = 3;
    private static final int PIG_BOOST_DURATION = 3;

    public BlockExcrementPile(Block.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 16.0F, (float)getHeight(state) * 2.0F, 16.0F);
    }

    protected static int getHeight(BlockState blockState) {
        return BlockHelpers.getSafeBlockStateProperty(blockState, LAYERS, 2);
    }

    @Override
    public boolean isValidPosition(BlockState blockState, IWorldReader world, BlockPos blockPos) {
        if (blockState.getBlock() == this && (Integer) blockState.get(LAYERS) == 8) return true;
        return (blockState.getBlock() instanceof LeavesBlock)
                || blockState.isOpaqueCube(world, blockPos) && blockState.getBlock().getMaterial(blockState).blocksMovement();
    }

    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return player.getHeldItemMainhand().getItem() instanceof IBroom;
    }

    @Override
    public int tickRate(IWorldReader worldIn) {
        return 100;
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos blockPos, Random random) {
        if(random.nextInt(CHANCE_DESPAWN) == 0) {
            if(random.nextInt(CHANCE_BONEMEAL) == 0) {
                for(int xr = - 2; xr <= 2; xr++) {
                    for(int zr = - 2; zr <= 2; zr++) {
                        if(random.nextInt(9) == 0) {
                            BlockState blockBelow = world.getBlockState(blockPos.add(xr, -1, zr));
                            if(blockBelow.getBlock() == Blocks.DIRT) {
                                world.setBlockState(blockPos.add(xr, -1, zr), Blocks.GRASS.getDefaultState());
                            } else if(blockBelow.getBlock() == Blocks.GRASS) {
                                BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL, 1), world, blockPos.add(xr, -1, zr));
                            }
                            BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL, 1), world, blockPos.add(xr, -1, zr));
                        }
                    }
                }
            }
            world.removeBlock(blockPos, false);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        if(random.nextInt(100) == 0) {
            float height = (float)(getHeight(blockState)) / 8.0F;
            double d0 = (double)blockPos.getX() + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)blockPos.getY() + 0.0625F + height);
            double d2 = (double)blockPos.getZ() + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;

            float f1 = 0.1F-random.nextFloat()*0.2F;
            float f2 = 0.1F-random.nextFloat()*0.2F;
            float f3 = 0.1F-random.nextFloat()*0.2F;
            world.addParticle(ParticleTypes.HAPPY_VILLAGER, false,
                    d0, d1, d2, f1, f2, f3);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return side != Direction.UP || super.isSideInvisible(state, adjacentBlockState, side);
    }

    @Override
    public boolean canBeReplacedByLogs(BlockState state, IWorldReader world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!isValidPosition(state, worldIn, pos)) {
            worldIn.removeBlock(pos, false);
        }
    }
    
    @Override
    public void onEntityWalk(World world, BlockPos blockPos, Entity entity) {
        if(entity instanceof LivingEntity) {
            // Pigs love excrement
            if(entity instanceof PigEntity) {
                if(isChanceWithHeight(world, blockPos)) {
                    ((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.REGENERATION, PIG_BOOST_DURATION * 20, 1));
                    ((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.SPEED, PIG_BOOST_DURATION * 20, 1));
                }
            } else if(entity instanceof PlayerEntity || BlockExcrementPileConfig.poisonEntities) {
                // Poison player or mob with a chance depending on the height of the pile.
                if(isChanceWithHeight(world, blockPos))
                    ((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.POISON, POISON_DURATION * 20, 1));
            }
        }
        super.onEntityWalk(world, blockPos, entity);
    }

    private boolean isChanceWithHeight(World world, BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        float height = ((float) getHeight(blockState)) * 0.125F;
        return world.rand.nextFloat() * 10 < height;
    }
    
    /**
     * If the height of this blockState can be increased.
     * @param world The World
     * @param blockPos The position.
     * @return If this pile can become higher.
     */
    public static boolean canHeightenPileAt(World world, BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        return getHeight(blockState) < 8;
    }

    /**
     * Set the height of a pile.
     * @param world the World
     * @param blockPos The position.
     */
    public static void heightenPileAt(World world, BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        int height = getHeight(blockState);
        if(height < 8)
            world.setBlockState(blockPos, RegistryEntries.BLOCK_EXCREMENT_PILE.getDefaultState().with(LAYERS, height + 1), 2);
    }

}
