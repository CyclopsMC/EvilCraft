package evilcraft.api.entities.tileentitites;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import evilcraft.api.Helpers;

public class TileConnectedTexture extends TileEntity{
    
    // Which directions relative to this block should connect (have same ID for example)
    protected boolean[] connectWithSides = new boolean[Helpers.DIRECTIONS.size()];
    
    public void writeToNBT(NBTTagCompound NBTTagCompound)
    {
        super.writeToNBT(NBTTagCompound);
        for(int i = 0; i < connectWithSides.length; i++) {
            NBTTagCompound.setBoolean("connect"+i, connectWithSides[i]);
        }
    }
    public void readFromNBT(NBTTagCompound NBTTagCompound)
    {
        super.readFromNBT(NBTTagCompound);
        for(int i = 0; i < connectWithSides.length; i++) {
            connectWithSides[i] = NBTTagCompound.getBoolean("connect"+i);
        }
    }
    
    public boolean[] getConnectWithSides() {
        return connectWithSides;
    }
    
    public void setConnectWithSides(boolean[] connectWithSides) {
        this.connectWithSides = connectWithSides;
    }
    
    public void connect(ForgeDirection direction, boolean connect) {
        this.connectWithSides[direction.ordinal()] = connect;
        sendUpdate();
    }
    
    public void check() {
        int i = 0;
        for(boolean con : getConnectWithSides())
            if(con)
                i++;
        System.out.println("check:"+i);
    }
    
    private void sendUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
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
