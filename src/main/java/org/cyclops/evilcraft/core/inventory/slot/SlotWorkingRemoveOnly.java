package org.cyclops.evilcraft.core.inventory.slot;

import net.minecraft.entity.player.PlayerEntity;
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
public class SlotWorkingRemoveOnly<T extends TileWorking<T, ?>> extends SlotWorking<T> {
   
	private boolean shouldHardReset;

    public SlotWorkingRemoveOnly(int index, int x, int y, ContainerTileWorking<T> container, boolean shouldHardReset, World world) {
        super(index, x, y, container, world);
        this.shouldHardReset = shouldHardReset;
    }
    
    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }
    
    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
        container.getTileSupplier().ifPresent(tile -> {
            tile.resetWork(shouldHardReset);
        });
        return super.onTake(player, itemStack);
    }
    
    @Override
    public void setChanged() {
        container.getTileSupplier().ifPresent(tile -> {
            tile.resetWork(shouldHardReset);
        });
    }
	
}
