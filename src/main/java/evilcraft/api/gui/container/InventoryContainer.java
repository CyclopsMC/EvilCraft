package evilcraft.api.gui.container;

import evilcraft.api.entities.tileentitites.InventoryTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A container for a tile entity with inventory.
 * @author rubensworks
 *
 * @param <T>
 */
public class InventoryContainer<T extends InventoryTileEntity> extends Container{
    
    protected static final int ITEMBOX = 18;
    
    private IInventory playerIInventory;
    protected T tile;

    /**
     * Make a new InventoryContainer.
     * @param inventory The player inventory.
     * @param tile The TileEntity for this container.
     */
    public InventoryContainer(InventoryPlayer inventory, T tile) {
        this.playerIInventory = inventory;
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tile.canInteractWith(entityPlayer);
    }
    
    /**
     * Add player inventory and hotbar to the GUI.
     * @param inventory Inventory of the player
     * @param offsetX Offset to X
     * @param offsetY Offset to Y
     */
    protected void addPlayerInventory(InventoryPlayer inventory, int offsetX, int offsetY) {
        // Player inventory
        int rows = 3;
        int cols = 9;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                // Slot params: id, x-coord, y-coord (coords are relative to gui box)
                addSlotToContainer(new Slot(inventory, x + (y + 1) * cols, offsetX + x * ITEMBOX, offsetY + y * ITEMBOX));
            }
        }
        
        // Player hotbar
        offsetY += 58;
        for (int x = 0; x < cols; x++) {
            addSlotToContainer(new Slot(inventory, x, offsetX + x * ITEMBOX, offsetY));
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = null;
        Slot slot = (Slot) inventorySlots.get(slotID);
        int slots = tile.getSizeInventory();
        
        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if(slotID < slots) { // Click in tile -> player inventory
                if(!mergeItemStack(stackInSlot, slots, inventorySlots.size(), true)) {
                    return null;
                }
            } else if(!mergeItemStack(stackInSlot, 0, slots, false)) { // Click in player inventory -> tile
                return null;
            }
            
            if(stackInSlot.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if(stackInSlot.stackSize == stack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, stackInSlot);
        }
        
        return stack;
    }
    
    @Override
    protected boolean mergeItemStack(ItemStack stack, int slotStart, int slotRange, boolean reverse) {
            boolean successful = false;
            int slotIndex = slotStart;
            int maxStack = Math.min(stack.getMaxStackSize(), tile.getSizeInventory());
            
            if(reverse) {
                    slotIndex = slotRange - 1;
            }
            
            Slot slot;
            ItemStack existingStack;
            
            if(stack.isStackable()) {
                    while(stack.stackSize > 0 && (!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart)) {
                            slot = (Slot)this.inventorySlots.get(slotIndex);
                            existingStack = slot.getStack();
                            
                            if(slot.isItemValid(stack) && existingStack != null && existingStack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == existingStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, existingStack)) {
                                    int existingSize = existingStack.stackSize + stack.stackSize;
                                    
                                    if(existingSize <= maxStack) {
                                            stack.stackSize = 0;
                                            existingStack.stackSize = existingSize;
                                            slot.onSlotChanged();
                                            successful = true;
                                    } else if (existingStack.stackSize < maxStack) {
                                            stack.stackSize -= maxStack - existingStack.stackSize;
                                            existingStack.stackSize = maxStack;
                                            slot.onSlotChanged();
                                            successful = true;
                                    }
                            }
                            
                            if(reverse) {
                                    --slotIndex;
                            } else {
                                    ++slotIndex;
                            }
                    }
            }
            
            if(stack.stackSize > 0) {
                    if(reverse) {
                            slotIndex = slotRange - 1;
                    } else {
                            slotIndex = slotStart;
                    }
                    
                    while(!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart) {
                            slot = (Slot)this.inventorySlots.get(slotIndex);
                            existingStack = slot.getStack();
                            
                            if(slot.isItemValid(stack) && existingStack == null) {
                                    slot.putStack(stack.copy());
                                    slot.onSlotChanged();
                                    stack.stackSize = 0;
                                    successful = true;
                                    break;
                            }
                            
                            if(reverse) {
                                    --slotIndex;
                            } else {
                                    ++slotIndex;
                            }
                    }
            }
            
            return successful;
    }

    /**
     * Get the inventory of the player for which this container is instantiated.
     * @return The player inventory.
     */
    public IInventory getPlayerIInventory() {
        return playerIInventory;
    }
    
}
