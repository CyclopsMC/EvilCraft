package evilcraft.block;

import evilcraft.Configs;
import evilcraft.core.IInformationProvider;
import evilcraft.core.block.IBlockTank;
import evilcraft.core.block.component.BlockTankComponent;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import evilcraft.fluid.Blood;
import evilcraft.fluid.BloodConfig;
import evilcraft.tileentity.TileDarkTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;

/**
 * A tank that can hold liquids.
 * @author rubensworks
 *
 */
public class DarkTank extends ConfigurableBlockContainer implements IInformationProvider, IBlockTank {
	
	private static final String NBT_TAG_CAPACITY = "tankCapacity";

	public static final PropertyBool DRAINING = PropertyBool.create("draining");
	
    private static DarkTank _instance = null;
    
    private BlockTankComponent<DarkTank> tankComponent = new BlockTankComponent<DarkTank>(this);
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkTank getInstance() {
        return _instance;
    }

    public DarkTank(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.glass, TileDarkTank.class);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DRAINING, false));
        this.setHardness(0.5F);
        this.setStepSound(soundTypeGlass);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.125F, 0.001F, 0.125F, 0.875F, 0.999F, 0.875F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos blockPos) {
        setBlockBoundsForItemRender();
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState blockState, AxisAlignedBB area, List list, Entity entity) {
        setBlockBounds(0, 0, 0, 1, 1, 1);
        super.addCollisionBoxesToList(world, blockPos, blockState, area, list, entity);
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
            return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
    	TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(blockPos);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }
    
    @Override
    public boolean isOpaqueCube() {
    	return false;
    }
    
    @Override
    public boolean isNormalCube() {
    	return false;
    }
    
    /*@Override
    public int getRenderBlockPass() {
    	return 1;
    }*/
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos blockPos, EnumFacing side) {
    	return true;
    }
    
    @Override
    public int getRenderType() {
        // TODO
        //return RenderDarkTank.ID;
        return -1;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumFacing side, float motionX, float motionY, float motionZ) {
    	if(tankComponent.onBlockActivatedTank(world, blockPos, player, side, motionX, motionY, motionZ)) {
        	return true;
        } else {
        	world.setBlockState(blockPos, this.blockState.getBaseState().withProperty(DRAINING, !(Boolean)blockState.getValue(DRAINING)), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
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
	public int getLightValue(IBlockAccess world, BlockPos blockPos) {
		TileEntity tile = world.getTileEntity(blockPos);
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
            	activated.setItemDamage(1 - activated.getItemDamage());
            	return activated;
            }
            return itemStack;
		}
		return itemStack;
	}

	@Override
	public boolean isActivated(ItemStack itemStack, World world, Entity entity) {
		return itemStack.getItemDamage() == 1;
	}
	
	@Override
	public int damageDropped(IBlockState blockState) {
		return (Boolean) blockState.getValue(DRAINING) ? 1 : 0;
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
                if (fluid != null && fluid != Blood.getInstance()) {
                    try {
                        ItemStack itemStackFilled = itemStack.copy();
                        setTankCapacity(itemStackFilled, capacityOriginal);
                        IFluidContainerItem container = (IFluidContainerItem) itemStackFilled.getItem();
                        container.fill(itemStackFilled, new FluidStack(fluid, capacity), true);
                        list.add(itemStackFilled);
                    } catch (NullPointerException e) {
                        // Skip registering tanks for invalid fluids.
                    }
                }
            }
        }
    }

}
