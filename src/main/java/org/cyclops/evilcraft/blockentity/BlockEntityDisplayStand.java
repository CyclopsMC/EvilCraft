package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.capability.registrar.BlockEntityCapabilityRegistrar;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;

import java.util.function.Supplier;

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
        super(RegistryEntries.BLOCK_ENTITY_DISPLAY_STAND.get(), blockPos, blockState);
        this.inventory = new SimpleInventory(1, 1);
        inventory.addDirtyMarkListener(this);
    }

    public static class CapabilityRegistrar extends BlockEntityCapabilityRegistrar<BlockEntityDisplayStand> {
        public CapabilityRegistrar(Supplier<BlockEntityType<? extends BlockEntityDisplayStand>> blockEntityType) {
            super(blockEntityType);
        }

        @Override
        public void populate() {
            add(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK, (blockEntity, direction) -> new InvWrapper(blockEntity.getInventory()));
            add(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK, (blockEntity, direction) -> blockEntity.getContents().getCapability(Capabilities.FluidHandler.ITEM));
        }
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
        return IModHelpers.get().getBlockHelpers().getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), BlockDisplayStand.FACING, Direction.NORTH);
    }

    protected ItemStack getContents() {
        return this.inventory.getItem(0);
    }

    @Override
    public void read(ValueInput input) {
        super.read(input);
        inventory.readFromNBT(input, "inventory");
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        inventory.writeToNBT(output, "inventory");
        super.saveAdditional(output);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        onUpdateReceived();
    }

    public void onUpdateReceived() {
        IModHelpers.get().getBlockHelpers().markForUpdate(level, worldPosition);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        IModHelpers.get().getInventoryHelpers().dropItems(getLevel(), getInventory(), pos);
    }
}
