package org.cyclops.evilcraft.core.helper;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.cyclops.cyclopscore.Capabilities;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.item.DamageIndicatedItemComponent;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.fluid.SimulatedFluidStack;

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

    /**
     * Get info for a given itemStack.
     * @param itemStack The itemStack that must be given information.
     * @return Information for that itemStack.
     */
    @OnlyIn(Dist.CLIENT)
    public static MutableComponent getInfoTank(ItemStack itemStack) {
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
        int amount = fluidStack.getAmount();
        int capacity = FluidUtil.getFluidHandler(itemStack)
                .map(handler -> ((IFluidHandlerItemCapacity) handler).getCapacity())
                .orElse(0);
        return DamageIndicatedItemComponent.getInfo(fluidStack, amount, capacity)
                .withStyle(IInformationProvider.ITEM_PREFIX);
    }

    /**
     * Convert fluid capabilities of tile to item.
     * @param tile The tile that has already been removed from the world.
     * @param itemStack The input itemstack.
     * @return The resulting itemstack.
     */
    public static ItemStack tileDataToItemStack(BlockEntity tile, ItemStack itemStack) {
        IFluidHandler fluidHandlerTile = tile.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
        if (fluidHandlerTile != null) {
            IFluidHandlerItemCapacity fluidHandlerItemCapacity = itemStack.getCapability(Capabilities.FLUID_HANDLER_ITEM_CAPACITY).orElse(null);
            if (fluidHandlerItemCapacity != null) {
                if (fluidHandlerTile instanceof IFluidTank) {
                    IFluidTank fluidTank = (IFluidTank) fluidHandlerTile;
                    fluidHandlerItemCapacity.setCapacity(fluidTank.getCapacity());
                    itemStack = fluidHandlerItemCapacity.getContainer();
                }
            }
            IFluidHandlerItem fluidHandlerItem = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
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
    public static void itemStackDataToTile(ItemStack itemStack, BlockEntity tile) {
        IFluidHandler fluidHandlerTile = tile.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
        if (fluidHandlerTile != null) {
            IFluidHandlerItemCapacity fluidHandlerItemCapacity = itemStack.getCapability(Capabilities.FLUID_HANDLER_ITEM_CAPACITY).orElse(null);
            if (fluidHandlerItemCapacity != null) {
                if (fluidHandlerTile instanceof FluidTank) {
                    FluidTank fluidTank = (FluidTank) fluidHandlerTile;
                    fluidTank.setCapacity(fluidHandlerItemCapacity.getCapacity());
                }
            }

            IFluidHandlerItem fluidHandlerItem = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
            if (fluidHandlerItem != null) {
                FluidUtil.tryEmptyContainer(itemStack, fluidHandlerTile, Integer.MAX_VALUE, null, true);
            }
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Force allow shift-right clicking with a fluid container passing through to this block
        if (!event.getItemStack().isEmpty()
                && event.getItemStack().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()
                && event.getLevel().getBlockState(event.getPos()).getBlock() instanceof IBlockTank) {
            event.setUseBlock(Event.Result.ALLOW);
        }
    }

    public static class SimulatableTankWrapper implements IFluidHandler {

        private final IFluidHandler tank;

        public SimulatableTankWrapper(IFluidHandler tank) {
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
            for (int i = 0; i < tank.getTanks(); i++) {
                if (tank.getFluidInTank(i).getAmount() < tank.getTankCapacity(i)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            for (int i = 0; i < tank.getTanks(); i++) {
                if (!tank.getFluidInTank(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }


}
