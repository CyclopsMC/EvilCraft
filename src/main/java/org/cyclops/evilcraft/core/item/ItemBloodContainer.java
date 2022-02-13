package org.cyclops.evilcraft.core.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
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

import net.minecraft.item.Item.Properties;

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
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(itemStack).orElse(null);
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
        FluidStack drained = fluidHandler.drain(FluidHelpers.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);

        boolean hasBucket = !drained.isEmpty()
                && (drained.getAmount() == FluidHelpers.BUCKET_VOLUME);
        boolean hasSpace = fluidStack.isEmpty()
                || (fluidStack.getAmount() + FluidHelpers.BUCKET_VOLUME <= fluidHandler.getCapacity());
        BlockRayTraceResult movingobjectpositionDrain = (BlockRayTraceResult) this.getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.NONE);
        BlockRayTraceResult movingobjectpositionFill = (BlockRayTraceResult) this.getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

        if (movingobjectpositionDrain != null && movingobjectpositionFill != null) {
            if (isPickupFluids() && movingobjectpositionFill.getType() == RayTraceResult.Type.BLOCK) {
                // Fill the container and remove fluid blockState
                BlockPos blockPos = movingobjectpositionFill.getBlockPos();

                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.getBlock() instanceof FlowingFluidBlock
                        && ((FlowingFluidBlock) blockState.getBlock()).getFluid() == getFluid()
                        && blockState.getValue(FlowingFluidBlock.LEVEL) == 0) {
                    if(hasSpace) {
                        world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                        fluidHandler.fill(new FluidStack(getFluid(), FluidHelpers.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
                    }
                    return MinecraftHelpers.successAction(itemStack);
                }
            }

            // Drain container and place fluid blockState
            if (hasBucket && isPlaceFluids() && movingobjectpositionDrain.getType() == RayTraceResult.Type.BLOCK) {
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

    private boolean tryPlaceContainedLiquid(World world, BlockPos blockPos, boolean hasBucket) {
        if (!hasBucket) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(blockPos);
            Material material = blockState.getMaterial();

            if (!world.isEmptyBlock(blockPos) && material.isSolid()) {
                return false;
            } else {
                if (!world.isClientSide && !material.isSolid() && !material.isLiquid()) {
                    world.destroyBlock(blockPos, true);
                }

                world.setBlock(blockPos, getFluid().getAttributes().getBlock(world, blockPos, getFluid().defaultFluidState()), 3);

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
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        return ActionResultType.PASS;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
        return true;
    }

    protected FluidStack drainFromOthers(int amount, ItemStack itemStack, Fluid fluid, PlayerEntity player, IFluidHandler.FluidAction action) {
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
    public boolean canConsume(int amount, ItemStack itemStack, @Nullable PlayerEntity player) {
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
    public FluidStack consume(int amount, ItemStack itemStack, @Nullable PlayerEntity player) {
        IFluidHandler.FluidAction fluidAction = player == null || (!player.isCreative() && !player.level.isClientSide) ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE;
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
