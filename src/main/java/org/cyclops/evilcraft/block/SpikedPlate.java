package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockBasePressurePlate;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import org.cyclops.evilcraft.tileentity.TileSanguinaryPedestal;

import java.util.List;

/**
 * Logs for the Undead Tree.
 * @author rubensworks
 *
 */
public class SpikedPlate extends ConfigurableBlockBasePressurePlate {

    private static SpikedPlate _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpikedPlate getInstance() {
        return _instance;
    }

    public SpikedPlate(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return super.canPlaceBlockAt(world, blockPos) ||
                world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == SanguinaryPedestal.getInstance();
    }

    @Override
    protected void playClickOnSound(World worldIn, BlockPos color) {

    }

    @Override
    protected void playClickOffSound(World worldIn, BlockPos pos) {

    }

    /**
     * Damage the given entity.
     * @param world The world.
     * @param entity The entity.
     * @param blockPos The position.
     * @return If the given entity was damaged.
     */
    protected boolean damageEntity(World world, Entity entity, BlockPos blockPos) {
    	if(!(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase) {
    		float damage = (float) SpikedPlateConfig.damage;
    		
    		// To make sure the entity actually will drop something.
    		ObfuscationHelpers.setRecentlyHit(((EntityLivingBase) entity), 100);
    		
    		if(entity.attackEntityFrom(ExtendedDamageSource.spiked, damage)) {
	    		TileEntity tile = world.getTileEntity(blockPos.add(0, -1, 0));
	    		if(tile != null && tile instanceof TileSanguinaryPedestal) {
	    			int amount = MathHelper.floor_float(damage * (float) SpikedPlateConfig.mobMultiplier);
	    			((TileSanguinaryPedestal) tile).fillWithPotentialBonus(new FluidStack(TileSanguinaryPedestal.FLUID, amount));
	    		}
	    		return true;
    		}
    	}
    	return false;
    }

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	protected int computeRedstoneStrength(World world, BlockPos blockPos) {
        AxisAlignedBB axisalignedbb = this.getBoundingBox(world.getBlockState(blockPos), world, blockPos);
		List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb.offset(blockPos));

        int ret = 0;

		if(list != null && !list.isEmpty()) {
            for(EntityLivingBase entity : list) {
                if(!entity.doesEntityNotTriggerPressurePlate() && damageEntity(world, entity, blockPos)) {
                    ret = 15;
                }
            }
        }

        return ret;
	}

    protected int getRedstoneStrength(IBlockState blockState) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    protected IBlockState setRedstoneStrength(IBlockState blockState, int meta) {
        return blockState.withProperty(POWERED, meta > 0);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        boolean flag = this.getRedstoneStrength(blockState) > 0;
        float offset = 0F;
        float f = 0.0775F;

        TileEntity tile = world.getTileEntity(blockPos.add(0, -1, 0));
        if(tile != null && tile instanceof TileSanguinaryPedestal) {
            offset = -0.025F;
        }

        if (flag) {
            return new AxisAlignedBB(f, offset, f, 1.0F - f, 0.03125F + offset, 1.0F - f);
        } else {
            return new AxisAlignedBB(f, offset, f, 1.0F - f, 0.0625F + offset, 1.0F - f);
        }
    }
	
	@Override
	public boolean canCreatureSpawn(IBlockState blockState, IBlockAccess world, BlockPos blockPos, EntityLiving.SpawnPlacementType type) {
		return true;
    }

}
