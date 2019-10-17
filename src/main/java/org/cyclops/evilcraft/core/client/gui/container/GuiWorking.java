package org.cyclops.evilcraft.core.client.gui.container;

import org.cyclops.evilcraft.client.gui.container.GuiUpgradeTab;
import org.cyclops.evilcraft.core.inventory.container.TickingTankInventoryContainer;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.tileentity.TileWorking;

/**
 * A GUI container that has support for the display of {@link WorkingTileEntity}.
 * @author rubensworks
 *
 * @param <T> The {@link WorkingTileEntity} class, mostly just the extension class.
 */
public abstract class GuiWorking<T extends TileWorking<?, ?>> extends GuiContainerTankInventory<T>
        implements GuiUpgradeTab.SlotEnabledCallback {

    public static final int UPGRADES_OFFSET_X = 28;

    private GuiUpgradeTab upgrades;

	/**
     * Make a new instance.
     * @param container The container to make the GUI for.
     * @param tile The tile entity to make the GUI for.
     */
	public GuiWorking(TickingTankInventoryContainer container, T tile) {
		super(container, tile);
        this.upgrades = new GuiUpgradeTab(this, this);
        this.offsetX = UPGRADES_OFFSET_X;
	}

    @Override
    public boolean isSlotEnabled(int upgradeSlotId) {
        return tile.isUpgradeSlotEnabled(tile.getBasicInventorySize() + upgradeSlotId);
    }
	
	@Override
    protected boolean isShowProgress() {
        return getContainer().getMaxProgress(0) > 0;
    }
    
    @Override
    protected int getProgressXScaled(int width) {
        return (int) Math.ceil((float)(getContainer().getProgress(0)) / (float)getContainer().getMaxProgress(0) * (float)width);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        upgrades.drawBackground(guiLeft, guiTop);
    }

}
