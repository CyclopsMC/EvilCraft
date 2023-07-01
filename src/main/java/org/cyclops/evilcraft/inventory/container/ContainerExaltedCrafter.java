package org.cyclops.evilcraft.inventory.container;

import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.inventory.container.ItemInventoryContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.inventory.NBTCraftingGrid;
import org.cyclops.evilcraft.item.ItemExaltedCrafter;
import org.cyclops.evilcraft.item.ItemExaltedCrafterConfig;

import java.util.List;

/**
 * Container for the {@link ItemExaltedCrafter}.
 * @author rubensworks
 *
 */
public class ContainerExaltedCrafter extends ItemInventoryContainer<ItemExaltedCrafter> {

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

    public static final String BUTTON_CLEAR = "clear";
    public static final String BUTTON_BALANCE = "balance";
    public static final String BUTTON_TOGGLERETURN = "toggleReturn";

    private final Level world;
    private final NBTCraftingGrid craftingGrid;
    private final ResultContainer result;
    private boolean initialized;

    public ContainerExaltedCrafter(int id, Inventory inventory, FriendlyByteBuf packetBuffer) {
        this(id, inventory, ItemLocation.readFromPacketBuffer(packetBuffer));
    }

    public ContainerExaltedCrafter(int id, Inventory inventory, ItemLocation itemLocation) {
        super(RegistryEntries.CONTAINER_EXALTED_CRAFTER, id, inventory, itemLocation);
        initialized = false;
        this.world = player.level();
        this.result = new ResultContainer();
        this.craftingGrid = new NBTCraftingGrid(player, itemLocation, this);

        this.addCraftingGrid(player, craftingGrid);
        this.addInventory(getItem().getSupplementaryInventory(player, itemLocation),
                0, CHEST_INVENTORY_OFFSET_X, CHEST_INVENTORY_OFFSET_Y,
                CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS);
        this.addPlayerInventory(player.getInventory(), INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);

        initialized = true;
        this.slotsChanged(craftingGrid);

        putButtonAction(BUTTON_CLEAR, (buttonId, container) -> this.clearGrid());
        putButtonAction(BUTTON_BALANCE, (buttonId, container) -> this.balanceGrid());
        putButtonAction(BUTTON_TOGGLERETURN, (buttonId, container) -> ((ContainerExaltedCrafter)container).setReturnToInnerInventory(!((ContainerExaltedCrafter)container).isReturnToInnerInventory()));
    }

    /**
     * Clear the crafting grid.
     */
    public void clearGrid() {
        for(int i = 0; i < craftingGrid.getContainerSize(); i++) {
            quickMoveStack(player, i);
        }
    }

    /**
     * Balance the crafting grid.
     */
    @SuppressWarnings("unchecked")
    public void balanceGrid() {
        // Init bins
        List<Pair<ItemStack, List<Pair<Integer, Integer>>>> bins = Lists.newArrayListWithExpectedSize(craftingGrid.getContainerSize());
        for(int slot = 0; slot < craftingGrid.getContainerSize(); slot++) {
            ItemStack itemStack = craftingGrid.getItem(slot);
            if(!itemStack.isEmpty()) {
                int amount = itemStack.getCount();
                itemStack = itemStack.copy();
                itemStack.setCount(1);
                int bin = 0;
                boolean addedToBin = false;
                while(bin < bins.size() && !addedToBin) {
                    Pair<ItemStack, List<Pair<Integer, Integer>>> pair = bins.get(bin);
                    ItemStack original = pair.getLeft().copy();
                    original.setCount(1);
                    if(ItemStack.matches(original, itemStack)) {
                        pair.getLeft().grow(amount);
                        pair.getRight().add(new MutablePair<Integer, Integer>(slot, 0));
                        addedToBin = true;
                    }
                    bin++;
                }

                if(!addedToBin) {
                    itemStack.setCount(amount);
                    bins.add(new MutablePair<>(itemStack, Lists.newArrayList(new MutablePair<>(slot, 0))));
                }
            }
        }

        // Balance bins
        for(Pair<ItemStack, List<Pair<Integer, Integer>>> pair : bins) {
            int division = pair.getLeft().getCount() / pair.getRight().size();
            int modulus = pair.getLeft().getCount() % pair.getRight().size();
            for(Pair<Integer, Integer> slot : pair.getRight()) {
                slot.setValue(division + Math.max(0, Math.min(1, modulus--)));
            }
        }

        // Set bins to slots
        for(Pair<ItemStack, List<Pair<Integer, Integer>>> pair : bins) {
            for(Pair<Integer, Integer> slot : pair.getRight()) {
                ItemStack itemStack = pair.getKey().copy();
                itemStack.setCount(slot.getRight());
                craftingGrid.setItem(slot.getKey(), itemStack);
            }
        }
    }

    public boolean isReturnToInnerInventory() {
        ItemStack itemStack = getItemStack(player);
        return !itemStack.isEmpty() && ItemExaltedCrafter.isReturnToInner(itemStack);
    }

    protected void setReturnToInnerInventory(boolean returnToInner) {
        ItemStack itemStack = getItemStack(player);
        if(!itemStack.isEmpty()) {
            ItemExaltedCrafter.setReturnToInner(itemStack, returnToInner);
        }
    }

    @Override
    protected int getSlotStart(int originSlot, int slotStart, boolean reverse) {
        if(!reverse && !ItemExaltedCrafterConfig.shiftCraftingGrid) {
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

    protected void addCraftingGrid(Player player, NBTCraftingGrid grid) {
        this.addInventory(grid, 0, GRID_OFFSET_X, GRID_OFFSET_Y, GRID_ROWS, GRID_COLUMNS);
        this.addSlot(new ResultSlot(player, grid, result, 0, 124, 35));
    }

    @Override
    protected int getSizeInventory() {
        return 1 + (GRID_ROWS * GRID_COLUMNS) + (CHEST_INVENTORY_ROWS * CHEST_INVENTORY_COLUMNS);
    }

    @Override
    public void slotsChanged(Container inventory) {
        if(initialized && !this.world.isClientSide()) {
            ItemStack itemstack = ItemStack.EMPTY;

            // Slightly altered logic from Container#slotChangedCraftingGrid
            CraftingRecipe irecipe = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingGrid, world).orElse(null);
            if (irecipe != null && result.setRecipeUsed(world, (ServerPlayer) player, irecipe)) {
                itemstack = irecipe.assemble(craftingGrid, world.registryAccess());
            }
            result.setItem(0, itemstack);
            ((ServerPlayer) this.player).connection.send(new ClientboundContainerSetSlotPacket(this.containerId, getStateId(), 9, itemstack));

            craftingGrid.save();
        }
    }
}
