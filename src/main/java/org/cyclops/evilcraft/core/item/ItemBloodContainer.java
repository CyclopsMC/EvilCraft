package org.cyclops.evilcraft.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.cyclopscore.item.DamageIndicatedItemFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ItemBloodContainer extends DamageIndicatedItemFluidContainer {

    protected boolean canPickUp = true;
    private boolean placeFluids = false;

    public ItemBloodContainer(Properties builder, int capacity) {
        super(builder, capacity, () -> RegistryEntries.FLUID_BLOOD);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(itemStack).orElse(null);
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
        FluidStack drained = fluidHandler.drain(FluidHelpers.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);

        boolean hasBucket = !drained.isEmpty()
                && (drained.getAmount() == FluidHelpers.BUCKET_VOLUME);
        boolean hasSpace = fluidStack.isEmpty()
                || (fluidStack.getAmount() + FluidHelpers.BUCKET_VOLUME <= fluidHandler.getCapacity());
        BlockHitResult movingobjectpositionDrain = (BlockHitResult) this.getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
        BlockHitResult movingobjectpositionFill = (BlockHitResult) this.getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);

        if (movingobjectpositionDrain != null && movingobjectpositionFill != null) {
            if (isPickupFluids() && movingobjectpositionFill.getType() == HitResult.Type.BLOCK) {
                // Fill the container and remove fluid blockState
                BlockPos blockPos = movingobjectpositionFill.getBlockPos();

                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.getBlock() instanceof LiquidBlock
                        && ((LiquidBlock) blockState.getBlock()).getFluid() == getFluid()
                        && blockState.getValue(LiquidBlock.LEVEL) == 0) {
                    if(hasSpace) {
                        world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                        fluidHandler.fill(new FluidStack(getFluid(), FluidHelpers.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
                    }
                    return MinecraftHelpers.successAction(itemStack);
                }
            }

            // Drain container and place fluid blockState
            if (hasBucket && isPlaceFluids() && movingobjectpositionDrain.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = movingobjectpositionFill.getBlockPos();

                Direction direction = movingobjectpositionDrain.getDirection();
                blockPos = blockPos.offset(direction.getNormal());

                if (this.tryPlaceContainedLiquid(world, blockPos, true)) {
                    fluidHandler.drain(FluidHelpers.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                    return MinecraftHelpers.successAction(itemStack);
                }
            }
        }
        return MinecraftHelpers.successAction(itemStack);
    }

    private boolean tryPlaceContainedLiquid(Level world, BlockPos blockPos, boolean hasBucket) {
        if (!hasBucket) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(blockPos);

            if (!world.isEmptyBlock(blockPos) && blockState.isSolid()) {
                return false;
            } else {
                if (!world.isClientSide && !blockState.isSolid() && !blockState.liquid()) {
                    world.destroyBlock(blockPos, true);
                }

                world.setBlock(blockPos, getFluid().getFluidType().getBlockForFluidState(world, blockPos, getFluid().defaultFluidState()), 3);

                return true;
            }
        }
    }

    /**
     * If this item can place fluids when right-clicking (non-sneaking).
     * The fluid will only be placed if the container has at least 1000 mB inside of it
     * and will drain that accordingly.
     * @return If it can place fluids.
     */
    public boolean isPlaceFluids() {
        return placeFluids;
    }

    /**
     * If this item can pick up fluids when right-clicking (non-sneaking).
     * @return If it can pick up fluids.
     */
    public boolean isPickupFluids() {
        return canPickUp;
    }

    /**
     * Set whether or not this item should be able to place fluids in the world
     * when right-clicking (non-sneaking).
     * The fluid will only be placed if the container has at least 1000 mB inside of it
     * and will drain that accordingly.
     * @param placeFluids If it can place fluids.
     */
    public void setPlaceFluids(boolean placeFluids) {
        this.placeFluids = placeFluids;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader world, BlockPos pos, Player player) {
        return true;
    }

    protected FluidStack drainFromOthers(int amount, ItemStack itemStack, Fluid fluid, Player player, IFluidHandler.FluidAction action) {
        PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
        FluidStack drained = FluidStack.EMPTY;
        while(it.hasNext() && amount > 0) {
            ItemStack current = it.next();
            IFluidHandler containerItem = FluidUtil.getFluidHandler(current).orElse(null);
            if(!current.isEmpty() && current != itemStack && containerItem != null) {
                FluidStack totalFluid = FluidUtil.getFluidContained(current).orElse(FluidStack.EMPTY);
                if(!totalFluid.isEmpty() && totalFluid.getFluid() == fluid) {
                    FluidStack thisDrained = containerItem.drain(amount, action);
                    if (!thisDrained.isEmpty() && thisDrained.getFluid() == fluid) {
                        if (drained.isEmpty()) {
                            drained = thisDrained;
                        } else {
                            drained.setAmount(drained.getAmount() + thisDrained.getAmount());
                        }
                        amount -= drained.getAmount();
                    }
                }
            }
        }
        if(!drained.isEmpty() && drained.isEmpty()) {
            drained = FluidStack.EMPTY;
        }
        return drained;
    }

    /**
     * If this container can consume a given fluid amount.
     * Will also check other containers inside the player inventory.
     * @param amount The amount to drain.
     * @param itemStack The fluid container.
     * @param player The player.
     * @return If the given amount can be drained.
     */
    public boolean canConsume(int amount, ItemStack itemStack, @Nullable Player player) {
        if(canDrain(amount, itemStack)) return true;
        int availableAmount = FluidUtil.getFluidContained(itemStack).map(FluidStack::getAmount).orElse(0);
        return player != null && !drainFromOthers(amount - availableAmount, itemStack, getFluid(), player, IFluidHandler.FluidAction.SIMULATE).isEmpty();
    }

    /**
     * Consume a given fluid amount.
     * Will also check other containers inside the player inventory.
     * @param amount The amount to drain.
     * @param itemStack The fluid container.
     * @param player The player.
     * @return The fluid that was drained.
     */
    public FluidStack consume(int amount, ItemStack itemStack, @Nullable Player player) {
        IFluidHandler.FluidAction fluidAction = player == null || (!player.isCreative() && !player.level().isClientSide) ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE;
        if (amount == 0) return FluidStack.EMPTY;
        FluidStack drained = FluidUtil.getFluidHandler(itemStack).orElseGet(null).drain(amount, fluidAction);
        if (!drained.isEmpty() && drained.getAmount() == amount) return drained;
        int drainedAmount = drained.getAmount();
        int toDrain = amount - drainedAmount;
        FluidStack otherDrained = player == null ? FluidStack.EMPTY : drainFromOthers(toDrain, itemStack, getFluid(), player, fluidAction);
        if (otherDrained.isEmpty()) return drained;
        otherDrained.setAmount(otherDrained.getAmount() + drainedAmount);
        return otherDrained;
    }
}
