package evilcraft.api.entities.tileentitites;

import net.minecraft.item.ItemStack;
import evilcraft.api.inventory.SimpleInventory;

public interface IConsumeProduceTile {
    
    public SimpleInventory getInventory();
    public int getConsumeSlot();
    public int getProduceSlot();
    public boolean canConsume(ItemStack itemStack);
    
}
