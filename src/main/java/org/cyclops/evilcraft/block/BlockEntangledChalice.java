package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.IFormattableTextComponent;
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
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
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

	public static final BooleanProperty DRAINING = BooleanProperty.create("draining");

	// Model Properties
	public static final ModelProperty<String> TANK_ID = new ModelProperty<>();
	public static final ModelProperty<FluidStack> TANK_FLUID = new ModelProperty<>();

	public static final VoxelShape SHAPE = Block.box(0.125F * 16F, 0F, 0.125F * 16F, 0.875F * 16F, 1.0F * 16F, 0.875F * 16F);

    public BlockEntangledChalice(Block.Properties properties) {
        super(properties, TileEntangledChalice::new);

		this.registerDefaultState(this.stateDefinition.any()
				.setValue(DRAINING, false));
    }

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(DRAINING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(DRAINING, false);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		if (FluidUtil.interactWithFluidHandler(player, hand, world, blockPos, Direction.UP)) {
			return ActionResultType.SUCCESS;
		}
		if (world.isClientSide()) {
			String tankId = TileHelpers.getSafeTile(world, blockPos, TileEntangledChalice.class)
					.map(TileEntangledChalice::getWorldTankId)
					.orElse("null");
			player.displayClientMessage(new TranslationTextComponent(L10NHelpers.localize(
					"block.evilcraft.entangled_chalice.info.id", ItemEntangledChalice.tankIdToNameParts(tankId))), true);
		}
		return super.use(state, world, blockPos, player, hand, rayTraceResult);
	}
    
    @Override
    public IFormattableTextComponent getInfo(ItemStack itemStack) {
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
            if(!world.isClientSide()) {
				ItemStack activated = itemStack.copy();
				if (isActivated(itemStack, world)) {
					activated.getOrCreateTag().remove(ItemHelpers.NBT_KEY_ENABLED);
					if (activated.getTag().isEmpty()) {
						activated.setTag(null);
					}
				} else {
					activated.getOrCreateTag().putBoolean(ItemHelpers.NBT_KEY_ENABLED, !isActivated(itemStack, world));
				}
				return activated;
            }
            return itemStack;
		}
		return itemStack;
	}

	@Override
	public boolean isActivated(ItemStack itemStack, World world) {
		return ItemHelpers.isActivated(itemStack);
	}

	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos blockPos) {
		TileEntity tile = world.getBlockEntity(blockPos);
		if(tile != null && tile instanceof TileEntangledChalice) {
			TileEntangledChalice tank = (TileEntangledChalice) tile;
			Fluid fluidType = tank.getTank().getFluidType();
			if(fluidType != null) {
				return (int) Math.min(15, tank.getFillRatio() * fluidType.getAttributes().getLuminosity());
			}
		}
		return 0;
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> list) {
		// Can be null during startup
		if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
			ItemStack itemStack = new ItemStack(this);
			ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStack).orElse(null);
			fluidHandler.setTankID("creative");
			list.add(itemStack);
		}
	}

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return Rarity.RARE;
    }

}
