package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.config.configurable.BlockPressurePlate;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import org.cyclops.evilcraft.tileentity.TileSanguinaryPedestal;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Logs for the Undead Tree.
 * @author rubensworks
 *
 */
public class BlockSpikedPlate extends BlockPressurePlate {

    public BlockSpikedPlate(Block.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos blockPos) {
        return super.isValidPosition(state, world, blockPos)
                || world.getBlockState(blockPos.add(0, -1, 0)).getBlock() instanceof BlockSanguinaryPedestal;
    }

    @Override
    protected void playClickOnSound(IWorld worldIn, BlockPos color) {

    }

    @Override
    protected void playClickOffSound(IWorld worldIn, BlockPos pos) {

    }

    /**
     * Damage the given entity.
     * @param world The world.
     * @param entity The entity.
     * @param blockPos The position.
     * @return If the given entity was damaged.
     */
    protected boolean damageEntity(ServerWorld world, Entity entity, BlockPos blockPos) {
    	if(!(entity instanceof PlayerEntity) && entity instanceof LivingEntity) {
    		float damage = (float) BlockSpikedPlateConfig.damage;
    		
    		// To make sure the entity actually will drop something.
            ((LivingEntity) entity).recentlyHit = 100;

    		if(entity.attackEntityFrom(ExtendedDamageSource.spikedDamage(world), damage)) {
	    		TileEntity tile = world.getTileEntity(blockPos.add(0, -1, 0));
	    		if(tile != null && tile instanceof TileSanguinaryPedestal) {
	    			int amount = MathHelper.floor(damage * (float) BlockSpikedPlateConfig.mobMultiplier);
	    			((TileSanguinaryPedestal) tile).getBonusFluidHandler()
                            .fill(new FluidStack(RegistryEntries.FLUID_BLOOD, amount), IFluidHandler.FluidAction.EXECUTE);
	    		}
	    		return true;
    		}
    	}
    	return false;
    }

	@Override
	protected int computeRedstoneStrength(World world, BlockPos blockPos) {
        VoxelShape shape = this.getShape(world.getBlockState(blockPos), world, blockPos, ISelectionContext.dummy());
		List<LivingEntity> list = world.getEntitiesWithinAABB(LivingEntity.class, shape.getBoundingBox().expand(0, 1, 1).offset(blockPos));

        int ret = 0;

		if (!world.isRemote() && !list.isEmpty()) {
            for (LivingEntity entity : list) {
                    if (!entity.doesEntityNotTriggerPressurePlate() && damageEntity((ServerWorld) world, entity, blockPos)) {
                    ret = 15;
                }
            }
        }

        return ret;
	}

    protected int getRedstoneStrength(BlockState blockState) {
        return BlockHelpers.getSafeBlockStateProperty(blockState, POWERED, false) ? 15 : 0;
    }

    protected BlockState setRedstoneStrength(BlockState blockState, int meta) {
        return blockState.with(POWERED, meta > 0);
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return true;
    }

}
