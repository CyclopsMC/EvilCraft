package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;

/**
 * A block that can display items.
 * @author rubensworks
 *
 */
public class BlockEntityDisplayStand extends CyclopsBlockEntity {

    @NBTPersist
    private ItemStack displayStandType;
    @NBTPersist
    private boolean directionPositive = false;
    private final SimpleInventory inventory;

    public BlockEntityDisplayStand(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_DISPLAY_STAND, blockPos, blockState);
        this.inventory = new SimpleInventory(1, 1);
        addCapabilityInternal(ForgeCapabilities.ITEM_HANDLER, LazyOptional.of(inventory::getItemHandler));
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
    public void read(CompoundTag tag) {
        super.read(tag);
        inventory.readFromNBT(tag, "inventory");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        inventory.writeToNBT(tag, "inventory");
        super.saveAdditional(tag);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            capability = (Capability<T>) ForgeCapabilities.FLUID_HANDLER_ITEM;
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
