package org.cyclops.evilcraft.core.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacityConfig;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.item.DamageIndicatedItemComponent;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.fluid.SimulatedFluidStack;

import javax.annotation.Nullable;

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
     * Called upon tank activation.
     * @param world The world.
     * @param blockPos The position.
     * @param player Player
     * @param hand The hand
     * @param side Side integer
     * @param motionX X motion
     * @param motionY Y motion
     * @param motionZ Z motion
     * @return If the event should be halted.
     */
    public static boolean onBlockActivatedTank(World world, BlockPos blockPos, EntityPlayer player, EnumHand hand,
                                               EnumFacing side, float motionX, float motionY, float motionZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(blockPos);
        if(tile != null) {
            if(!itemStack.isEmpty()) {
                SimulatableTankWrapper tank = new SimulatableTankWrapper(tile.getTank());
                IFluidHandler itemFluidHandler = FluidUtil.getFluidHandler(itemStack);
                if(!player.isSneaking() && !tank.isFull() && itemFluidHandler != null) { // Fill the tank.
                    FluidActionResult result = FluidUtil.tryEmptyContainer(itemStack, tank, Fluid.BUCKET_VOLUME, player, true);
                    if (result.isSuccess() && !player.capabilities.isCreativeMode) {
                        InventoryHelpers.tryReAddToStack(player, itemStack, result.getResult());
                    }
                    return true;
                } else if(player.isSneaking() && !tank.isEmpty()) { // Drain the tank.
                    FluidActionResult result = FluidUtil.tryFillContainer(itemStack, tank, Fluid.BUCKET_VOLUME, player, true);
                    if (result.isSuccess() && !player.capabilities.isCreativeMode) {
                        InventoryHelpers.tryReAddToStack(player, itemStack, result.getResult());
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
    public static String getInfoTank(ItemStack itemStack) {
        int amount = 0;
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);
        if(fluidStack != null) {
            amount = fluidStack.amount;
        }
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
        if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                && itemStack.hasCapability(FluidHandlerItemCapacityConfig.CAPABILITY, null)) {
            IFluidHandler fluidHandlerTile = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            if (fluidHandlerTile instanceof IFluidTank) {
                IFluidTank fluidTank = (IFluidTank) fluidHandlerTile;
                IFluidHandlerItemCapacity fluidHandlerItemCapacity = itemStack.getCapability(FluidHandlerItemCapacityConfig.CAPABILITY, null);
                fluidHandlerItemCapacity.setCapacity(fluidTank.getCapacity());
                itemStack = fluidHandlerItemCapacity.getContainer();
            }
        }

        if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                && itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            IFluidHandler fluidHandlerTile = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            FluidActionResult res = FluidUtil.tryFillContainer(itemStack, fluidHandlerTile, Integer.MAX_VALUE, null, true);
            if (res.isSuccess()) {
                itemStack = res.getResult();
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
        if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                && itemStack.hasCapability(FluidHandlerItemCapacityConfig.CAPABILITY, null)) {
            IFluidHandler fluidHandlerTile = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            if (fluidHandlerTile instanceof FluidTank) {
                FluidTank fluidTank = (FluidTank) fluidHandlerTile;
                IFluidHandlerItemCapacity fluidHandlerItemCapacity = itemStack.getCapability(FluidHandlerItemCapacityConfig.CAPABILITY, null);
                fluidTank.setCapacity(fluidHandlerItemCapacity.getCapacity());
            }
        }

        if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                && itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            IFluidHandler fluidHandlerTile = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            FluidUtil.tryEmptyContainer(itemStack, fluidHandlerTile, Integer.MAX_VALUE, null, true);
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Force allow shift-right clicking with a fluid container passing through to this block
        if (!event.getItemStack().isEmpty()
                && event.getItemStack().hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
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
        public IFluidTankProperties[] getTankProperties() {
            return tank.getTankProperties();
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return tank.fill(resource, doFill);
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            FluidStack drained = tank.drain(resource, doDrain);
            return doDrain ? drained : new SimulatedFluidStack(drained.getFluid(), drained.amount);
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            FluidStack drained = tank.drain(maxDrain, doDrain);
            return doDrain ? drained : new SimulatedFluidStack(drained.getFluid(), drained.amount);
        }

        public boolean isFull() {
            return tank.isFull();
        }

        public boolean isEmpty() {
            return tank.isEmpty();
        }
    }


}
