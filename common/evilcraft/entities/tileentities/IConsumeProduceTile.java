package evilcraft.entities.tileentities;

import net.minecraft.item.ItemStack;
import evilcraft.inventory.SimpleInventory;

public interface IConsumeProduceTile {
    
    public SimpleInventory getInventory();
    public int getConsumeSlot();
    public int getProduceSlot();
    public boolean canConsume(ItemStack itemStack);
    
}
