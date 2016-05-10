package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.UnlistedProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.model.ModelDarkTank;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.block.component.BlockTankComponent;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.fluid.BloodConfig;
import org.cyclops.evilcraft.tileentity.TileDarkTank;

import java.util.List;

/**
 * A tank that can hold liquids.
 * @author rubensworks
 *
 */
public class DarkTank extends ConfigurableBlockContainer implements IInformationProvider, IBlockTank {

	private static final String NBT_TAG_CAPACITY = "tankCapacity";

	@BlockProperty
	public static final PropertyBool DRAINING = PropertyBool.create("draining");
	@BlockProperty
	public static final IUnlistedProperty<FluidStack> TANK_FLUID = new UnlistedProperty<FluidStack>("tank_fluidstack", FluidStack.class);
	@BlockProperty
	public static final IUnlistedProperty<Integer> TANK_CAPACITY = new UnlistedProperty<Integer>("tank_capacity", Integer.class);
	
    private static DarkTank _instance = null;
    
    private BlockTankComponent<DarkTank> tankComponent = new BlockTankComponent<DarkTank>(this);
    @SideOnly(Side.CLIENT)
    private ModelResourceLocation[] itemModels;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkTank getInstance() {
        return _instance;
    }

    public DarkTank(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.glass, TileDarkTank.class);
        this.setHardness(0.5F);
        this.setStepSound(SoundType.GLASS);
        MinecraftForge.EVENT_BUS.register(this);
        if(MinecraftHelpers.isClientSide()) {
            itemModels = new ModelResourceLocation[2];
            itemModels[0] = new ModelResourceLocation(eConfig.getMod().getModId() + ":" + eConfig.getNamedId() + "_off", "inventory");
            itemModels[1] = new ModelResourceLocation(eConfig.getMod().getModId() + ":" + eConfig.getNamedId() + "_on", "inventory");
        }
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.125F, 0.001F, 0.125F, 0.875F, 0.999F, 0.875F);
	}

    @Override
    public boolean hasComparatorInputOverride(IBlockState blockState) {
            return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos blockPos) {
    	TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(blockPos);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
    	return false;
    }
    
    @Override
    public boolean isNormalCube(IBlockState blockState) {
    	return false;
    }

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos blockPos, EnumFacing side) {
    	return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float motionX, float motionY, float motionZ) {
    	if(tankComponent.onBlockActivatedTank(world, blockPos, player, hand, heldItem, side, motionX, motionY, motionZ)) {
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
	public int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
		TileDarkTank tile = TileHelpers.getSafeTile(world, blockPos, TileDarkTank.class);
		if(tile != null) {
			if(tile.getTank().getFluidType() != null) {
				return (int) Math.min(15, tile.getFillRatio() * tile.getTank().getFluidType().getLuminosity() * 15);
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
        int lastCapacity;
		do{
            setTankCapacity(itemStack, capacity);
        	list.add(itemStack.copy());
        	if(Configs.isEnabled(BloodConfig.class)) {
        		ItemStack itemStackFilled = itemStack.copy();
        		IFluidContainerItem container = (IFluidContainerItem) itemStackFilled.getItem();
        		container.fill(itemStackFilled, new FluidStack(Blood.getInstance(), capacity), true);
        		list.add(itemStackFilled);
        	}
            lastCapacity = capacity;
        	capacity = capacity << 2;
        } while(capacity < Math.min(DarkTankConfig.maxTankCreativeSize, DarkTankConfig.maxTankSize) && capacity > lastCapacity);

        // Add filled basic tanks for all fluids.
        if(DarkTankConfig.creativeTabFluids) {
            for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
                if (fluid != null && fluid != Blood.getInstance()) {
                    try {
                        ItemStack itemStackFilled = itemStack.copy();
                        setTankCapacity(itemStackFilled, capacityOriginal);
                        IFluidContainerItem container = (IFluidContainerItem) itemStackFilled.getItem();
                        container.fill(itemStackFilled, new FluidStack(fluid, capacityOriginal), true);
                        list.add(itemStackFilled);
                    } catch (NullPointerException e) {
                        // Skip registering tanks for invalid fluids.
                    }
                }
            }
        }
    }

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/**
	 * Called for baking the model of the item version of the tank.
	 * @param event The bake event.
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
        // Take the original item tank model and replace it with a dynamic one but pass this original one to it as parent.
        for(ModelResourceLocation itemModel : itemModels) {
            IBakedModel baseModel = event.getModelRegistry().getObject(itemModel);
            ModelDarkTank newModel = new ModelDarkTank(baseModel);
            event.getModelRegistry().putObject(itemModel, newModel);
        }

		// Do the same for block models
		if(DarkTankConfig.staticBlockRendering) {
			ModelResourceLocation blockDrainingOf = new ModelResourceLocation(Reference.MOD_ID + ":darkTank", "draining=false");
			ModelResourceLocation blockDrainingOn = new ModelResourceLocation(Reference.MOD_ID + ":darkTank", "draining=true");
			event.getModelRegistry().putObject(blockDrainingOf, new ModelDarkTank(event.getModelRegistry().getObject(blockDrainingOf)));
			event.getModelRegistry().putObject(blockDrainingOn, new ModelDarkTank(event.getModelRegistry().getObject(blockDrainingOn)));
		}
    }

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) super.getExtendedState(state, world, pos);
		TileDarkTank tile = TileHelpers.getSafeTile(world, pos, TileDarkTank.class);
		if(tile != null) {
			FluidStack fluidStack = tile.getTank().getFluid();
			if(fluidStack != null) {
				extendedBlockState = extendedBlockState.withProperty(TANK_FLUID, fluidStack);
			}
			extendedBlockState = extendedBlockState.withProperty(TANK_CAPACITY, tile.getTank().getCapacity());
		}
		return extendedBlockState;
	}

}
