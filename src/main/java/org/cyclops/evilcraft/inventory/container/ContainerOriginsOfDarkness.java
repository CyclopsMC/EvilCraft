package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import org.cyclops.cyclopscore.inventory.container.ItemInventoryContainer;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Container for the On the Dynamics of Integration book.
 * @author rubensworks
 */
public class ContainerOriginsOfDarkness extends ItemInventoryContainer {

    public ContainerOriginsOfDarkness(int id, Inventory inventory, FriendlyByteBuf packetBuffer) {
        this(id, inventory, readItemIndex(packetBuffer), readHand(packetBuffer));
    }

    public ContainerOriginsOfDarkness(int id, Inventory playerInventory, int itemIndex, InteractionHand hand) {
        super(RegistryEntries.CONTAINER_ORIGINS_OF_DARKNESS, id, playerInventory, itemIndex, hand);
    }

    @Override
    protected int getSizeInventory() {
        return 0;
    }
}
