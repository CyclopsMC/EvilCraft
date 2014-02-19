package evilcraft.api;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Iterate over a player's hotbar.
 * @author rubensworks
 *
 */
public class HotbarIterator implements Iterator<ItemStack>{
    
    private EntityPlayer player;
    private int i;
    
    /**
     * Create a new HotbarIterator.
     * @param player The player to iterate the hotbar from.
     */
    public HotbarIterator(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public boolean hasNext() {
        return i < player.inventory.mainInventory.length;
    }

    @Override
    public ItemStack next() {
        return player.inventory.mainInventory[i++];
    }

    @Override
    public void remove() {
        if(i - 1 >= 0 && i - 1 < player.inventory.mainInventory.length)
            player.inventory.mainInventory[i - 1] = null;
    }

}
