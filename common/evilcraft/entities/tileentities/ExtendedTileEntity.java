package evilcraft.entities.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ExtendedTileEntity extends TileEntity{
    
    protected int inventorySize;
    
    public void destroy() {
        
    }
    
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }
    
    public int getInventorySize() {
        return this.inventorySize;
    }
    
}
