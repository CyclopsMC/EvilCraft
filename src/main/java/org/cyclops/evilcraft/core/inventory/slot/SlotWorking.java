package org.cyclops.evilcraft.core.inventory.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;

/**
 * Slot that is used for only accepting workable items.
 * @author rubensworks
 * @param <T> The tile type.
 *
 */
public class SlotWorking<T extends BlockEntityWorking<T, ?>> extends Slot {

    protected ContainerTileWorking<T> container;
    private ItemStack lastSlotContents = null;
    private final Level world;

    public SlotWorking(int index, int x, int y, ContainerTileWorking<T> container, Level world) {
        super(container.getContainerInventory(), index, x, y);
        this.container = container;
        this.lastSlotContents = getItem();
        this.world = world;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return !itemStack.isEmpty() && container.getTileWorkingMetadata().canConsume(itemStack, this.world);
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        container.getTileSupplier().ifPresent(tile -> {
            if(!ItemStack.isSameItemSameTags(itemStack, this.getItem())) {
                tile.resetWork();
            }
        });
        super.onTake(player, itemStack);
    }

    @Override
    public void setChanged() {
        container.getTileSupplier().ifPresent(tile -> {
            if(!ItemStack.isSameItemSameTags(lastSlotContents, this.getItem())) {
                tile.resetWork();
            }
        });
        lastSlotContents = this.getItem();
        if(!lastSlotContents.isEmpty()) lastSlotContents = lastSlotContents.copy();
    }

}
