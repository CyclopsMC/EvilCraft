package evilcraft.api.entities.tileentitites;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;

/**
 * A base class for all the tile entities that are used inside this mod.
 * @author rubensworks
 *
 */
public class EvilCraftTileEntity extends TileEntity{
    
    private boolean rotatable = false;
    private ForgeDirection rotation = ForgeDirection.NORTH;
    
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
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, getNBTTagCompound());
    }
    
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        super.onDataPacket(net, packet);
        NBTTagCompound tag = packet.data;
        readFromNBT(tag);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound NBTTagCompound) {
        super.writeToNBT(NBTTagCompound);
        NBTTagCompound.setBoolean("rotatable", rotatable);
        NBTTagCompound.setInteger("rotation", rotation.ordinal());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound NBTTagCompound) {
        super.readFromNBT(NBTTagCompound);
        rotatable = NBTTagCompound.getBoolean("rotatable");
        rotation = ForgeDirection.getOrientation(NBTTagCompound.getInteger("rotation"));
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
    
}
