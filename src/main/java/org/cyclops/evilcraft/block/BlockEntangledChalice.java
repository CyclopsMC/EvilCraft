package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.item.ItemEntangledChalice;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

import java.util.List;

/**
 * Chalice that can be bound to other chalices which causes them to always share the same fluid amount.
 * Can be filled or drained in blockState mode, and can be used to auto-supply ALL slots in a player inventory.
 * @author rubensworks
 *
 */
public class BlockEntangledChalice extends BlockTile implements IInformationProvider, IBlockTank, IBlockRarityProvider {

	// Model Properties
	public static final ModelProperty<String> TANK_ID = new ModelProperty<>();
	public static final ModelProperty<FluidStack> TANK_FLUID = new ModelProperty<>();

	public static final VoxelShape SHAPE = Block.makeCuboidShape(0.125F * 16F, 0F, 0.125F * 16F, 0.875F * 16F, 1.0F * 16F, 0.875F * 16F);

	private final boolean enabled;

    public BlockEntangledChalice(Block.Properties properties, boolean enabled) {
        super(properties, TileEntangledChalice::new);
        this.enabled = enabled;
    }

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		if (BlockTankHelpers.onBlockActivatedTank(state, world, blockPos, player, hand, rayTraceResult)) {
			return ActionResultType.SUCCESS;
		}
		if (world.isRemote()) {
			String tankId = TileHelpers.getSafeTile(world, blockPos, TileEntangledChalice.class)
					.map(TileEntangledChalice::getWorldTankId)
					.orElse("null");
			player.sendStatusMessage(new TranslationTextComponent(L10NHelpers.localize(
					"tile.blocks.evilcraft.entangled_chalice.info.id", tankId)), true);
		}
		return super.onBlockActivated(state, world, blockPos, player, hand, rayTraceResult);
	}
    
    @Override
    public ITextComponent getInfo(ItemStack itemStack) {
        return BlockTankHelpers.getInfoTank(itemStack);
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public void provideInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {

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
	public ItemStack toggleActivation(ItemStack itemStack, World world, PlayerEntity player) {
		if(player.isCrouching()) {
            if(!world.isRemote()) {
				ItemStack activated;
				if (itemStack.getItem() == RegistryEntries.ITEM_ENTANGLED_CHALICE) {
					activated = new ItemStack(RegistryEntries.ITEM_ENTANGLED_CHALICE_ON, itemStack.getCount());
				} else {
					activated = new ItemStack(RegistryEntries.ITEM_ENTANGLED_CHALICE, itemStack.getCount());
				}
				activated.setTag(itemStack.getTag());
				return activated;
            }
            return itemStack;
		}
		return itemStack;
	}

	@Override
	public boolean isActivated(ItemStack itemStack, World world) {
		return enabled;
	}

	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos blockPos) {
		TileEntity tile = world.getTileEntity(blockPos);
		if(tile != null && tile instanceof TileEntangledChalice) {
			TileEntangledChalice tank = (TileEntangledChalice) tile;
			if(tank.getTank().getFluidType() != null) {
				return (int) Math.min(15, tank.getFillRatio() * tank.getTank().getFluidType().getAttributes().getLuminosity());
			}
		}
		return 0;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> list) {
		if (enabled) {
			return;
		}

		// Can be null during startup
		if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
			ItemStack itemStack = new ItemStack(this);
			ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStack).orElse(null);
			fluidHandler.setTankID("creativeTank0");
			list.add(itemStack);
		}
	}

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return Rarity.RARE;
    }

    /*
	@Override
	public BlockState getExtendedState(BlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) super.getExtendedState(state, world, pos);
		TileEntangledChalice tile = TileHelpers.getSafeTile(world, pos, TileEntangledChalice.class);
		if(tile != null) {
			FluidStack fluidStack = tile.getTank().getFluid();
			extendedBlockState = extendedBlockState.with(TANK_ID, ((WorldSharedTank) tile.getTank()).getTankID());
			if(fluidStack != null) {
				extendedBlockState = extendedBlockState.with(TANK_FLUID, fluidStack);
			}
		}
		return extendedBlockState;
	}
	TODO: models
     */

	/*
	@Override
	protected ItemStack tileDataToItemStack(CyclopsTileEntity tile, ItemStack itemStack) {
		itemStack = super.tileDataToItemStack(tile, itemStack);
		// Convert tank id
		String tankId = ((TileEntangledChalice) tile).getWorldTankId();
		EntangledChaliceItem.FluidHandler fluidHandler = (EntangledChaliceItem.FluidHandler) FluidUtil.getFluidHandler(itemStack);
		fluidHandler.setTankID(tankId);

		return itemStack;
		TODO: loot tables
	}*/
}
