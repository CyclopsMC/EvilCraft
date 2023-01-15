package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Chalice that can be bound to other chalices which causes them to always share the same fluid amount.
 * Can be filled or drained in blockState mode, and can be used to auto-supply ALL slots in a player inventory.
 * @author rubensworks
 *
 */
public class BlockEntangledChalice extends BlockWithEntity implements IInformationProvider, IBlockTank, IBlockRarityProvider {

    public static final BooleanProperty DRAINING = BooleanProperty.create("draining");

    // Model Properties
    public static final ModelProperty<String> TANK_ID = new ModelProperty<>();
    public static final ModelProperty<FluidStack> TANK_FLUID = new ModelProperty<>();

    public static final VoxelShape SHAPE = Block.box(0.125F * 16F, 0F, 0.125F * 16F, 0.875F * 16F, 1.0F * 16F, 0.875F * 16F);

    public BlockEntangledChalice(Block.Properties properties) {
        super(properties, BlockEntityEntangledChalice::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DRAINING, false));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_ENTANGLED_CHALICE, new BlockEntityEntangledChalice.TickerServer());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DRAINING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(DRAINING, false);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (FluidUtil.interactWithFluidHandler(player, hand, world, blockPos, Direction.UP)) {
            return InteractionResult.SUCCESS;
        }
        if (world.isClientSide()) {
            String tankId = BlockEntityHelpers.get(world, blockPos, BlockEntityEntangledChalice.class)
                    .map(BlockEntityEntangledChalice::getWorldTankId)
                    .orElse("null");
            player.displayClientMessage(Component.translatable(L10NHelpers.localize(
                    "block.evilcraft.entangled_chalice.info.id", ItemEntangledChalice.tankIdToNameParts(tankId))), true);
        }
        return super.use(state, world, blockPos, player, hand, rayTraceResult);
    }

    @Override
    public MutableComponent getInfo(ItemStack itemStack) {
        return BlockTankHelpers.getInfoTank(itemStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void provideInformation(ItemStack itemStack, Level world, List<Component> list, TooltipFlag iTooltipFlag) {

    }

    @Override
    public int getDefaultCapacity() {
        return BlockEntityEntangledChalice.BASE_CAPACITY;
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
    public boolean isActivated(ItemStack itemStack, Level world) {
        return ItemHelpers.isActivated(itemStack);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos blockPos) {
        BlockEntity tile = world.getBlockEntity(blockPos);
        if(tile != null && tile instanceof BlockEntityEntangledChalice) {
            BlockEntityEntangledChalice tank = (BlockEntityEntangledChalice) tile;
            Fluid fluidType = tank.getTank().getFluidType();
            if(fluidType != null) {
                return (int) Math.min(15, tank.getFillRatio() * fluidType.getFluidType().getLightLevel());
            }
        }
        return 0;
    }

    public void fillItemCategory(NonNullList<ItemStack> list) {
        // Can be null during startup
        if (ForgeCapabilities.FLUID_HANDLER_ITEM != null) {
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
