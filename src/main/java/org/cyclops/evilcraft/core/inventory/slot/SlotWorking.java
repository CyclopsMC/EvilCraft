package org.cyclops.evilcraft.core.inventory.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.core.inventory.container.ContainerWorking;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;

/**
 * Slot that is used for only accepting workable items.
 * @author rubensworks
 * @param <T> The tile type.
 *
 */
public class SlotWorking<T extends TileWorking<T, ?>> extends Slot {

	protected ContainerWorking<T> container;
    private ItemStack lastSlotContents = null;
    private final World world;

    public SlotWorking(int index, int x, int y, ContainerWorking<T> container, World world) {
        super(container.getContainerInventory(), index, x, y);
        this.container = container;
        this.lastSlotContents = getStack();
        this.world = world;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return !itemStack.isEmpty() && container.getTileWorkingMetadata().canConsume(itemStack, this.world);
    }
    
    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
        container.getTileSupplier().ifPresent(tile -> {
            if(!ItemStack.areItemStackTagsEqual(itemStack, this.getStack())) {
                tile.resetWork();
            }
        });
        return super.onTake(player, itemStack);
    }
    
    @Override
    public void onSlotChanged() {
        container.getTileSupplier().ifPresent(tile -> {
            if(!ItemStack.areItemStackTagsEqual(lastSlotContents, this.getStack())) {
                tile.resetWork();
            }
        });
        lastSlotContents = this.getStack();
        if(!lastSlotContents.isEmpty()) lastSlotContents = lastSlotContents.copy();
    }
	
}
