package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.ExtendedDamageSource;
import evilcraft.core.config.configurable.ConfigurableBlockBasePressurePlate;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import evilcraft.tileentity.TileSanguinaryPedestal;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Logs for the Undead Tree.
 * @author rubensworks
 *
 */
public class SpikedPlate extends ConfigurableBlockBasePressurePlate {
    
    private static SpikedPlate _instance = null;
    
    @SideOnly(Side.CLIENT)
    protected IIcon blockIconSide;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new SpikedPlate(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpikedPlate getInstance() {
        return _instance;
    }

    private SpikedPlate(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    	blockIconSide = iconRegister.registerIcon(getTextureName() + "_side");
    	super.registerBlockIcons(iconRegister);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if(ForgeDirection.getOrientation(side) == ForgeDirection.UP) {
        	return super.getIcon(side, meta);
        }
        return blockIconSide;
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) || world.getBlock(x, y - 1, z) == SanguinaryPedestal.getInstance();
    }
    
    /**
     * Damage the given entity.
     * @param world The world.
     * @param entity The entity.
     * @param x X
     * @param y Y
     * @param z Z
     * @return If the given entity was damaged.
     */
    protected boolean damageEntity(World world, Entity entity, int x, int y, int z) {
    	if(!(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase) {
    		float damage = (float) SpikedPlateConfig.damage;
    		
    		// To make sure the entity actually will drop something.
    		ObfuscationHelpers.setRecentlyHit(((EntityLivingBase) entity), 100);
    		
    		if(entity.attackEntityFrom(ExtendedDamageSource.spiked, damage)) {
	    		TileEntity tile = world.getTileEntity(x, y - 1, z);
	    		if(tile != null && tile instanceof TileSanguinaryPedestal) {
	    			int amount = MathHelper.floor_float(damage * (float) SpikedPlateConfig.mobMultiplier);
	    			((TileSanguinaryPedestal) tile).fillWithPotentialBonus(new FluidStack(TileSanguinaryPedestal.FLUID, amount));
	    		}
	    		return true;
    		}
    	}
    	return false;
    }

	@SuppressWarnings("rawtypes")
	@Override
	protected int func_150065_e(World world, int x,
			int y, int z) {
		List list = world.getEntitiesWithinAABBExcludingEntity(null, this.func_150061_a(x, y, z));
		
		int ret = 0;
		
		if(list != null && !list.isEmpty()) {
            for(Entity entity : (List<Entity>) list) {
                if(!entity.doesEntityNotTriggerPressurePlate() && damageEntity(world, entity, x, y, z)) {
                    ret = 15;
                }
            }
        }

        return ret;
	}

	@Override
	protected int func_150060_c(int meta) {
		return meta == 1 ? 15 : 0;
	}

	@Override
	protected int func_150066_d(int meta) {
		return meta > 0 ? 1 : 0;
	}
	
	@Override
	protected void setMetaBlockBounds(IBlockAccess world, int x, int y, int z, int meta) {
        boolean flag = this.func_150060_c(meta) > 0;
        float offset = 0F;
        float f = 0.0775F;
        
        TileEntity tile = world.getTileEntity(x, y - 1, z);
		if(tile != null && tile instanceof TileSanguinaryPedestal) {
			offset = -0.025F;
		}

        if (flag) {
            this.setBlockBounds(f, offset, f, 1.0F - f, 0.03125F + offset, 1.0F - f);
        } else {
            this.setBlockBounds(f, offset, f, 1.0F - f, 0.0625F + offset, 1.0F - f);
        }
    }
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return true;
    }

}
