package evilcraft.api.entities.tileentitites;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;
import evilcraft.entities.tileentities.NBTClassType;
import evilcraft.entities.tileentities.NBTPersist;

/**
 * A base class for all the tile entities that are used inside this mod.
 * @author rubensworks
 *
 */
public class EvilCraftTileEntity extends TileEntity{
    
    private List<Field> nbtPersistedFields = null;
    
    @NBTPersist
    private boolean rotatable = false;
    @NBTPersist
    private ForgeDirection rotation = ForgeDirection.NORTH;
    
    /**
     * Make a new instance.
     */
    public EvilCraftTileEntity() {
        generateNBTPersistedFields();
    }
    
    /**
     * Called when the block of this tile entity is destroyed.
     */
    public void destroy() {
        invalidate();
    }
    
    /**
     * If this entity is interactable with a player.
     * @param entityPlayer The player that is checked.
     * @return If the given player can interact.
     */
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }
    
    /**
     * Send a world update for the coordinates of this tile entity.
     */
    public void sendUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public Packet getDescriptionPacket() {
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, getNBTTagCompound());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        NBTTagCompound tag = packet.func_148857_g();
        readFromNBT(tag);
        onUpdateReceived();
    }
    
    private void generateNBTPersistedFields() {
        nbtPersistedFields = new LinkedList<Field>();
        for(Field field : this.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(NBTPersist.class)) {         
                nbtPersistedFields.add(field);
            }
        }
    }
    
    private void writePersistedField(Field field, NBTTagCompound tag) {
        NBTClassType.performActionForField(this, field, tag, true);
    }
    
    private void readPersistedField(Field field, NBTTagCompound tag) {
        NBTClassType.performActionForField(this, field, tag, false);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound NBTTagCompound) {
        super.writeToNBT(NBTTagCompound);
        for(Field field : nbtPersistedFields)
            writePersistedField(field, NBTTagCompound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound NBTTagCompound) {
        super.readFromNBT(NBTTagCompound);
        for(Field field : nbtPersistedFields)
            readPersistedField(field, NBTTagCompound);
    }
    
    /**
     * Get the NBT tag for this tile entity.
     * @return The NBT tag that is created with the
     * {@link EvilCraftTileEntity#writeToNBT(NBTTagCompound)} method.
     */
    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }
    
    /**
     * If the block this tile entity has can be rotated.
     * @return If it can be rotated.
     */
    public boolean isRotatable() {
        return this.rotatable;
    }
    
    /**
     * Set whether or not the block that has this tile entity can be rotated.
     * @param rotatable If it can be rotated.
     */
    public void setRotatable(boolean rotatable) {
        this.rotatable = rotatable;
    }

    /**
     * Get the current rotation of this tile entity.
     * Default is {@link ForgeDirection#NORTH}.
     * @return The rotation.
     */
    public ForgeDirection getRotation() {
        return rotation;
    }

    /**
     * Set the rotation of this tile entity.
     * Default is {@link ForgeDirection#NORTH}.
     * @param rotation The new rotation.
     */
    public void setRotation(ForgeDirection rotation) {
        this.rotation = rotation;
    }
    
    /**
     * Get the block type this tile entity is defined for.
     * @return The block instance.
     */
    public ConfigurableBlockContainer getBlock() {
        return (ConfigurableBlockContainer) this.getBlockType();
    }
    
    /**
     * This method is called when the tile entity receives
     * an update (ie a data packet) from the server. 
     * If this tile entity  uses NBT, then the NBT will have 
     * already been updated when this method is called.
     */
    public void onUpdateReceived() {
        
    }
}
