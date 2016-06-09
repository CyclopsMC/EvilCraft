package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.InventoryTileEntity;

import javax.annotation.Nullable;
import java.util.Collections;

/**
 * A block that can display items.
 * @author rubensworks
 *
 */
public class TileDisplayStand extends InventoryTileEntity implements CyclopsTileEntity.ITickingTile {

    @Delegate
    protected final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    @NBTPersist
    private ItemStack displayStandType;
    @NBTPersist
    private boolean directionPositive = false;

    public TileDisplayStand() {
        super(1, "displayStand", 1);
        for (EnumFacing side : EnumFacing.values()) {
            addSlotsToSide(side, Collections.singleton(0));
        }
        addCapabilityInternal(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, new ItemFluidWrapper(this));
    }

    public EnumFacing.AxisDirection getDirection() {
        return directionPositive ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE;
    }

    public void setDirection(EnumFacing.AxisDirection direction) {
        this.directionPositive = direction == EnumFacing.AxisDirection.POSITIVE;
        sendUpdate();
    }

    public ItemStack getDisplayStandType() {
        return this.displayStandType;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        super.setInventorySlotContents(slotId, itemstack);
        sendUpdate();
    }

    public static class ItemFluidWrapper implements IFluidHandler {

        private final TileDisplayStand tile;

        public ItemFluidWrapper(TileDisplayStand tile) {
            this.tile = tile;
        }

        protected IFluidHandler getFluidHandler() {
            IFluidHandler fluidHandler = FluidUtil.getFluidHandler(tile.getStackInSlot(0));
            if (fluidHandler == null) {
                return EmptyFluidHandler.INSTANCE;
            }
            return fluidHandler;
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            return getFluidHandler().getTankProperties();
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return getFluidHandler().fill(resource, doFill);
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return getFluidHandler().drain(resource, doDrain);
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return getFluidHandler().drain(maxDrain, doDrain);
        }
    }
}
