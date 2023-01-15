package org.cyclops.evilcraft.core.inventory.slot;

import net.minecraft.world.entity.player.Player;
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
public class SlotWorkingRemoveOnly<T extends BlockEntityWorking<T, ?>> extends SlotWorking<T> {

    private boolean shouldHardReset;

    public SlotWorkingRemoveOnly(int index, int x, int y, ContainerTileWorking<T> container, boolean shouldHardReset, Level world) {
        super(index, x, y, container, world);
        this.shouldHardReset = shouldHardReset;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        container.getTileSupplier().ifPresent(tile -> {
            tile.resetWork(shouldHardReset);
        });
        super.onTake(player, itemStack);
    }

    @Override
    public void setChanged() {
        container.getTileSupplier().ifPresent(tile -> {
            tile.resetWork(shouldHardReset);
        });
    }

}
