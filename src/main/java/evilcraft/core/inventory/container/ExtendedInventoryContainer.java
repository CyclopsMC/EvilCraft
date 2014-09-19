package evilcraft.core.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import evilcraft.core.inventory.IGuiContainerProvider;

/**
 * An extended container.
 * @author rubensworks
 */
public abstract class ExtendedInventoryContainer extends InventoryContainer {
	
	protected IGuiContainerProvider guiProvider;

	/**
	 * Make a new instance.
	 * @param inventory The player inventory.
	 * @param guiProvider The gui provider.
	 */
	public ExtendedInventoryContainer(InventoryPlayer inventory, IGuiContainerProvider guiProvider) {
		super(inventory);
		this.guiProvider = guiProvider;
	}

	/**
	 * Get the gui provider.
	 * @return The gui provider.
	 */
	public IGuiContainerProvider getGuiProvider() {
		return guiProvider;
	}
	
}
