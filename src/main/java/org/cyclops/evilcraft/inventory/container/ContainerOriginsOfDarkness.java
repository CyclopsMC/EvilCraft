package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import org.cyclops.cyclopscore.inventory.container.ItemInventoryContainer;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Container for the On the Dynamics of Integration book.
 * @author rubensworks
 */
public class ContainerOriginsOfDarkness extends ItemInventoryContainer {

    public ContainerOriginsOfDarkness(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        this(id, inventory, readItemIndex(packetBuffer), readHand(packetBuffer));
    }

    public ContainerOriginsOfDarkness(int id, PlayerInventory playerInventory, int itemIndex, Hand hand) {
        super(RegistryEntries.CONTAINER_ORIGINS_OF_DARKNESS, id, playerInventory, itemIndex, hand);
    }

    @Override
    protected int getSizeInventory() {
        return 0;
    }
}
