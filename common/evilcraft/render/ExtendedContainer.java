package evilcraft.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ExtendedContainer extends Container{
    
    private int inventorySize;

    public ExtendedContainer(int inventorySize) {
        this.inventorySize = inventorySize;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }
    
}
