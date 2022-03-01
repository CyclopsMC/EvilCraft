package org.cyclops.evilcraft.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.block.BlockWithEntityGui;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTankInventory;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Tank block with gui.
 * @author rubensworks
 *
 */
public abstract class BlockWithEntityGuiTank extends BlockWithEntityGui implements IInformationProvider, IBlockTank {

    public BlockWithEntityGuiTank(Properties properties, BiFunction<BlockPos, BlockState, CyclopsBlockEntity> blockEntitySupplier) {
        super(properties, blockEntitySupplier);
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
    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (FluidUtil.interactWithFluidHandler(player, hand, world, blockPos, Direction.UP)) {
            return InteractionResult.SUCCESS;
        }
        return super.use(blockState, world, blockPos, player, hand, rayTraceResult);
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
    public boolean isActivatable() {
        return false;
    }

    @Override
    public ItemStack toggleActivation(ItemStack itemStack, Level world, Player player) {
        return itemStack;
    }

    @Override
    public boolean isActivated(ItemStack itemStack, Level world) {
        return false;
    }

    @Override
    public void onRemove(BlockState oldState, Level world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide() && oldState.getBlock() != newState.getBlock()) {
            BlockEntityHelpers.get(world, blockPos, BlockEntityTankInventory.class)
                    .ifPresent(tile -> InventoryHelpers.dropItems(world, tile.getInventory(), blockPos));
        }
        super.onRemove(oldState, world, blockPos, newState, isMoving);
    }
}
