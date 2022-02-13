package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;

import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.ITickingTile;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.TickingTileComponent;

/**
 * A block that can display items.
 * @author rubensworks
 *
 */
public class TileDisplayStand extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

    @Delegate
    protected final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    @NBTPersist
    private ItemStack displayStandType;
    @NBTPersist
    private boolean directionPositive = false;
    private final SimpleInventory inventory;

    public TileDisplayStand() {
        super(RegistryEntries.TILE_ENTITY_DISPLAY_STAND);
        this.inventory = new SimpleInventory(1, 1);
        addCapabilityInternal(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, LazyOptional.of(inventory::getItemHandler));
        inventory.addDirtyMarkListener(this);
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    public void onDirty() {
        super.onDirty();
        sendUpdate();
    }

    public Direction.AxisDirection getDirection() {
        return directionPositive ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
    }

    public void setDirection(Direction.AxisDirection direction) {
        this.directionPositive = direction == Direction.AxisDirection.POSITIVE;
        sendUpdate();
    }

    public void setDisplayStandType(ItemStack displayStandType) {
        this.displayStandType = displayStandType;
        sendUpdate();
    }

    public ItemStack getDisplayStandType() {
        return this.displayStandType;
    }

    protected Direction getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), BlockDisplayStand.FACING, Direction.NORTH);
    }

    protected ItemStack getContents() {
        return this.inventory.getItem(0);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        inventory.readFromNBT(tag, "inventory");
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        inventory.writeToNBT(tag, "inventory");
        return super.save(tag);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            capability = (Capability<T>) CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
        }
        return facing == getFacing() || facing == getFacing().getOpposite() || getContents().isEmpty()
                ? super.getCapability(capability, facing)
                : getContents().getCapability(capability, null);
    }

    @Override
    public void onUpdateReceived() {
        super.onUpdateReceived();
        BlockHelpers.markForUpdate(level, worldPosition);
    }
}
