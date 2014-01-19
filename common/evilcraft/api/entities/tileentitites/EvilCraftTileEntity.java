package evilcraft.api.entities.tileentitites;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import evilcraft.api.config.configurable.ConfigurableBlock;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;

public class EvilCraftTileEntity extends TileEntity{
    
    protected boolean rotatable = false;
    protected ForgeDirection rotation = ForgeDirection.NORTH;
    
    public void destroy() {
        
    }
    
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }
    
    public void sendUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public Packet getDescriptionPacket() {
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, getNBTTagCompound());
    }
    
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        super.onDataPacket(net, packet);
        NBTTagCompound tag = packet.data;
        readFromNBT(tag);
    }
    
    public void writeToNBT(NBTTagCompound NBTTagCompound)
    {
        super.writeToNBT(NBTTagCompound);
        NBTTagCompound.setBoolean("rotatable", rotatable);
        NBTTagCompound.setInteger("rotation", rotation.ordinal());
    }
    public void readFromNBT(NBTTagCompound NBTTagCompound)
    {
        super.readFromNBT(NBTTagCompound);
        rotatable = NBTTagCompound.getBoolean("rotatable");
        rotation = ForgeDirection.getOrientation(NBTTagCompound.getInteger("rotation"));
    }
    
    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }
    
    public boolean isRotatable() {
        return this.rotatable;
    }
    
    public void setRotatable(boolean rotatable) {
        this.rotatable = rotatable;
    }

    public ForgeDirection getRotation() {
        return rotation;
    }

    public void setRotation(ForgeDirection rotation) {
        this.rotation = rotation;
    }
    
    public ConfigurableBlockContainer getBlock() {
        return (ConfigurableBlockContainer) this.getBlockType();
    }
    
}
