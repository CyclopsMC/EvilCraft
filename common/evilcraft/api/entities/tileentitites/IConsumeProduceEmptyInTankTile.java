package evilcraft.api.entities.tileentitites;

import net.minecraft.item.ItemStack;
import evilcraft.api.inventory.SimpleInventory;

public interface IConsumeProduceEmptyInTankTile extends IConsumeProduceWithTankTile{
    
    public int getEmptyToTankSlot();
    
}
