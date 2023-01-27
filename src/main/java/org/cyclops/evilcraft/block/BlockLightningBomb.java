package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PRIMED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(PRIMED, false);
    }

    @Override
    public void onPlace(BlockState blockState, Level world, BlockPos blockPos, BlockState oldState, boolean isMoving) {
        super.onPlace(blockState, world, blockPos, oldState, isMoving);

        if (world.hasNeighborSignal(blockPos)) {
            this.destroy(world, blockPos, blockState.setValue(PRIMED, true));
            world.removeBlock(blockPos, false);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level world, BlockPos blockPos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(blockState, world, blockPos, blockIn, fromPos, isMoving);

        if (world.hasNeighborSignal(blockPos)) {
            this.destroy(world, blockPos, blockState.setValue(PRIMED, true));
            world.removeBlock(blockPos, false);
        }
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos blockPos, Explosion explosion) {
        if (!world.isClientSide()) {
            EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed(world,
                    (double)((float)blockPos.getX() + 0.5F), (double)((float)blockPos.getY() + 0.5F),
                    (double)((float)blockPos.getZ() + 0.5F), explosion.getIndirectSourceEntity());
            entityprimed.setFuse(world.random.nextInt(entityprimed.getFuse() / 4) + entityprimed.getFuse() / 8);
            world.addFreshEntity(entityprimed);
            world.removeBlock(blockPos, false);
        }
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState) {
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
    public void primeBomb(LevelAccessor world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity placer) {
        if (!world.isClientSide()) {
            if (blockState.getValue(PRIMED)) {
                EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed((Level) world,
                        (float)blockPos.getX() + 0.5F, (float)blockPos.getY() + 0.5F,
                        (float)blockPos.getZ() + 0.5F, placer);
                world.addFreshEntity(entityprimed);
                world.playSound(null, blockPos, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_225533_6_) {
        if (!player.getItemInHand(hand).isEmpty() &&
                (player.getItemInHand(hand).getItem() == Items.FLINT_AND_STEEL || player.getItemInHand(hand).getItem() == Items.FIRE_CHARGE)) {
            this.primeBomb(world, blockPos, defaultBlockState().setValue(PRIMED, true), player);
            world.removeBlock(blockPos, false);
            player.getItemInHand(hand).hurtAndBreak(1, player, (e) -> {});
            return InteractionResult.SUCCESS;
        } else {
            return super.use(state, world, blockPos, player, hand, p_225533_6_);
        }
    }

    @Override
    public void stepOn(Level world, BlockPos blockPos, BlockState blockState, Entity entity) {
        BlockState primedState = defaultBlockState().setValue(PRIMED, true);
        if (entity instanceof AbstractArrow && !world.isClientSide()) {
            AbstractArrow entityarrow = (AbstractArrow)entity;

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
