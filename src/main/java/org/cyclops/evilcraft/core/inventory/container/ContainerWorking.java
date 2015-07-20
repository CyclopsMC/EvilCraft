package org.cyclops.evilcraft.core.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;

/**
 * A container for a working tile entity.
 * @author rubensworks
 *
 * @param <T> The {@link WorkingTileEntity} class.
 */
public abstract class ContainerWorking<T extends WorkingTileEntity<T, ?>>
	extends TickingTankInventoryContainer<T>{

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerWorking(InventoryPlayer inventory, T tile) {
        super(inventory, tile);
    }
	
}
