package evilcraft.inventory.container;

import evilcraft.block.BloodChest;
import evilcraft.core.inventory.container.ItemInventoryContainer;
import evilcraft.core.inventory.slot.SlotSingleItem;
import evilcraft.item.PrimedPendant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

/**
 * Container for the {@link BloodChest}.
 * @author rubensworks
 *
 */
public class ContainerPrimedPendant extends ItemInventoryContainer<PrimedPendant> {

    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 83;
    private static final int SLOT_X = 80;
    private static final int SLOT_Y = 35;

    public ContainerPrimedPendant(EntityPlayer player, int itemIndex) {
        super(player.inventory, PrimedPendant.getInstance(), itemIndex);

        addSlotToContainer(new SlotSingleItem(getItem().getSupplementaryInventory(player, getItemStack(player), itemIndex),
                0, SLOT_X, SLOT_Y, Items.potionitem));
        this.addPlayerInventory(player.inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    @Override
    protected int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}