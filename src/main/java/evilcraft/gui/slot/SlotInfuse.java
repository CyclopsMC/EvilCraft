package evilcraft.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import evilcraft.entities.tileentities.TileBloodInfuser;

/**
 * Slot that is used for only acceepting infusable items.
 * @author rubensworks
 *
 */
public class SlotInfuse extends Slot {
    
    private TileBloodInfuser tile;
    
    /**
     * Make a new instance.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param tile The this slot is in.
     */
    public SlotInfuse(IInventory inventory, int index, int x,
            int y, TileBloodInfuser tile) {
        super(inventory, index, x, y);
        this.tile = tile;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return itemStack != null && tile.canConsume(itemStack);
    }
    
    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack) {
        tile.resetInfusion();
    }
    
}
