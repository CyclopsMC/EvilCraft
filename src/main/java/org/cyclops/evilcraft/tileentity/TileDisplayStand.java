package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.InventoryTileEntity;
import org.cyclops.evilcraft.block.DisplayStand;

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

    protected EnumFacing getFacing() {
        return getWorld().getBlockState(getPos()).getValue(DisplayStand.FACING);
    }

    protected ItemStack getContents() {
        return getStackInSlot(0);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return facing == getFacing() || facing == getFacing().getOpposite() || getContents() == null
                ? super.hasCapability(capability, facing) : getContents().hasCapability(capability, null);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return facing == getFacing() || facing == getFacing().getOpposite() || getContents() == null
                ? super.getCapability(capability, facing) : getContents().getCapability(capability, null);
    }
}
