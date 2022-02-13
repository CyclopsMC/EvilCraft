package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.cyclops.evilcraft.entity.block.EntityLightningBombPrimed;
import org.cyclops.evilcraft.entity.item.EntityLightningGrenade;

import javax.annotation.Nullable;

/**
 * A bomb that spawns lightning when ignited.
 * @author rubensworks
 *
 */
public class BlockLightningBomb extends Block {

    public static final BooleanProperty PRIMED = BooleanProperty.create("primed");

    public BlockLightningBomb(Block.Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PRIMED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PRIMED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(PRIMED, false);
    }

    @Override
    public void onPlace(BlockState blockState, World world, BlockPos blockPos, BlockState oldState, boolean isMoving) {
        super.onPlace(blockState, world, blockPos, oldState, isMoving);

        if (world.hasNeighborSignal(blockPos)) {
            this.destroy(world, blockPos, blockState.setValue(PRIMED, true));
            world.removeBlock(blockPos, false);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, World world, BlockPos blockPos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(blockState, world, blockPos, blockIn, fromPos, isMoving);

        if (world.hasNeighborSignal(blockPos)) {
            this.destroy(world, blockPos, blockState.setValue(PRIMED, true));
            world.removeBlock(blockPos, false);
        }
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos blockPos, Explosion explosion) {
        if (!world.isClientSide()) {
            EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed(world,
                    (double)((float)blockPos.getX() + 0.5F), (double)((float)blockPos.getY() + 0.5F),
                    (double)((float)blockPos.getZ() + 0.5F), explosion.getSourceMob());
            entityprimed.setFuse(world.random.nextInt(entityprimed.getLife() / 4) + entityprimed.getLife() / 8);
            world.addFreshEntity(entityprimed);
            world.removeBlock(blockPos, false);
        }
    }

    @Override
    public void destroy(IWorld world, BlockPos blockPos, BlockState blockState) {
        super.destroy(world, blockPos, blockState);
        this.primeBomb(world, blockPos, blockState, null);
    }
    
    /**
     * Spawns the primed bomb and plays the fuse sound.
     * @param world The world.
     * @param blockPos The position.
     * @param blockState The state.
     * @param placer The entity that primed the bomb.
     */
    public void primeBomb(IWorld world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity placer) {
        if (!world.isClientSide()) {
            if (blockState.getValue(PRIMED)) {
                EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed((World) world,
                        (float)blockPos.getX() + 0.5F, (float)blockPos.getY() + 0.5F,
                        (float)blockPos.getZ() + 0.5F, placer);
                world.addFreshEntity(entityprimed);
                world.playSound(null, blockPos, SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_) {
        if (!player.getItemInHand(hand).isEmpty() &&
                (player.getItemInHand(hand).getItem() == Items.FLINT_AND_STEEL || player.getItemInHand(hand).getItem() == Items.FIRE_CHARGE)) {
            this.primeBomb(world, blockPos, defaultBlockState().setValue(PRIMED, true), player);
            world.removeBlock(blockPos, false);
            player.getItemInHand(hand).hurtAndBreak(1, player, (e) -> {});
            return ActionResultType.SUCCESS;
        } else {
            return super.use(state, world, blockPos, player, hand, p_225533_6_);
        }
    }

    @Override
    public void stepOn(World world, BlockPos blockPos, Entity entity) {
        BlockState primedState = defaultBlockState().setValue(PRIMED, true);
        if (entity instanceof AbstractArrowEntity && !world.isClientSide()) {
            AbstractArrowEntity entityarrow = (AbstractArrowEntity)entity;

            if (entityarrow.isOnFire()) {
                this.primeBomb(world, blockPos, primedState, entityarrow.getOwner() instanceof LivingEntity ? (LivingEntity)entityarrow.getOwner() : null);
                world.removeBlock(blockPos, false);
            }
        } else if (entity instanceof EntityLightningGrenade && !world.isClientSide()) {
            EntityLightningGrenade entitygrenade = (EntityLightningGrenade)entity;

            this.primeBomb(world, blockPos, primedState, entitygrenade.getOwner() instanceof LivingEntity ? (LivingEntity) entitygrenade.getOwner() : null);
            world.removeBlock(blockPos, false);
        }
    }
    
    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

}
