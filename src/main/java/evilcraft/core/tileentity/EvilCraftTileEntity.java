package evilcraft.core.tileentity;

import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * A base class for all the tile entities that are used inside this mod.
 * 
 * This class has an anti-lag mechanism to send updates with {@link EvilCraftTileEntity#sendUpdate()}.
 * Every instance has a continuously looping counter that counts from getUpdateBackoffTicks() to zero.
 * and every time the counter reaches zero, the backoff will be reset and an update packet will be sent
 * if one has been queued.
 * 
 * @author rubensworks
 *
 */
public class EvilCraftTileEntity extends TileEntity {
	
	private static final int UPDATE_BACKOFF_TICKS = 1;
    
    private List<Field> nbtPersistedFields = null;
    
    @NBTPersist
    private Boolean rotatable = false;
    private ForgeDirection rotation = ForgeDirection.NORTH;
    private boolean shouldSendUpdate = false;
    private int sendUpdateBackoff = 0;
    
    /**
     * Make a new instance.
     */
    public EvilCraftTileEntity() {
    	sendUpdateBackoff = (int) Math.round(Math.random() * getUpdateBackoffTicks()); // Random backoff so not all TE's will be updated at once.
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
     * This will always send lag-safe updates, so calling this many times per tick will
     * not cause multiple packets to be sent, more info in the class javadoc.
     */
    public final void sendUpdate() {
    	shouldSendUpdate = true;
    }
    
    /**
     * Send an immediate world update for the coordinates of this tile entity.
     * This does the same as {@link EvilCraftTileEntity#sendUpdate()} but will
     * ignore the update backoff.
     */
    public final void sendImmediateUpdate() {
    	sendUpdate();
    	sendUpdateBackoff = 0;
    }
    
    /**
     * Do not override this method (you won't even be able to do so).
     * Use updateTileEntity() instead.
     */
    @Override
    public final void updateEntity() {
    	super.updateEntity();
    	updateTileEntity();
    	trySendActualUpdate();
    }
    
    /**
     * Override this method instead of {@link EvilCraftTileEntity#updateEntity()}.
     * This method is called each tick.
     */
    protected void updateTileEntity() {
    	
    }
    
    private void trySendActualUpdate() {
    	sendUpdateBackoff--;    		
		if(sendUpdateBackoff <= 0) {
            sendUpdateBackoff = getUpdateBackoffTicks();
			
			if(shouldSendUpdate && isTileValid()) {
    			shouldSendUpdate = false;
        		
	    		beforeSendUpdate();
	    		onSendUpdate();
	    		afterSendUpdate();
    		}
		}
    }

    protected boolean isTileValid() {
        return getBlockType() instanceof ConfigurableBlockContainer;
    }
    
    /**
     * Called when an update will is sent.
     * This contains the logic to send the update, so make sure to call the super!
     */
    protected void onSendUpdate() {
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    /**
     * Called when before update is sent.
     */
    protected void beforeSendUpdate() {
    	
    }
    
    /**
     * Called when after update is sent. (Not necessarily received yet!)
     */
    protected void afterSendUpdate() {
    	
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
        for(Class<?> clazz = this.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
	        for(Field field : clazz.getDeclaredFields()) {
	            if(field.isAnnotationPresent(NBTPersist.class)) {         
	                nbtPersistedFields.add(field);
	            }
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
        
        // Separate action for direction
        NBTTagCompound.setInteger("rotation", rotation.ordinal());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound NBTTagCompound) {
        super.readFromNBT(NBTTagCompound);
        for(Field field : nbtPersistedFields)
            readPersistedField(field, NBTTagCompound);
        
        // Separate action for direction
        ForgeDirection foundRotation = ForgeDirection
        		.getOrientation(NBTTagCompound.getInteger("rotation"));
        if(foundRotation != ForgeDirection.UNKNOWN) {
        	rotation = foundRotation;
        }
        onLoad();
    }

    /**
     * When the tile is loaded or created.
     */
    public void onLoad() {

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
    
    /**
     * @return The minimum amount of ticks between two consecutive sent packets.
     */
    protected int getUpdateBackoffTicks() {
    	return UPDATE_BACKOFF_TICKS;
    }
}
