package evilcraft.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.slot.SlotFluidContainer;
import evilcraft.gui.slot.SlotInfuse;
import evilcraft.gui.slot.SlotRemoveOnly;

public class ContainerBloodInfuser extends ExtendedContainer {
    
    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;

    private IInventory playerIInventory;
    private TileBloodInfuser tile;
    
    private int infuseTick = 0;

    public ContainerBloodInfuser(InventoryPlayer inventory, TileBloodInfuser tile) {
        super(tile.getInventorySize());
        playerIInventory = inventory;
        this.tile = tile;

        // TODO Nicer GUI (make it more thaumcrafty than buildcrafty)

        // Adding inventory
        addSlotToContainer(new SlotFluidContainer(tile, TileBloodInfuser.SLOT_CONTAINER, 8, 36)); // Container emptier
        addSlotToContainer(new SlotInfuse(tile, TileBloodInfuser.SLOT_INFUSE, 115, 36, tile)); // Infuse slot
        addSlotToContainer(new SlotRemoveOnly(tile, TileBloodInfuser.SLOT_INFUSE_RESULT, 130, 36)); // Infuse result slot

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);

    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tile.canInteractWith(entityPlayer);
    }
    
    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, this.tile.infuseTick);
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.infuseTick != this.tile.infuseTick) {
                icrafting.sendProgressBarUpdate(this, 0, this.tile.infuseTick);
            }
        }
        this.infuseTick = this.tile.infuseTick;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        if (index == 0) {
            this.tile.infuseTick = value;
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = null;
        Slot slot = (Slot) inventorySlots.get(slotID);
        int slots = tile.getInventorySize();
        
        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if(slotID < slots) {
                if(!mergeItemStack(stackInSlot, slots, inventorySlots.size(), true)) {
                    return null;
                }
            } else if(!mergeItemStack(stackInSlot, 0, slots, false)) {
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
    protected boolean mergeItemStack(ItemStack stack, int slotStart, int slotRange, boolean reverse)
    {
            boolean successful = false;
            int slotIndex = slotStart;
            int maxStack = Math.min(stack.getMaxStackSize(), tile.getInventoryStackLimit());
            
            if(reverse)
            {
                    slotIndex = slotRange - 1;
            }
            
            Slot slot;
            ItemStack existingStack;
            
            if(stack.isStackable())
            {
                    while(stack.stackSize > 0 && (!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart))
                    {
                            slot = (Slot)this.inventorySlots.get(slotIndex);
                            existingStack = slot.getStack();
                            
                            if(slot.isItemValid(stack) && existingStack != null && existingStack.itemID == stack.itemID && (!stack.getHasSubtypes() || stack.getItemDamage() == existingStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, existingStack))
                            {
                                    int existingSize = existingStack.stackSize + stack.stackSize;
                                    
                                    if(existingSize <= maxStack)
                                    {
                                            stack.stackSize = 0;
                                            existingStack.stackSize = existingSize;
                                            slot.onSlotChanged();
                                            successful = true;
                                    }
                                    else if (existingStack.stackSize < maxStack)
                                    {
                                            stack.stackSize -= maxStack - existingStack.stackSize;
                                            existingStack.stackSize = maxStack;
                                            slot.onSlotChanged();
                                            successful = true;
                                    }
                            }
                            
                            if(reverse)
                            {
                                    --slotIndex;
                            }
                            else
                            {
                                    ++slotIndex;
                            }
                    }
            }
            
            if(stack.stackSize > 0)
            {
                    if(reverse)
                    {
                            slotIndex = slotRange - 1;
                    }
                    else
                    {
                            slotIndex = slotStart;
                    }
                    
                    while(!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart)
                    {
                            slot = (Slot)this.inventorySlots.get(slotIndex);
                            existingStack = slot.getStack();
                            
                            if(slot.isItemValid(stack) && existingStack == null)
                            {
                                    slot.putStack(stack.copy());
                                    slot.onSlotChanged();
                                    stack.stackSize = 0;
                                    successful = true;
                                    break;
                            }
                            
                            if(reverse)
                            {
                                    --slotIndex;
                            }
                            else
                            {
                                    ++slotIndex;
                            }
                    }
            }
            
            return successful;
    }
}