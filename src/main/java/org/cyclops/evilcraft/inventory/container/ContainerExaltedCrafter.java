package org.cyclops.evilcraft.inventory.container;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.inventory.container.ItemInventoryContainer;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.gui.container.GuiExaltedCrafter;
import org.cyclops.evilcraft.core.inventory.NBTCraftingGrid;
import org.cyclops.evilcraft.item.ExaltedCrafter;
import org.cyclops.evilcraft.item.ExaltedCrafterConfig;
import org.cyclops.evilcraft.network.packet.ExaltedCrafterButtonPacket;

import java.util.List;
import java.util.Map;

/**
 * Container for the {@link ExaltedCrafter}.
 * @author rubensworks
 *
 */
@ChestContainer
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

    private static final Map<Integer, IButtonAction> ACTIONS = Maps.newHashMap();
    static {
        ACTIONS.put(GuiExaltedCrafter.BUTTON_CLEAR, new IButtonAction() {
            @Override
            public void execute(ContainerExaltedCrafter container) {
                container.clearGrid();
            }
        });
        ACTIONS.put(GuiExaltedCrafter.BUTTON_BALANCE, new IButtonAction() {
            @Override
            public void execute(ContainerExaltedCrafter container) {
                container.balanceGrid();
            }
        });
        ACTIONS.put(GuiExaltedCrafter.BUTTON_TOGGLERETURN, new IButtonAction() {
            @Override
            public void execute(ContainerExaltedCrafter container) {
                container.setReturnToInnerInventory(!container.isReturnToInnerInventory());
            }
        });
    }

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
     * Send a packet to the server for pressing a button.
     * @param buttonId The id of the button.
     */
    public void sendPressButton(int buttonId) {
        if(ACTIONS.containsKey(buttonId)) {
            executePressButton(buttonId);
            EvilCraft._instance.getPacketHandler().sendToServer(new ExaltedCrafterButtonPacket(buttonId));
        }
    }

    /**
     * Send a packet to the server for pressing a button.
     * @param buttonId The id of the button.
     */
    public void executePressButton(int buttonId) {
        if(ACTIONS.containsKey(buttonId)) {
            ACTIONS.get(buttonId).execute(this);
        }
    }
    
    /**
     * Clear the crafting grid.
     */
    public void clearGrid() {
    	for(int i = 0; i < craftingGrid.getSizeInventory(); i++) {
    		transferStackInSlot(player, i);
    	}
    }

    /**
     * Balance the crafting grid.
     */
    @SuppressWarnings("unchecked")
    public void balanceGrid() {
        // Init bins
        List<Pair<ItemStack, List<Pair<Integer, Integer>>>> bins = Lists.newArrayListWithExpectedSize(craftingGrid.getSizeInventory());
        for(int slot = 0; slot < craftingGrid.getSizeInventory(); slot++) {
            ItemStack itemStack = craftingGrid.getStackInSlot(slot);
            if(itemStack != null) {
                int amount = itemStack.stackSize;
                itemStack = itemStack.copy();
                itemStack.stackSize = 1;
                int bin = 0;
                boolean addedToBin = false;
                while(bin < bins.size() && !addedToBin) {
                    Pair<ItemStack, List<Pair<Integer, Integer>>> pair = bins.get(bin);
                    ItemStack original = pair.getLeft().copy();
                    original.stackSize = 1;
                    if(ItemStack.areItemStacksEqual(original, itemStack)) {
                        pair.getLeft().stackSize += amount;
                        pair.getRight().add(new MutablePair<Integer, Integer>(slot, 0));
                        addedToBin = true;
                    }
                    bin++;
                }

                if(!addedToBin) {
                    itemStack.stackSize = amount;
                    bins.add(new MutablePair<ItemStack, List<Pair<Integer, Integer>>>(itemStack,
                            Lists.newArrayList((Pair<Integer, Integer>) new MutablePair<Integer, Integer>(slot, 0))));
                }
            }
        }

        // Balance bins
        for(Pair<ItemStack, List<Pair<Integer, Integer>>> pair : bins) {
            int division = pair.getLeft().stackSize / pair.getRight().size();
            int modulus = pair.getLeft().stackSize % pair.getRight().size();
            for(Pair<Integer, Integer> slot : pair.getRight()) {
                slot.setValue(division + Math.max(0, Math.min(1, modulus--)));
            }
        }

        // Set bins to slots
        for(Pair<ItemStack, List<Pair<Integer, Integer>>> pair : bins) {
            for(Pair<Integer, Integer> slot : pair.getRight()) {
                ItemStack itemStack = pair.getKey().copy();
                itemStack.stackSize = slot.getRight();
                craftingGrid.setInventorySlotContents(slot.getKey(), itemStack);
            }
        }
    }

    public boolean isReturnToInnerInventory() {
        ItemStack itemStack = getItemStack(player);
        return itemStack != null && ExaltedCrafter.getInstance().isReturnToInner(itemStack);
    }

    protected void setReturnToInnerInventory(boolean returnToInner) {
        ItemStack itemStack = getItemStack(player);
        if(itemStack != null) {
            ExaltedCrafter.getInstance().setReturnToInner(itemStack, returnToInner);
        }
    }
    
    @Override
    protected int getSlotStart(int originSlot, int slotStart, boolean reverse) {
    	if(!reverse && !ExaltedCrafterConfig.shiftCraftingGrid) {
    		// Avoid shift clicking with as target the crafting grid (+ result).
    		return 10;
    	} else if(reverse && originSlot < 10) {
            if(isReturnToInnerInventory()) {
                // Shift clicking from the crafting grid (+ result) should first go to the inner inventory.
                return 1 + GRID_ROWS * GRID_COLUMNS;
            } else {
                return slotStart;
            }
    	}
    	return super.getSlotStart(originSlot, slotStart, reverse);
    }
    
    @Override
    protected int getSlotRange(int originSlot, int slotRange, boolean reverse) {
    	if(isReturnToInnerInventory() && reverse && originSlot < 10) {
    		// Shift clicking from the crafting grid (+ result) should first go to the inner inventory.
    		return getSizeInventory();
    	} else {
            return slotRange;
        }
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

    /**
     * @return Container selection options for inventory tweaks.
     */
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getContainerSelection() {
        Map<ContainerSection, List<Slot>> selection = Maps.newHashMap();
        List<Slot> craftingInSlots = Lists.newLinkedList();
        List<Slot> craftingOutSlots = Lists.newLinkedList();
        List<Slot> craftingChest = Lists.newLinkedList();
        for(int i = 0; i < 9; i++) {
            craftingInSlots.add(this.getSlot(i));
        }
        for(int i = 10; i < 10 + CHEST_INVENTORY_ROWS * CHEST_INVENTORY_COLUMNS; i++) {
            craftingChest.add(this.getSlot(i));
        }
        selection.put(ContainerSection.CRAFTING_IN_PERSISTENT, craftingInSlots);
        selection.put(ContainerSection.CRAFTING_OUT, craftingOutSlots);
        selection.put(ContainerSection.CHEST, craftingChest);
        return selection;

    }

    /**
     * Action for pressing buttons.
     * Easily expandable for more containers.
     * Might want to further abstract Gui logic related to buttons in that case as well.
     */
    private static interface IButtonAction {

        public void execute(ContainerExaltedCrafter container);

    }
    
}