package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.client.particle.ParticleBloodSplash;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.tileentity.TileBloodStain;

import java.util.Random;

/**
 * A blood stain that can rest on other blocks.
 * @author rubensworks
 */
public class BlockBloodStain extends BlockTile {

    private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public BlockBloodStain(Block.Properties properties) {
        super(properties, TileBloodStain::new);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return true;
    }

    @Override
    public boolean isReplaceable(BlockState p_225541_1_, Fluid p_225541_2_) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return blockstate.isSolidSide(worldIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            if (!state.isValidPosition(worldIn, pos)) {
                worldIn.removeBlock(pos, false);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        splash(worldIn, pos);
        super.onBlockClicked(state, worldIn, pos, player);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn.getMotion().length() > 0.1D) {
            splash(worldIn, pos);
        }
        super.onEntityCollision(state, worldIn, pos, entityIn);
    }

    /**
     * Spawn particles.
     * @param world The world.
     * @param blockPos The position.
     */
    @OnlyIn(Dist.CLIENT)
    public static void splash(World world, BlockPos blockPos) {
    	if(MinecraftHelpers.isClientSide()) {
    		ParticleBloodSplash.spawnParticles(world, blockPos, 1, 1 + world.rand.nextInt(1));
    	}
    }
    
    @Override
    public void fillWithRain(World world, BlockPos blockPos) {
        world.removeBlock(blockPos, false);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void bloodStainedBlockEvent(LivingDeathEvent event) {
        if(event.getSource() == DamageSource.FALL
                && !(event.getEntity() instanceof EntityVengeanceSpirit)) {
            int x = MathHelper.floor(event.getEntity().getPosX());
            int y = MathHelper.floor(event.getEntity().getPosY() - (event.getEntity().getHeight() - 1));
            int z = MathHelper.floor(event.getEntity().getPosZ());

            if (!event.getEntity().world.isRemote()) {
                event.getEntity().getServer().execute(() -> {
                    // Only in the next tick, to resolve #601.
                    // The problem is that Vanilla's logic for handling fall events caches the Block.
                    // But Forge throws the living death event _after_ this block is determined,
                    // after which vanilla can still perform operators with this block.
                    // In some cases, this can result in inconsistencies, which can lead to crashes.
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = event.getEntity().world.getBlockState(pos).getBlock();

                    int amount = (int) (BlockBloodStainConfig.bloodMBPerHP * event.getEntityLiving().getMaxHealth());
                    if (block != this) {
                        // Offset position by one
                        pos = pos.add(0, 1, 0);

                        // Add a new blood stain block
                        if (isValidPosition(getDefaultState(), event.getEntity().getEntityWorld(), pos)) {
                            event.getEntity().getEntityWorld().setBlockState(pos, getDefaultState());
                        }
                    }
                    // Add blood to existing block
                    TileHelpers.getSafeTile(event.getEntity().getEntityWorld(), pos, TileBloodStain.class)
                            .ifPresent(tile -> tile.addAmount(amount));
                });
            } else {
                // Init particles
                Random random = new Random();
                BlockPos pos = new BlockPos(x, y, z);
                ParticleBloodSplash.spawnParticles(event.getEntity().world, pos.add(0, 1, 0),
                        ((int) event.getEntityLiving().getMaxHealth()) + random.nextInt(15), 5 + random.nextInt(5));
            }
        }
    }

}
