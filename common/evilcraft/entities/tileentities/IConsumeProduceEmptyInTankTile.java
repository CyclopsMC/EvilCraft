package evilcraft.entities.tileentities;

import net.minecraft.item.ItemStack;
import evilcraft.inventory.SimpleInventory;

public interface IConsumeProduceEmptyInTankTile extends IConsumeProduceWithTankTile{
    
    public int getEmptyToTankSlot();
    
}
