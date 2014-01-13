package evilcraft.api.entities.tileentitites;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
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
    
    public void sendUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }
    
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        super.onDataPacket(net, packet);
        NBTTagCompound tag = packet.data;
        readFromNBT(tag);
    }
    
}
