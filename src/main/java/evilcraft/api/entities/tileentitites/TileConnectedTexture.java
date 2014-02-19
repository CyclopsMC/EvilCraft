package evilcraft.api.entities.tileentitites;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import evilcraft.api.DirectionCorner;
import evilcraft.api.Helpers;

/**
 * A tile entity for blocks that have a connected texture.
 * @author rubensworks
 *
 */
public class TileConnectedTexture extends EvilCraftTileEntity{
    
    // Which directions relative to this block should connect (have same ID for example)
    protected boolean[] connectWithSides = new boolean[Helpers.DIRECTIONS.size()];
    // Which directions relative to this block (with corner) should connect (have same ID for example)
    protected boolean[] connectWithSidesCorner = new boolean[Helpers.DIRECTIONS_CORNERS.size()];
    
    @Override
    public void writeToNBT(NBTTagCompound NBTTagCompound) {
        super.writeToNBT(NBTTagCompound);
        for(int i = 0; i < connectWithSides.length; i++) {
            NBTTagCompound.setBoolean("connect"+i, connectWithSides[i]);
        }
        for(int i = 0; i < connectWithSidesCorner.length; i++) {
            NBTTagCompound.setBoolean("connectCorner"+i, connectWithSidesCorner[i]);
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound NBTTagCompound) {
        super.readFromNBT(NBTTagCompound);
        for(int i = 0; i < connectWithSides.length; i++) {
            connectWithSides[i] = NBTTagCompound.getBoolean("connect"+i);
        }
        for(int i = 0; i < connectWithSidesCorner.length; i++) {
            connectWithSidesCorner[i] = NBTTagCompound.getBoolean("connectCorner"+i);
        }
    }
    
    /**
     * Get the array that is indexed by the {@link ForgeDirection} ordinal values with
     * their respective value for whether or not this block should connect to that block.
     * @return The connect with sides boolean array.
     */
    public boolean[] getConnectWithSides() {
        return connectWithSides;
    }
    
    /**
     * Get the array that is indexed by the {@link DirectionCorner} ordinal values with
     * their respective value for whether or not this block should connect to that block.
     * @return The connect with sides boolean array.
     */
    public boolean[] getConnectWithSidesCorner() {
        return connectWithSidesCorner;
    }
    
    /**
     * Set the connection to a certain {@link ForgeDirection}.
     * @param direction The direction to enable/disable the connection to.
     * @param connect If the connection for the given direction should be enabled.
     */
    public void connect(ForgeDirection direction, boolean connect) {
        boolean old = this.connectWithSides[direction.ordinal()];
        this.connectWithSides[direction.ordinal()] = connect;
        if(old != connect)
            sendUpdate();
    }
    
    /**
     * Set the connection to a certain {@link DirectionCorner}.
     * @param direction The direction to enable/disable the connection to.
     * @param connect If the connection for the given direction should be enabled.
     */
    public void connectCorner(DirectionCorner direction, boolean connect) {
        boolean old = this.connectWithSidesCorner[direction.ordinal()];
        this.connectWithSidesCorner[direction.ordinal()] = connect;
        if(old != connect)
            sendUpdate();
    }

}
