package evilcraft.api.entities.tileentitites;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import evilcraft.api.DirectionCorner;
import evilcraft.api.Helpers;

public class TileConnectedTexture extends TileEntity{
    
    // Which directions relative to this block should connect (have same ID for example)
    protected boolean[] connectWithSides = new boolean[Helpers.DIRECTIONS.size()];
    // Which directions relative to this block (with corner) should connect (have same ID for example)
    protected boolean[] connectWithSidesCorner = new boolean[Helpers.DIRECTIONS_CORNERS.size()];
    
    public void writeToNBT(NBTTagCompound NBTTagCompound)
    {
        super.writeToNBT(NBTTagCompound);
        for(int i = 0; i < connectWithSides.length; i++) {
            NBTTagCompound.setBoolean("connect"+i, connectWithSides[i]);
        }
        for(int i = 0; i < connectWithSidesCorner.length; i++) {
            NBTTagCompound.setBoolean("connectCorner"+i, connectWithSidesCorner[i]);
        }
    }
    public void readFromNBT(NBTTagCompound NBTTagCompound)
    {
        super.readFromNBT(NBTTagCompound);
        for(int i = 0; i < connectWithSides.length; i++) {
            connectWithSides[i] = NBTTagCompound.getBoolean("connect"+i);
        }
        for(int i = 0; i < connectWithSidesCorner.length; i++) {
            connectWithSidesCorner[i] = NBTTagCompound.getBoolean("connectCorner"+i);
        }
    }
    
    public boolean[] getConnectWithSides() {
        return connectWithSides;
    }
    
    public boolean[] getConnectWithSidesCorner() {
        return connectWithSidesCorner;
    }
    
    public void connect(ForgeDirection direction, boolean connect) {
        boolean old = this.connectWithSides[direction.ordinal()];
        this.connectWithSides[direction.ordinal()] = connect;
        if(old != connect)
            sendUpdate();
    }
    
    public void connectCorner(DirectionCorner direction, boolean connect) {
        boolean old = this.connectWithSidesCorner[direction.ordinal()];
        this.connectWithSidesCorner[direction.ordinal()] = connect;
        if(old != connect)
            sendUpdate();
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
