package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
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
    }

    @Override
    public void onBlockAdded(BlockState blockState, World world, BlockPos blockPos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(blockState, world, blockPos, oldState, isMoving);

        if (world.isBlockPowered(blockPos)) {
            this.onPlayerDestroy(world, blockPos, blockState.with(PRIMED, true));
            world.removeBlock(blockPos, false);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, World world, BlockPos blockPos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(blockState, world, blockPos, blockIn, fromPos, isMoving);

        if (world.isBlockPowered(blockPos)) {
            this.onPlayerDestroy(world, blockPos, blockState.with(PRIMED, true));
            world.removeBlock(blockPos, false);
        }
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos blockPos, Explosion explosion) {
        if (!world.isRemote()) {
            EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed(world,
                    (double)((float)blockPos.getX() + 0.5F), (double)((float)blockPos.getY() + 0.5F),
                    (double)((float)blockPos.getZ() + 0.5F), explosion.getExplosivePlacedBy());
            entityprimed.setFuse(world.rand.nextInt(entityprimed.getFuse() / 4) + entityprimed.getFuse() / 8);
            world.addEntity(entityprimed);
            world.removeBlock(blockPos, false);
        }
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState) {
        super.onPlayerDestroy(world, blockPos, blockState);
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
        if (!world.isRemote()) {
            if (blockState.get(PRIMED)) {
                EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed((World) world,
                        (float)blockPos.getX() + 0.5F, (float)blockPos.getY() + 0.5F,
                        (float)blockPos.getZ() + 0.5F, placer);
                world.addEntity(entityprimed);
                world.playSound(null, blockPos, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_) {
        if (!player.getHeldItem(hand).isEmpty() &&
                (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL || player.getHeldItem(hand).getItem() == Items.FIRE_CHARGE)) {
            this.primeBomb(world, blockPos, getDefaultState().with(PRIMED, true), player);
            world.removeBlock(blockPos, false);
            player.getHeldItem(hand).damageItem(1, player, (e) -> {});
            return ActionResultType.SUCCESS;
        } else {
            return super.onBlockActivated(state, world, blockPos, player, hand, p_225533_6_);
        }
    }

    @Override
    public void onEntityWalk(World world, BlockPos blockPos, Entity entity) {
        BlockState primedState = getDefaultState().with(PRIMED, true);
        if (entity instanceof AbstractArrowEntity && !world.isRemote()) {
            AbstractArrowEntity entityarrow = (AbstractArrowEntity)entity;

            if (entityarrow.isBurning()) {
                this.primeBomb(world, blockPos, primedState, entityarrow.getShooter() instanceof LivingEntity ? (LivingEntity)entityarrow.getShooter() : null);
                world.removeBlock(blockPos, false);
            }
        } else if (entity instanceof EntityLightningGrenade && !world.isRemote()) {
            EntityLightningGrenade entitygrenade = (EntityLightningGrenade)entity;

            this.primeBomb(world, blockPos, primedState, entitygrenade.getThrower() != null ? entitygrenade.getThrower() : null);
            world.removeBlock(blockPos, false);
        }
    }
    
    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

}
