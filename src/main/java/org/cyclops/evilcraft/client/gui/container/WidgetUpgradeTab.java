package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.client.gui.container.WidgetTab;

/**
 * Tab for upgrades in guis.
 * @author rubensworks
 */
public class WidgetUpgradeTab extends WidgetTab {

    private ResourceLocation texture;
    private SlotEnabledCallback slotEnabledCallback;

    public WidgetUpgradeTab(ContainerScreen gui, SlotEnabledCallback slotEnabledCallback) {
        super(28, 82, 0, 0, 0, 0, gui);
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "gui_upgrades.png");
        this.slotEnabledCallback = slotEnabledCallback;
    }

    @Override
    protected ResourceLocation getResourceLocation() {
        return texture;
    }

    /**
     * Draw the tab.
     * @param x Origin X.
     * @param y Origin Y.
     */
    public void drawBackground(int x, int y) {
        super.drawBackground(x, y);
        for(int i = 0; i < 4; i++) {
            if(slotEnabledCallback != null && !slotEnabledCallback.isSlotEnabled(i)) {
                gui.blit(x + posX + 5, y + posY + 5 + 18 * i, 28, 0, 18, 18);
            }
        }
    }

    public static interface SlotEnabledCallback {

        public boolean isSlotEnabled(int upgradeSlotId);

    }
}
