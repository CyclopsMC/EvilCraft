package org.cyclops.evilcraft.inventory.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.inventory.container.ItemInventoryContainer;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Container for the On the Dynamics of Integration book.
 * @author rubensworks
 */
public class ContainerOriginsOfDarkness extends ItemInventoryContainer {

    public ContainerOriginsOfDarkness(int id, Inventory inventory, FriendlyByteBuf packetBuffer) {
        this(id, inventory, ItemLocation.readFromPacketBuffer(packetBuffer));
    }

    public ContainerOriginsOfDarkness(int id, Inventory playerInventory, ItemLocation itemLocation) {
        super(RegistryEntries.CONTAINER_ORIGINS_OF_DARKNESS, id, playerInventory, itemLocation);
    }

    @Override
    protected int getSizeInventory() {
        return 0;
    }
}
