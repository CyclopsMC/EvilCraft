package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.core.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.tileentity.TileDarkTank;

import java.util.List;

/**
 * A tank that can hold liquids.
 * @author rubensworks
 *
 */
public class BlockDarkTank extends BlockTile implements IBlockTank {

	public static final String NBT_KEY_DRAINING = "enabled";

	// Model Properties
	public static final ModelProperty<FluidStack> TANK_FLUID = new ModelProperty<>();
	public static final ModelProperty<Integer> TANK_CAPACITY = new ModelProperty<>();

	public static final VoxelShape SHAPE = Block.makeCuboidShape(0.125F * 16F, 0.001F * 16F, 0.125F * 16F, 0.875F * 16F, 0.999F * 16F, 0.875F * 16F);

    public BlockDarkTank(Block.Properties properties) {
        super(properties, TileDarkTank::new);

        MinecraftForge.EVENT_BUS.register(this);
    }

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

    @Override
    public boolean hasComparatorInputOverride(BlockState blockState) {
            return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos blockPos) {
    	TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(blockPos);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
		return false;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
		if (FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, Direction.UP)) {
			return ActionResultType.SUCCESS;
		} else if (!player.isCrouching()) {
			TileHelpers.getSafeTile(worldIn, pos, TileDarkTank.class)
					.ifPresent(tile -> tile.setEnabled(!tile.isEnabled()));
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, p_225533_6_);
	}

	/*
	@Override
	protected ItemStack tileDataToItemStack(CyclopsTileEntity tile, ItemStack itemStack) {
		return BlockTankHelpers.tileDataToItemStack(tile, itemStack);
	}*/ // TODO: loot tables


	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return TileHelpers.getSafeTile(world, pos, TileDarkTank.class)
				.map(tile -> tile.getTank().getFluidType() != null
						? (int) Math.min(15, tile.getFillRatio() * tile.getTank().getFluidType()
							.getAttributes().getLuminosity(tile.getTank().getFluid()) * 15)
						: 0)
				.orElse(0);
	}

	@Override
	public int getDefaultCapacity() {
		return TileDarkTank.BASE_CAPACITY;
	}

	@Override
	public boolean isActivatable() {
		return true;
	}
	
	@Override
	public ItemStack toggleActivation(ItemStack itemStack, World world, PlayerEntity player) {
		if(player.isCrouching()) {
            if(!world.isRemote()) {
            	ItemStack activated = itemStack.copy();
            	if (isActivated(itemStack, world)) {
					activated.getOrCreateTag().remove(NBT_KEY_DRAINING);
					if (activated.getTag().isEmpty()) {
						activated.setTag(null);
					}
				} else {
					activated.getOrCreateTag().putBoolean(NBT_KEY_DRAINING, !isActivated(itemStack, world));
				}
            	return activated;
            }
            return itemStack;
		}
		return itemStack;
	}

	@Override
	public boolean isActivated(ItemStack itemStack, World world) {
		return itemStack.hasTag() && itemStack.getTag().getBoolean(NBT_KEY_DRAINING);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> list) {
		ItemStack itemStack = new ItemStack(this);

		int capacityOriginal = TileDarkTank.BASE_CAPACITY;
		int capacity = capacityOriginal;
		int lastCapacity;
		do{
			IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(itemStack.copy()).orElse(null);
			fluidHandler.setCapacity(capacity);
			list.add(fluidHandler.getContainer().copy());
			fluidHandler.fill(new FluidStack(RegistryEntries.FLUID_BLOOD, capacity), IFluidHandler.FluidAction.EXECUTE);
			list.add(fluidHandler.getContainer().copy());
			lastCapacity = capacity;
			capacity = capacity << 2;
		} while(capacity < Math.min(BlockDarkTankConfig.maxTankCreativeSize, BlockDarkTankConfig.maxTankSize) && capacity > lastCapacity);

		// Add filled basic tanks for all fluids.
		if(BlockDarkTankConfig.creativeTabFluids) {
			for (Fluid fluid : ForgeRegistries.FLUIDS.getValues()) {
				if (fluid != RegistryEntries.FLUID_BLOOD) {
					try {
						ItemStack itemStackFilled = itemStack.copy();
						IFluidHandlerItemCapacity fluidHandlerFilled = FluidHelpers.getFluidHandlerItemCapacity(itemStackFilled).orElse(null);
						fluidHandlerFilled.setCapacity(capacityOriginal);
						fluidHandlerFilled.fill(new FluidStack(fluid, capacityOriginal), IFluidHandler.FluidAction.EXECUTE);
						list.add(fluidHandlerFilled.getContainer());
					} catch (NullPointerException e) {
						// Skip registering tanks for invalid fluids.
					}
				}
			}
		}
	}

    // TODO: handle model
	/*@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onModelRegistryEvent(ModelRegistryEvent event) {
		// Handle additional type of dark tank item rendering
		for (int meta = 0; meta < 2; meta++) {
			Item item = Item.getItemFromBlock(this);
			String modId = getConfig().getMod().getModId();
			String itemName = getConfig().getModelName(new ItemStack(item, 1, meta));
			ModelResourceLocation modelResourceLocation = new ModelResourceLocation(modId + ":" + itemName, "inventory");
			ModelBakery.registerItemVariants(item, modelResourceLocation);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
        // Take the original item tank model and replace it with a dynamic one but pass this original one to it as parent.
        for(ModelResourceLocation itemModel : itemModels) {
			IBakedModel baseModel = Objects.requireNonNull(event.getModelRegistry().getObject(itemModel), "Could not find the item model for " + itemModel);
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
	public BlockState getExtendedState(BlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) super.getExtendedState(state, world, pos);
		TileDarkTank tile = TileHelpers.getSafeTile(world, pos, TileDarkTank.class);
		if(tile != null) {
			FluidStack fluidStack = tile.getTank().getFluid();
			if(fluidStack != null) {
				extendedBlockState = extendedBlockState.with(TANK_FLUID, fluidStack);
			}
			extendedBlockState = extendedBlockState.with(TANK_CAPACITY, tile.getTank().getCapacity());
		}
		return extendedBlockState;
	}*/

}
