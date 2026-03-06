package org.cyclops.evilcraft.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.inventory.ItemAccessItemLocation;
import org.cyclops.cyclopscore.inventory.ItemLocation;
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
        super(builder, capacity, RegistryEntries.FLUID_BLOOD::get);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        IFluidHandlerCapacity fluidHandler = IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(ItemAccess.forStack(itemStack)).orElse(null);
        FluidStack fluidStack = FluidUtil.getFirstStackContained(itemStack);
        int drained;
        try (var tx = Transaction.openRoot()) {
            if (fluidStack.isEmpty()) {
                drained = 0;
            } else {
                drained = fluidHandler.extract(FluidResource.of(fluidStack), IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume(), tx);
            }
        }

        boolean hasBucket = drained == IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume();
        boolean hasSpace = fluidStack.isEmpty()
                || (fluidStack.getAmount() + IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume() <= fluidHandler.getTankCapacity(0));
        BlockHitResult movingobjectpositionDrain = (BlockHitResult) this.getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
        BlockHitResult movingobjectpositionFill = (BlockHitResult) this.getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);

        if (movingobjectpositionDrain != null && movingobjectpositionFill != null) {
            if (isPickupFluids() && movingobjectpositionFill.getType() == HitResult.Type.BLOCK) {
                // Fill the container and remove fluid blockState
                BlockPos blockPos = movingobjectpositionFill.getBlockPos();

                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.getBlock() instanceof LiquidBlock
                        && ((LiquidBlock) blockState.getBlock()).fluid == getFluid()
                        && blockState.getValue(LiquidBlock.LEVEL) == 0) {
                    if(hasSpace) {
                        world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                        try (var tx = Transaction.openRoot()) {
                            fluidHandler.insert(FluidResource.of(getFluid()), IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume(), tx);
                            tx.commit();
                        }
                    }
                    return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
                }
            }

            // Drain container and place fluid blockState
            if (hasBucket && isPlaceFluids() && movingobjectpositionDrain.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = movingobjectpositionFill.getBlockPos();

                Direction direction = movingobjectpositionDrain.getDirection();
                blockPos = blockPos.offset(direction.getUnitVec3i());

                if (this.tryPlaceContainedLiquid(world, blockPos, true)) {
                    try (var tx = Transaction.openRoot()) {
                        fluidHandler.extract(FluidResource.of(fluidStack), IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume(), tx);
                        tx.commit();
                    }
                    return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
                }
            }
        }
        return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
    }

    private boolean tryPlaceContainedLiquid(Level world, BlockPos blockPos, boolean hasBucket) {
        if (!hasBucket) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(blockPos);

            if (!world.isEmptyBlock(blockPos) && blockState.isSolid()) {
                return false;
            } else {
                if (!world.isClientSide() && !blockState.isSolid() && !blockState.liquid()) {
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

    @Override
    public boolean canDrain(int amount, ItemStack itemStack) {
        // Avoid IllegalArgumentException when the fluid handler has no fluid (empty resource is not allowed in NeoForge extract calls)
        FluidStack fluidStack = FluidUtil.getFirstStackContained(itemStack);
        if (fluidStack.isEmpty()) return false;
        return super.canDrain(amount, itemStack);
    }

    protected FluidStack drainFromOthers(int amount, ItemStack itemStack, Fluid fluid, Player player, TransactionContext transaction) {
        PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
        int drained = 0;
        while(it.hasNext() && amount > 0) {
            ItemLocation current = it.nextIndexed();
            ResourceHandler<FluidResource> containerItem = new ItemAccessItemLocation(player, current).getCapability(Capabilities.Fluid.ITEM);
            if(current.getItemStack(player) != itemStack && containerItem != null) {
                int thisDrained = containerItem.extract(FluidResource.of(fluid), amount, transaction);
                if (thisDrained > 0) {
                    drained += thisDrained;
                    amount -= thisDrained;
                }
            }
        }
        return new FluidStack(fluid, drained);
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
        int availableAmount = FluidUtil.getFirstStackContained(itemStack).getAmount();
        try (var tx = Transaction.openRoot()) {
        return player != null && !drainFromOthers(amount - availableAmount, itemStack, getFluid(), player, tx).isEmpty();
        }
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
        if (amount == 0) return FluidStack.EMPTY;
        try (var tx = Transaction.openRoot()) {
            ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
            FluidResource resource = fluidHandler.getResource(0);
            // Avoid IllegalArgumentException when the fluid handler has no fluid (empty resource is not allowed in NeoForge extract calls)
            int drained = resource.isEmpty() ? 0 : fluidHandler.extract(resource, amount, tx);
            if (drained == amount) return resource.toStack(drained);
            int toDrain = amount - drained;
            FluidStack otherDrained = player == null ? FluidStack.EMPTY : drainFromOthers(toDrain, itemStack, getFluid(), player, tx);
            if (otherDrained.isEmpty()) return resource.toStack(drained);
            otherDrained.setAmount(otherDrained.getAmount() + drained);
            if (player == null || (!player.isCreative() && !player.level().isClientSide())) {
                tx.commit();
            }
            return otherDrained;
        }
    }
}
