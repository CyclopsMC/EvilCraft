package evilcraft.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.core.inventory.NBTCraftingGrid;
import evilcraft.core.inventory.container.ItemInventoryContainer;
import evilcraft.item.ExaltedCrafter;
import evilcraft.network.PacketHandler;
import evilcraft.network.packet.ExaltedCrafterClearPacket;

/**
 * Container for the {@link ExaltedCrafter}.
 * @author rubensworks
 *
 */
public class ContainerExaltedCrafter extends ItemInventoryContainer<ExaltedCrafter> {
    
	private static final int GRID_OFFSET_X = 30;
    private static final int GRID_OFFSET_Y = 17;
    private static final int GRID_ROWS = 3;
    private static final int GRID_COLUMNS = 3;
    
    private static final int CHEST_INVENTORY_OFFSET_X = 8;
    private static final int CHEST_INVENTORY_OFFSET_Y = 84;
    private static final int CHEST_INVENTORY_ROWS = 3;
    private static final int CHEST_INVENTORY_COLUMNS = 9;
	
    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 143;
    
    private World world;
    private EntityPlayer player;
    private NBTCraftingGrid craftingGrid;
    private InventoryCraftResult result;
    private boolean initialized;

    /**
     * Make a new instance.
     * @param player The player.
     * @param itemIndex The index of the item in use inside the player inventory.
     */
    public ContainerExaltedCrafter(EntityPlayer player, int itemIndex) {
        super(player.inventory, ExaltedCrafter.getInstance(), itemIndex);
        initialized = false;
        this.world = player.worldObj;
        this.player = player;
        this.result = new InventoryCraftResult();
        this.craftingGrid = new NBTCraftingGrid(player, itemIndex, this);
        
        this.addCraftingGrid(player, craftingGrid);
        this.addInventory(getItem().getSupplementaryInventory(player, InventoryHelpers.getItemFromIndex(player, itemIndex), itemIndex),
        		0, CHEST_INVENTORY_OFFSET_X, CHEST_INVENTORY_OFFSET_Y,
        		CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS);
        this.addPlayerInventory(player.inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
        
        initialized = true;
        this.onCraftMatrixChanged(craftingGrid);
    }
    
    /**
     * Send a packet to the server for clearing the grid.
     */
    public void sendClearGrid() {
    	clearGrid();
    	PacketHandler.sendToServer(new ExaltedCrafterClearPacket());
    }
    
    /**
     * Clear the crafting grid.
     */
    public void clearGrid() {
    	for(int i = 0; i < craftingGrid.getSizeInventory(); i++) {
    		transferStackInSlot(player, i);
    	}
    }
    
    @Override
    protected int getSlotStart(int originSlot, int slotStart, boolean reverse) {
    	if(!reverse) { // Avoid shift clicking with as target the crafting grid (+ result).
    		return 10;
    	} else if(reverse && originSlot < 10) { // Shift clicking from the crafting grid (+ result) should first go to the inner inventory.
    		return 1 + GRID_ROWS * GRID_COLUMNS;
    	}
    	return super.getSlotStart(originSlot, slotStart, reverse);
    }
    
    @Override
    protected int getSlotRange(int originSlot, int slotRange, boolean reverse) {
    	if(reverse && originSlot < 10) { // Shift clicking from the crafting grid (+ result) should first go to the inner inventory.
    		return getSizeInventory();
    	}
    	return slotRange;
    }
    
    protected void addCraftingGrid(EntityPlayer player, NBTCraftingGrid grid) {
    	this.addInventory(grid, 0, GRID_OFFSET_X, GRID_OFFSET_Y, GRID_ROWS, GRID_COLUMNS);
    	this.addSlotToContainer(new SlotCrafting(player, grid, result, 0, 124, 35));
    }

	@Override
	protected int getSizeInventory() {
		return 1 + (GRID_ROWS * GRID_COLUMNS) + (CHEST_INVENTORY_ROWS * CHEST_INVENTORY_COLUMNS);
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		if(initialized) {
			result.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftingGrid, world));
			craftingGrid.save();
		}
    }
    
}