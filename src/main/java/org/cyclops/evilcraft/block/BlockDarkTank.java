package org.cyclops.evilcraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTankInventory;
import org.cyclops.evilcraft.core.helper.ItemHelpers;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * A tank that can hold liquids.
 * @author rubensworks
 *
 */
public class BlockDarkTank extends BlockWithEntity implements IBlockTank {

    public static final MapCodec<BlockDarkTank> CODEC = simpleCodec(BlockDarkTank::new);

    // Model Properties
    public static final ModelProperty<FluidStack> TANK_FLUID = new ModelProperty<>();
    public static final ModelProperty<Integer> TANK_CAPACITY = new ModelProperty<>();

    public static final VoxelShape SHAPE = Block.box(0.125F * 16F, 0.001F * 16F, 0.125F * 16F, 0.875F * 16F, 0.999F * 16F, 0.875F * 16F);

    public BlockDarkTank(Block.Properties properties) {
        super(properties, BlockEntityDarkTank::new);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_DARK_TANK.get(), new BlockEntityDarkTank.TickerServer());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
            return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos blockPos) {
        BlockEntityTankInventory tile = (BlockEntityTankInventory) world.getBlockEntity(blockPos);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return false;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult p_225533_6_) {
        if (FluidUtil.interactWithFluidHandler(player, player.getUsedItemHand(), worldIn, pos, Direction.UP)) {
            return InteractionResult.SUCCESS;
        } else if (!player.isCrouching()) {
            BlockEntityHelpers.get(worldIn, pos, BlockEntityDarkTank.class)
                    .ifPresent(tile -> {
                        tile.setEnabled(!tile.isEnabled());
                        player.displayClientMessage(Component.literal(String.format(Locale.ROOT, "%,d", tile.getTank().getFluidAmount()))
                                .append(" / ")
                                .append(String.format(Locale.ROOT, "%,d", tile.getTank().getCapacity()))
                                .append(" mB"), true);
                    });
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, worldIn, pos, player, p_225533_6_);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return BlockEntityHelpers.get(world, pos, BlockEntityDarkTank.class)
                .map(tile -> tile.getTank().getFluidType() != null
                        ? (int) Math.min(15, tile.getFillRatio() * tile.getTank().getFluidType()
                            .getFluidType().getLightLevel(tile.getTank().getFluid()) * 15)
                        : 0)
                .orElse(0);
    }

    @Override
    public int getDefaultCapacity() {
        return BlockEntityDarkTank.BASE_CAPACITY;
    }

    @Override
    public boolean isActivatable() {
        return true;
    }

    @Override
    public ItemStack toggleActivation(ItemStack itemStack, Level world, Player player) {
        if(player.isCrouching()) {
            if(!world.isClientSide()) {
                ItemStack activated = itemStack.copy();
                ItemHelpers.toggleActivation(activated);
                return activated;
            }
            return itemStack;
        }
        return itemStack;
    }

    @Override
    public boolean isActivated(ItemStack itemStack, Item.TooltipContext context) {
        return ItemHelpers.isActivated(itemStack);
    }

    public void fillItemCategory(NonNullList<ItemStack> list) {
        ItemStack itemStack = new ItemStack(this);

        int capacityOriginal = BlockEntityDarkTank.BASE_CAPACITY;
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
        } while(capacity < BlockDarkTankConfig.maxTankCreativeSize && capacity > lastCapacity);

        // Add filled basic tanks for all fluids.
        if(BlockDarkTankConfig.creativeTabFluids) {
            for (Fluid fluid : BuiltInRegistries.FLUID) {
                if (fluid != RegistryEntries.FLUID_BLOOD.get() && fluid.isSource(fluid.defaultFluidState())) {
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

}
