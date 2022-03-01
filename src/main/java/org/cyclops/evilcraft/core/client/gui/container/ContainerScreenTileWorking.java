package org.cyclops.evilcraft.core.client.gui.container;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import org.cyclops.evilcraft.client.gui.container.WidgetUpgradeTab;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;

/**
 * A GUI container that has support for the display of {@link BlockEntityWorking}.
 * @author rubensworks
 *
 * @param <T> The {@link BlockEntityWorking} class, mostly just the extension class.
 */
public abstract class ContainerScreenTileWorking<C extends ContainerTileWorking<T>, T extends BlockEntityWorking<T, ?>>
        extends ContainerScreenContainerTankInventory<C, T>
        implements WidgetUpgradeTab.SlotEnabledCallback {

    public static final int UPGRADES_OFFSET_X = 28;

    private WidgetUpgradeTab upgrades;

    public ContainerScreenTileWorking(C container, Inventory playerInventory, Component title) {
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
        return getMenu().getMaxProgress(0) > 0;
    }
    
    @Override
    protected int getProgressXScaled(int width) {
        return (int) Math.ceil((float)(getMenu().getProgress(0)) / (float)getMenu().getMaxProgress(0) * (float)width);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float f, int x, int y) {
        super.renderBg(matrixStack, f, x, y);
        upgrades.drawBackground(matrixStack, leftPos, topPos);
    }

}
