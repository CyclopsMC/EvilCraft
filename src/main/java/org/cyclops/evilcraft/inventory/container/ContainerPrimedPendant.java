package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
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

    public ContainerPrimedPendant(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        this(id, inventory, readItemIndex(packetBuffer), readHand(packetBuffer));
    }

    public ContainerPrimedPendant(int id, PlayerInventory inventory, int itemIndex, Hand hand) {
        super(RegistryEntries.CONTAINER_PRIMED_PENDANT, id, inventory, itemIndex, hand);
        addSlot(new SlotSingleItem(getItem().getSupplementaryInventory(player, getItemStack(player), itemIndex, hand),
                0, SLOT_X, SLOT_Y, Items.POTION));
        this.addPlayerInventory(player.inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    @Override
    protected int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}