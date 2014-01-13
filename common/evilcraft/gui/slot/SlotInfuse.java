package evilcraft.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import evilcraft.entities.tileentities.TileBloodInfuser;

public class SlotInfuse extends Slot {
    
    private TileBloodInfuser tile;
    
    public SlotInfuse(IInventory inventory, int index, int x,
            int y, TileBloodInfuser tile) {
        super(inventory, index, x, y);
        this.tile = tile;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return itemStack != null && tile.canConsume(itemStack);
    }
    
}
