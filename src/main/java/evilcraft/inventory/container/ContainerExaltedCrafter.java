package evilcraft.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import evilcraft.block.BloodChest;
import evilcraft.core.inventory.NBTCraftingGrid;
import evilcraft.core.inventory.container.ItemInventoryContainer;
import evilcraft.item.ExaltedCrafter;

/**
 * Container for the {@link BloodChest}.
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
    private NBTCraftingGrid craftingGrid;
    private InventoryCraftResult result;
    private boolean initialized;

    /**
     * Make a new instance.
     * @param player The player.
     */
    public ContainerExaltedCrafter(EntityPlayer player) {
        super(player.inventory, ExaltedCrafter.getInstance());
        initialized = false;
        this.world = player.worldObj;
        this.result = new InventoryCraftResult();
        this.craftingGrid = new NBTCraftingGrid(player, player.getCurrentEquippedItem(), this);
        this.addCraftingGrid(player, craftingGrid);
        this.addInventory(player.getInventoryEnderChest(), 0, CHEST_INVENTORY_OFFSET_X, CHEST_INVENTORY_OFFSET_Y,
        		CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS);
        this.addPlayerInventory(player.inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
        initialized = true;
        this.onCraftMatrixChanged(craftingGrid);
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