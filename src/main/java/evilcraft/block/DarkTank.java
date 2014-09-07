package evilcraft.block;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.render.block.RenderDarkTank;
import evilcraft.core.IInformationProvider;
import evilcraft.core.block.IBlockTank;
import evilcraft.core.block.component.BlockTankComponent;
import evilcraft.core.config.BlockConfig;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.tileentity.TileDarkTank;

/**
 * A tank that can hold liquids.
 * @author rubensworks
 *
 */
public class DarkTank extends ConfigurableBlockContainer implements IInformationProvider, IBlockTank {
	
    private static DarkTank _instance = null;
    
    private BlockTankComponent<DarkTank> tankComponent = new BlockTankComponent<DarkTank>(this); 
    
    protected IIcon sideIcon;
    protected IIcon topIcon;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkTank(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkTank getInstance() {
        return _instance;
    }

    private DarkTank(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.glass, TileDarkTank.class);
        
        this.setHardness(0.5F);
        this.setStepSound(soundTypeGlass);
        this.setBlockBounds(0.125F, 0F, 0.125F, 0.875F, 1F, 0.875F);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    	sideIcon = iconRegister.registerIcon(getTextureName() + "_side");
    	topIcon = iconRegister.registerIcon(getTextureName() + "_top");
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
    	ForgeDirection direction = ForgeDirection.getOrientation(side);
        if(direction == ForgeDirection.UP || direction == ForgeDirection.DOWN) {
        	return topIcon;
        }
        return sideIcon;
    }
    
    @Override
    public boolean isOpaqueCube() {
    	return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
    	return false;
    }
    
    @Override
    public int getRenderBlockPass() {
    	return 1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
    	return true;
    }
    
    @Override
    public int getRenderType() {
        return RenderDarkTank.ID;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float motionX, float motionY, float motionZ) {
    	if(tankComponent.onBlockActivatedTank(world, x, y, z, player, side, motionX, motionY, motionZ)) {
        	return true;
        }
    	return super.onBlockActivated(world, x, y, z, player, side, motionX, motionY, motionZ);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return tankComponent.getInfoTank(itemStack);
    }

	@SuppressWarnings("rawtypes")
	@Override
	public void provideInformation(ItemStack itemStack,
			EntityPlayer entityPlayer, List list, boolean par4) {
		
	}

	@Override
	public String getTankNBTName() {
		return TileDarkTank.NBT_TAG_TANK;
	}

	@Override
	public int getTankCapacity(ItemStack itemStack) {
		return TileDarkTank.BASE_CAPACITY << itemStack.getItemDamage();
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileDarkTank) {
			TileDarkTank tank = (TileDarkTank) tile;
			if(tank.getTank().getFluidType() != null) {
				return (int) Math.min(15, tank.getFillRatio() * tank.getTank().getFluidType().getLuminosity() * 15);
			}
		}
		return 0;
	}

}
