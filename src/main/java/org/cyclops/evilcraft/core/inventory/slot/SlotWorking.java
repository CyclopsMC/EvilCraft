package org.cyclops.evilcraft.core.inventory.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.tileentity.TileWorking;

/**
 * Slot that is used for only accepting workable items.
 * @author rubensworks
 * @param <T> The tile type.
 *
 */
public class SlotWorking<T extends TileWorking<T, ?>> extends Slot {

	protected ContainerTileWorking<T> container;
    private ItemStack lastSlotContents = null;
    private final World world;

    public SlotWorking(int index, int x, int y, ContainerTileWorking<T> container, World world) {
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
    public ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
        container.getTileSupplier().ifPresent(tile -> {
            if(!ItemStack.tagMatches(itemStack, this.getItem())) {
                tile.resetWork();
            }
        });
        return super.onTake(player, itemStack);
    }
    
    @Override
    public void setChanged() {
        container.getTileSupplier().ifPresent(tile -> {
            if(!ItemStack.tagMatches(lastSlotContents, this.getItem())) {
                tile.resetWork();
            }
        });
        lastSlotContents = this.getItem();
        if(!lastSlotContents.isEmpty()) lastSlotContents = lastSlotContents.copy();
    }
	
}
