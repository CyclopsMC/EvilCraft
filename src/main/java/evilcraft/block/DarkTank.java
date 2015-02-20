package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Configs;
import evilcraft.client.render.block.RenderDarkTank;
import evilcraft.core.IInformationProvider;
import evilcraft.core.block.IBlockTank;
import evilcraft.core.block.component.BlockTankComponent;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import evilcraft.fluid.Blood;
import evilcraft.fluid.BloodConfig;
import evilcraft.tileentity.TileDarkTank;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

/**
 * A tank that can hold liquids.
 * @author rubensworks
 *
 */
public class DarkTank extends ConfigurableBlockContainer implements IInformationProvider, IBlockTank {
	
	private static final String NBT_TAG_CAPACITY = "tankCapacity";
	
	/**
	 * Meta data for draining.
	 */
	public static final int META_DRAINING = 1;
	
    private static DarkTank _instance = null;
    
    private BlockTankComponent<DarkTank> tankComponent = new BlockTankComponent<DarkTank>(this); 
    
    protected IIcon sideIcon;
    protected IIcon topIcon;
    protected IIcon sideIconActive;
    protected IIcon topIconActive;
    
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
    }

    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.125F, 0.001F, 0.125F, 0.875F, 0.999F, 0.875F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        setBlockBoundsForItemRender();
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB area, List list, Entity entity) {
        setBlockBounds(0, 0, 0, 1, 1, 1);
        super.addCollisionBoxesToList(world, x, y, z, area, list, entity);
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
            return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
    	TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(x, y, z);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    	sideIcon = iconRegister.registerIcon(getTextureName() + "_side");
    	topIcon = iconRegister.registerIcon(getTextureName() + "_top");
    	sideIconActive = iconRegister.registerIcon(getTextureName() + "_side_active");
    	topIconActive = iconRegister.registerIcon(getTextureName() + "_top_active");
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
    	ForgeDirection direction = ForgeDirection.getOrientation(side);
        if(direction == ForgeDirection.UP || direction == ForgeDirection.DOWN) {
        	return meta == META_DRAINING ? topIconActive : topIcon;
        }
        return meta == META_DRAINING ? sideIconActive : sideIcon;
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
        } else {
        	int meta = world.getBlockMetadata(x, y, z);
        	world.setBlockMetadataWithNotify(x, y, z, 1 - meta, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        	return true;
        }
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
		NBTTagCompound tag = itemStack.getTagCompound();
		if(tag == null || !tag.hasKey(NBT_TAG_CAPACITY)) {
			return TileDarkTank.BASE_CAPACITY;
		}
		return tag.getInteger(NBT_TAG_CAPACITY);
	}
	
	@Override
	public void setTankCapacity(ItemStack itemStack, int capacity) {
		NBTTagCompound tag = itemStack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			itemStack.setTagCompound(tag);
		}
		setTankCapacity(tag, capacity);
	}
	
	@Override
	public void setTankCapacity(NBTTagCompound tag, int capacity) {
		tag.setInteger(NBT_TAG_CAPACITY, capacity);
	}
	
	@Override
	public void writeAdditionalInfo(TileEntity tile, NBTTagCompound tag) {
    	super.writeAdditionalInfo(tile, tag);
    	tankComponent.writeAdditionalInfo(tile, tag);
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

	@Override
	public int getMaxCapacity() {
		return DarkTankConfig.maxTankSize;
	}
	
	@Override
	public boolean isActivatable() {
		return true;
	}
	
	@Override
	public ItemStack toggleActivation(ItemStack itemStack, World world, EntityPlayer player) {
		if(player.isSneaking()) {
            if(!world.isRemote) {
            	ItemStack activated = itemStack.copy();
            	activated.setItemDamage(DarkTank.META_DRAINING - activated.getItemDamage());
            	return activated;
            }
            return itemStack;
		}
		return itemStack;
	}

	@Override
	public boolean isActivated(ItemStack itemStack, World world, Entity entity) {
		return itemStack.getItemDamage() == DarkTank.META_DRAINING;
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        ItemStack itemStack = new ItemStack(item);

        int capacityOriginal = TileDarkTank.BASE_CAPACITY;
		int capacity = capacityOriginal;
		do{
            setTankCapacity(itemStack, capacity);
        	list.add(itemStack.copy());
        	if(Configs.isEnabled(BloodConfig.class)) {
        		ItemStack itemStackFilled = itemStack.copy();
        		IFluidContainerItem container = (IFluidContainerItem) itemStackFilled.getItem();
        		container.fill(itemStackFilled, new FluidStack(Blood.getInstance(), capacity), true);
        		list.add(itemStackFilled);
        	}
        	capacity = capacity << 2;
        } while(capacity << 2 < DarkTankConfig.maxTankSize);

        // Add filled basic tanks for all fluids.
        if(DarkTankConfig.creativeTabFluids) {
            for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
                if (fluid != Blood.getInstance()) {
                    ItemStack itemStackFilled = itemStack.copy();
                    setTankCapacity(itemStackFilled, capacityOriginal);
                    IFluidContainerItem container = (IFluidContainerItem) itemStackFilled.getItem();
                    container.fill(itemStackFilled, new FluidStack(fluid, capacity), true);
                    list.add(itemStackFilled);
                }
            }
        }
    }

}
