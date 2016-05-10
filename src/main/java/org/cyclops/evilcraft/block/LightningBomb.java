package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.entity.block.EntityLightningBombPrimed;
import org.cyclops.evilcraft.entity.item.EntityLightningGrenade;

import java.util.Random;

/**
 * A bomb that spawns lightning when ignited.
 * @author rubensworks
 *
 */
public class LightningBomb extends ConfigurableBlock {
    
    private static LightningBomb _instance = null;

    @BlockProperty
    public static final PropertyBool PRIMED = PropertyBool.create("primed");
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static LightningBomb getInstance() {
        return _instance;
    }

    public LightningBomb(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.tnt);
        this.setHardness(0.0F);
        this.setStepSound(SoundType.GROUND);
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState blockState) {
        super.onBlockAdded(world, blockPos, blockState);

        if (world.isBlockPowered(blockPos)) {
            this.onBlockDestroyedByPlayer(world, blockPos, blockState.withProperty(PRIMED, true));
            world.setBlockToAir(blockPos);
        }
    }
    
    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState blockState, Block neighbour) {
        if (world.isBlockPowered(blockPos)) {
            this.onBlockDestroyedByPlayer(world, blockPos, blockState.withProperty(PRIMED, true));
            world.setBlockToAir(blockPos);
        }
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos blockPos, Explosion explosion) {
        if (!world.isRemote) {
            EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed(world,
                    (double)((float)blockPos.getX() + 0.5F), (double)((float)blockPos.getY() + 0.5F),
                    (double)((float)blockPos.getZ() + 0.5F), explosion.getExplosivePlacedBy());
            entityprimed.setFuse(world.rand.nextInt(entityprimed.getFuse() / 4) + entityprimed.getFuse() / 8);
            world.spawnEntityInWorld(entityprimed);
        }
    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState blockState) {
        this.primeBomb(world, blockPos, blockState, null);
    }
    
    /**
     * Spawns the primed bomb and plays the fuse sound.
     * @param world The world.
     * @param blockPos The position.
     * @param blockState The state.
     * @param placer The entity that primed the bomb.
     */
    public void primeBomb(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase placer) {
        if (!world.isRemote) {
            if ((Boolean) blockState.getValue(PRIMED)) {
                EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed(world,
                        (double)((float)blockPos.getX() + 0.5F), (double)((float)blockPos.getY() + 0.5F),
                        (double)((float)blockPos.getZ() + 0.5F), placer);
                world.spawnEntityInWorld(entityprimed);
                world.playSound(null, blockPos, SoundEvents.entity_tnt_primed, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float coordX, float coordY, float coordZ) {
        if (player.getActiveItemStack() != null &&
                (player.getActiveItemStack().getItem() == Items.flint_and_steel || player.getActiveItemStack().getItem() == Items.fire_charge)) {
            this.primeBomb(world, blockPos, this.blockState.getBaseState().withProperty(PRIMED, true), player);
            world.setBlockToAir(blockPos);
            player.getActiveItemStack().damageItem(1, player);
            return true;
        } else {
            return super.onBlockActivated(world, blockPos, blockState, player, hand, heldItem, side, coordX, coordY, coordZ);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity) {
        IBlockState primedState = this.blockState.getBaseState().withProperty(PRIMED, true);
        if (entity instanceof EntityArrow && !world.isRemote) {
            EntityArrow entityarrow = (EntityArrow)entity;

            if (entityarrow.isBurning()) {
                this.primeBomb(world, blockPos, primedState, entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
                world.setBlockToAir(blockPos);
            }
        } else if (entity instanceof EntityLightningGrenade && !world.isRemote) {
            EntityLightningGrenade entitygrenade = (EntityLightningGrenade)entity;

            this.primeBomb(world, blockPos, primedState, entitygrenade.getThrower() != null ? entitygrenade.getThrower() : null);
            world.setBlockToAir(blockPos);
        }
    }
    
    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

}
