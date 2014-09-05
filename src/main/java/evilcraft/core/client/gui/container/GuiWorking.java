package evilcraft.core.client.gui.container;

import net.minecraft.inventory.Container;
import evilcraft.core.tileentity.WorkingTileEntity;

/**
 * A GUI container that has support for the display of {@link WorkingTileEntity}.
 * @author rubensworks
 *
 * @param <T> The {@link WorkingTileEntity} class, mostly just the extension class.
 */
public class GuiWorking<T extends WorkingTileEntity<?>> extends GuiContainerTankInventory<T> {

	/**
     * Make a new instance.
     * @param container The container to make the GUI for.
     * @param tile The tile entity to make the GUI for.
     */
	public GuiWorking(Container container, T tile) {
		super(container, tile);
	}
	
	@Override
    protected boolean isShowProgress() {
        return tile.isWorking();
    }
    
    @Override
    protected int getProgressScaled(int scale) {
        return tile.getWorkTickScaled(24);
    }

}
