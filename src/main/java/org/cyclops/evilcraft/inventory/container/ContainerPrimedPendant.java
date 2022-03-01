package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import org.cyclops.cyclopscore.inventory.container.ItemInventoryContainer;
import org.cyclops.cyclopscore.inventory.slot.SlotSingleItem;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemPrimedPendant;

/**
 * Container for the {@link ItemPrimedPendant}.
 * @author rubensworks
 *
 */
public class ContainerPrimedPendant extends ItemInventoryContainer<ItemPrimedPendant> {

    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 83;
    private static final int SLOT_X = 80;
    private static final int SLOT_Y = 35;

    public ContainerPrimedPendant(int id, Inventory inventory, FriendlyByteBuf packetBuffer) {
        this(id, inventory, readItemIndex(packetBuffer), readHand(packetBuffer));
    }

    public ContainerPrimedPendant(int id, Inventory inventory, int itemIndex, InteractionHand hand) {
        super(RegistryEntries.CONTAINER_PRIMED_PENDANT, id, inventory, itemIndex, hand);
        addSlot(new SlotSingleItem(getItem().getSupplementaryInventory(player, getItemStack(player), itemIndex, hand),
                0, SLOT_X, SLOT_Y, Items.POTION));
        this.addPlayerInventory(player.getInventory(), INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    @Override
    protected int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}