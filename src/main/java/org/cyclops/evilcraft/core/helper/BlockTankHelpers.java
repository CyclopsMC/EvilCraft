package org.cyclops.evilcraft.core.helper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacityConfig;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.item.DamageIndicatedItemComponent;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.fluid.SimulatedFluidStack;
import org.cyclops.evilcraft.core.tileentity.TankInventoryTileEntity;

import javax.annotation.Nonnull;

/**
 * Helpers related to blocks with tanks.
 * @author rubensworks
 */
public class BlockTankHelpers {

    static {
        MinecraftForge.EVENT_BUS.register(new BlockTankHelpers());
    }

    private BlockTankHelpers() {

    }

    public static boolean onBlockActivatedTank(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack itemStack = player.getHeldItem(hand);
        TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(blockPos);
        if(tile != null) {
            if(!itemStack.isEmpty()) {
                SimulatableTankWrapper tank = new SimulatableTankWrapper(tile.getTank());
                IFluidHandler itemFluidHandler = FluidUtil.getFluidHandler(itemStack).orElse(null);
                if(!player.isCrouching() && !tank.isFull() && itemFluidHandler != null
                        && FluidUtil.tryEmptyContainer(itemStack, tank, FluidHelpers.BUCKET_VOLUME, player, false).isSuccess()) { // Fill the tank.
                    FluidActionResult result = FluidUtil.tryEmptyContainer(itemStack, tank, FluidHelpers.BUCKET_VOLUME, player, true);
                    if (result.isSuccess() && !player.isCreative()) {
                        InventoryHelpers.tryReAddToStack(player, itemStack, result.getResult(), hand);
                    }
                    return true;
                } else if(player.isCrouching() && !tank.isEmpty()
                        && FluidUtil.tryFillContainer(itemStack, tank, FluidHelpers.BUCKET_VOLUME, player, false).isSuccess()) { // Drain the tank.
                    FluidActionResult result = FluidUtil.tryFillContainer(itemStack, tank, FluidHelpers.BUCKET_VOLUME, player, true);
                    if (result.isSuccess() && !player.isCreative()) {
                        InventoryHelpers.tryReAddToStack(player, itemStack, result.getResult(), hand);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get info for a given itemStack.
     * @param itemStack The itemStack that must be given information.
     * @return Information for that itemStack.
     */
    @OnlyIn(Dist.CLIENT)
    public static ITextComponent getInfoTank(ItemStack itemStack) {
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
        int amount = fluidStack.getAmount();
        IFluidHandlerItemCapacity fluidHandlerItemCapacity = (IFluidHandlerItemCapacity) FluidUtil.getFluidHandler(itemStack);
        return DamageIndicatedItemComponent.getInfo(fluidStack, amount, fluidHandlerItemCapacity.getCapacity());
    }

    /**
     * Convert fluid capabilities of tile to item.
     * @param tile The tile that has already been removed from the world.
     * @param itemStack The input itemstack.
     * @return The resulting itemstack.
     */
    public static ItemStack tileDataToItemStack(TileEntity tile, ItemStack itemStack) {
        IFluidHandler fluidHandlerTile = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
        if (fluidHandlerTile != null) {
            IFluidHandlerItemCapacity fluidHandlerItemCapacity = itemStack.getCapability(FluidHandlerItemCapacityConfig.CAPABILITY).orElse(null);
            if (fluidHandlerItemCapacity != null) {
                if (fluidHandlerTile instanceof IFluidTank) {
                    IFluidTank fluidTank = (IFluidTank) fluidHandlerTile;
                    fluidHandlerItemCapacity.setCapacity(fluidTank.getCapacity());
                    itemStack = fluidHandlerItemCapacity.getContainer();
                }
            }
            IFluidHandlerItem fluidHandlerItem = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElse(null);
            if (fluidHandlerItem != null) {
                FluidActionResult res = FluidUtil.tryFillContainer(itemStack, fluidHandlerTile, Integer.MAX_VALUE, null, true);
                if (res.isSuccess()) {
                    itemStack = res.getResult();
                }
            }
        }
        return itemStack;
    }

    /**
     * Convert fluid capabilities of item to tile.
     * @param itemStack The itemstack.
     * @param tile The tile that has already been removed from the world.
     */
    public static void itemStackDataToTile(ItemStack itemStack, TileEntity tile) {
        IFluidHandler fluidHandlerTile = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
        if (fluidHandlerTile != null) {
            IFluidHandlerItemCapacity fluidHandlerItemCapacity = itemStack.getCapability(FluidHandlerItemCapacityConfig.CAPABILITY).orElse(null);
            if (fluidHandlerItemCapacity != null) {
                if (fluidHandlerTile instanceof FluidTank) {
                    FluidTank fluidTank = (FluidTank) fluidHandlerTile;
                    fluidTank.setCapacity(fluidHandlerItemCapacity.getCapacity());
                }
            }

            IFluidHandlerItem fluidHandlerItem = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElse(null);
            if (fluidHandlerItem != null) {
                FluidUtil.tryEmptyContainer(itemStack, fluidHandlerTile, Integer.MAX_VALUE, null, true);
            }
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Force allow shift-right clicking with a fluid container passing through to this block
        if (!event.getItemStack().isEmpty()
                && event.getItemStack().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()
                && event.getWorld().getBlockState(event.getPos()).getBlock() instanceof IBlockTank) {
            event.setUseBlock(Event.Result.ALLOW);
        }
    }

    public static class SimulatableTankWrapper implements IFluidHandler {

        private final SingleUseTank tank;

        public SimulatableTankWrapper(SingleUseTank tank) {
            this.tank = tank;
        }

        @Override
        public int getTanks() {
            return this.tank.getTanks();
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return this.tank.getFluidInTank(tank);
        }

        @Override
        public int getTankCapacity(int tank) {
            return this.tank.getTankCapacity(tank);
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return this.tank.isFluidValid(tank, stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return tank.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            FluidStack drained = tank.drain(resource, action);
            return action.execute() ? drained : new SimulatedFluidStack(drained.getFluid(), drained.getAmount());
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack drained = tank.drain(maxDrain, action);
            return action.execute() ? drained : new SimulatedFluidStack(drained.getFluid(), drained.getAmount());
        }

        public boolean isFull() {
            return tank.isFull();
        }

        public boolean isEmpty() {
            return tank.isEmpty();
        }
    }


}
