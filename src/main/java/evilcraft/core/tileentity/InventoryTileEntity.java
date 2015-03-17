package evilcraft.core.tileentity;

import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.inventory.SimpleInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A TileEntity with an internal inventory.
 * @author rubensworks
 *
 */
public abstract class InventoryTileEntity extends TickingEvilCraftTileEntity implements ISidedInventory {
    
    protected SimpleInventory inventory;
    protected Map<EnumFacing, int[]> slotSides;
    protected Map<EnumFacing, Integer> slotSidesSize;
    protected boolean sendUpdateOnInventoryChanged = false;
    
    /**
     * Make new tile with an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param stackSize The maximum stacksize each slot can have
     */
    public InventoryTileEntity(int inventorySize, String inventoryName, int stackSize) {
        inventory = new SimpleInventory(inventorySize , inventoryName, stackSize);
        slotSides = new HashMap<EnumFacing, int[]>();
        slotSidesSize = new HashMap<EnumFacing, Integer>();
        for(EnumFacing side : DirectionHelpers.DIRECTIONS) {
            // Init each side to it can theoretically hold all possible slots,
            // Integer lists are not option because Java allows to autoboxing
            // and that would be required in the getter methods below.
            int array[] = new int[inventorySize];
            for(int i = 0; i < inventorySize; i++) array[i] = -1;
            slotSides.put(side, array);
            slotSidesSize.put(side, 0); 
        }
    }
    
    /**
     * Make new tile with an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     */
    public InventoryTileEntity(int inventorySize, String inventoryName) {
        this(inventorySize , inventoryName, 64);
    }
    
    /**
     * Add mappings to slots to a certain (normalized) side of this TileEntity.
     * @param side The side to map this slots to.
     * @param slots The numerical representations of the slots to map.
     */
    protected void addSlotsToSide(EnumFacing side, Collection<Integer> slots) {
        int[] currentSlots = slotSides.get(side);
        int offset = slotSidesSize.get(side);
        int i = 0;
        for(int slot : slots) {
            currentSlots[offset + i] = slot;
            i++;
        }
        slotSidesSize.put(side, offset + i);
    }
    
    /**
     * Get the internal inventory.
     * @return The SimpleInventory.
     */
    public SimpleInventory getInventory() {
        return inventory;
    }
    
    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }
    
    @Override
    public ItemStack getStackInSlot(int slotId) {
        if(slotId >= getSizeInventory() || slotId < 0)
            return null;
        return inventory.getStackInSlot(slotId);
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        ItemStack itemStack  = inventory.decrStackSize(slotId, count);
        onInventoryChanged();
        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotId) {
        return inventory.getStackInSlotOnClosing(slotId);
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        inventory.setInventorySlotContents(slotId, itemstack);
        onInventoryChanged();
    }

    protected void onInventoryChanged() {
        if(isSendUpdateOnInventoryChanged())
            sendUpdate();
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return inventory.hasCustomName();
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(getPos()) == this && entityPlayer.getDistanceSq(getPos().add(0.5D, 0.5D, 0.5D)) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
        inventory.openInventory(playerIn);
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
        inventory.closeInventory(playerIn);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return inventory.isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return inventory.getFieldCount();
    }

    @Override
    public void clear() {
        inventory.clear();;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        inventory.readFromNBT(data);
    }

    @Override
    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        inventory.writeToNBT(data);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return slotSides.get(side);
    }
    
    private boolean canAccess(int slot, EnumFacing side) {
        boolean canAccess = false;
        for(int slotAccess : getSlotsForFace(side)) {
            if(slotAccess == slot)
                canAccess = true;
        }
        return canAccess;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, EnumFacing side) {
        return canAccess(slot, side) && this.isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, EnumFacing side) {
        return canAccess(slot, side);
    }

    /**
     * If this tile should send blockState updates when the inventory has changed.
     * @return If it should send blockState updates.
     */
    public boolean isSendUpdateOnInventoryChanged() {
        return sendUpdateOnInventoryChanged;
    }

    /**
     * If this tile should send blockState updates when the inventory has changed.
     * @param sendUpdateOnInventoryChanged If it should send blockState updates.
     */
    public void setSendUpdateOnInventoryChanged(
            boolean sendUpdateOnInventoryChanged) {
        this.sendUpdateOnInventoryChanged = sendUpdateOnInventoryChanged;
    }
    
}
