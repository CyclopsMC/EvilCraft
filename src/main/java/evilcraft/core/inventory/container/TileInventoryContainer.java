package evilcraft.core.inventory.container;

import evilcraft.core.inventory.IGuiContainerProvider;
import evilcraft.core.tileentity.InventoryTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * A container for a tile entity with inventory.
 * @author rubensworks
 *
 * @param <T> The type of tile.
 */
public class TileInventoryContainer<T extends InventoryTileEntity> extends ExtendedInventoryContainer {
    
    protected T tile;

    /**
     * Make a new TileInventoryContainer.
     * @param inventory The player inventory.
     * @param tile The TileEntity for this container.
     */
    public TileInventoryContainer(InventoryPlayer inventory, T tile) {
        super(inventory, (IGuiContainerProvider) tile.getBlock());
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tile.canInteractWith(entityPlayer);
    }
    
    /**
     * @return The tile entity.
     */
    public T getTile() {
    	return tile;
    }

	@Override
	protected int getSizeInventory() {
		return getTile().getSizeInventory();
	}
    
}
