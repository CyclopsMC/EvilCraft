package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.capability.registrar.BlockEntityCapabilityRegistrar;
import org.cyclops.cyclopscore.helper.BlockHelpers;
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
            add(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK, (blockEntity, direction) -> blockEntity.getInventory().getItemHandler());
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
        return BlockHelpers.getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), BlockDisplayStand.FACING, Direction.NORTH);
    }

    protected ItemStack getContents() {
        return this.inventory.getItem(0);
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        super.read(tag, holderLookupProvider);
        inventory.readFromNBT(holderLookupProvider, tag, "inventory");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        inventory.writeToNBT(holderLookupProvider, tag, "inventory");
        super.saveAdditional(tag, holderLookupProvider);
    }

    @Override
    public void onUpdateReceived() {
        super.onUpdateReceived();
        BlockHelpers.markForUpdate(level, worldPosition);
    }
}
