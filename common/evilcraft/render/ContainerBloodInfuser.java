package evilcraft.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import evilcraft.entities.tileentities.TileBloodInfuser;

public class ContainerBloodInfuser extends ExtendedContainer {
    
    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;

    private IInventory playerIInventory;
    private TileBloodInfuser tile;

    public ContainerBloodInfuser(InventoryPlayer inventory, TileBloodInfuser tile) {
        super(tile.getInventorySize());
        playerIInventory = inventory;
        this.tile = tile;
        
        // Adding tank display
        // TODO (make it more thaumcrafty than buildcrafty)

        // Adding inventory
        addSlotToContainer(new Slot(tile, 0, 8, 36)); // Container emptier
        addSlotToContainer(new Slot(tile, 1, 115, 36)); // Infuse slot

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);

    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tile.canInteractWith(entityPlayer);
    }
}