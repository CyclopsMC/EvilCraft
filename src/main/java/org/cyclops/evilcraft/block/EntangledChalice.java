package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.UnlistedProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

import java.util.List;

/**
 * Chalice that can be bound to other chalices which causes them to always share the same fluid amount.
 * Can be filled or drained in blockState mode, and can be used to auto-supply ALL slots in a player inventory.
 * @author rubensworks
 *
 */
public class EntangledChalice extends ConfigurableBlockContainer implements IInformationProvider, IBlockTank, IBlockRarityProvider {
	
    private static EntangledChalice _instance = null;

	@BlockProperty
	public static final IUnlistedProperty<String> TANK_ID = new UnlistedProperty<String>("tank_id", String.class);
	@BlockProperty
	public static final IUnlistedProperty<FluidStack> TANK_FLUID = new UnlistedProperty<FluidStack>("tank_fluidstack", FluidStack.class);
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EntangledChalice getInstance() {
        return _instance;
    }

    public EntangledChalice(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.IRON, TileEntangledChalice.class);
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.125F, 0F, 0.125F, 0.875F, 1.0F, 0.875F);
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
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing side, float motionX, float motionY, float motionZ) {
    	return BlockTankHelpers.onBlockActivatedTank(world, blockPos, player, hand, side, motionX, motionY, motionZ) ||
                super.onBlockActivated(world, blockPos, blockState, player, hand, side, motionX, motionY, motionZ);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return BlockTankHelpers.getInfoTank(itemStack);
    }

	@SuppressWarnings("rawtypes")
	@Override
	public void provideInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {

	}
	
	@Override
	public int getDefaultCapacity() {
		return TileEntangledChalice.BASE_CAPACITY;
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
	public boolean isActivated(ItemStack itemStack, World world) {
		return itemStack.getItemDamage() == 1;
	}
	
	@Override
	public int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
		TileEntity tile = world.getTileEntity(blockPos);
		if(tile != null && tile instanceof TileEntangledChalice) {
			TileEntangledChalice tank = (TileEntangledChalice) tile;
			if(tank.getTank().getFluidType() != null) {
				return (int) Math.min(15, tank.getFillRatio() * tank.getTank().getFluidType().getLuminosity());
			}
		}
		return 0;
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (!BlockHelpers.isValidCreativeTab(this, tab)) return;
        ItemStack itemStack = new ItemStack(this);
		EntangledChaliceItem.FluidHandler fluidHandler = (EntangledChaliceItem.FluidHandler) FluidUtil.getFluidHandler(itemStack);
		fluidHandler.setTankID("creativeTank0");
        list.add(itemStack);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.RARE;
    }

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) super.getExtendedState(state, world, pos);
		TileEntangledChalice tile = TileHelpers.getSafeTile(world, pos, TileEntangledChalice.class);
		if(tile != null) {
			FluidStack fluidStack = tile.getTank().getFluid();
			extendedBlockState = extendedBlockState.withProperty(TANK_ID, ((WorldSharedTank) tile.getTank()).getTankID());
			if(fluidStack != null) {
				extendedBlockState = extendedBlockState.withProperty(TANK_FLUID, fluidStack);
			}
		}
		return extendedBlockState;
	}

	@Override
	protected ItemStack tileDataToItemStack(CyclopsTileEntity tile, ItemStack itemStack) {
		itemStack = super.tileDataToItemStack(tile, itemStack);
		// Convert tank id
		EntangledChaliceItem.FluidHandler fluidHandler = (EntangledChaliceItem.FluidHandler) FluidUtil.getFluidHandler(itemStack);
		String tankId = fluidHandler.getTankID();
		((TileEntangledChalice) tile).setWorldTankId(tankId);

		return itemStack;
	}
}
