package org.cyclops.evilcraft.core.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import org.cyclops.evilcraft.client.gui.container.WidgetUpgradeTab;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.tileentity.TileWorking;

/**
 * A GUI container that has support for the display of {@link TileWorking}.
 * @author rubensworks
 *
 * @param <T> The {@link TileWorking} class, mostly just the extension class.
 */
public abstract class ContainerScreenTileWorking<C extends ContainerTileWorking<T>, T extends TileWorking<T, ?>>
        extends ContainerScreenContainerTankInventory<C, T>
        implements WidgetUpgradeTab.SlotEnabledCallback {

    public static final int UPGRADES_OFFSET_X = 28;

    private WidgetUpgradeTab upgrades;

    public ContainerScreenTileWorking(C container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.upgrades = new WidgetUpgradeTab(this, this);
        this.offsetX = UPGRADES_OFFSET_X;
	}

    @Override
    public boolean isSlotEnabled(int upgradeSlotId) {
        return container.isUpgradeSlotEnabled(upgradeSlotId);
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
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(matrixStack, f, x, y);
        upgrades.drawBackground(matrixStack, guiLeft, guiTop);
    }

}
