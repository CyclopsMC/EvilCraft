package evilcraft.core.client.gui.container;

import evilcraft.core.helper.RenderHelpers;
import net.minecraft.util.ResourceLocation;

/**
 * Simple tab gui element.
 * @author rubensworks
 */
public abstract class GuiTab {

    private int width;
    private int height;
    private int posX;
    private int posY;
    private int u;
    private int v;
    private GuiContainerExtended gui;

    public GuiTab(int width, int height, int posX, int posY, int u, int v, GuiContainerExtended gui) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.u = u;
        this.v = v;
        this.gui = gui;
    }

    protected abstract ResourceLocation getResourceLocation();

    /**
     * Draw the tab.
     * @param x Origin X.
     * @param y Origin Y.
     */
    public void drawBackground(int x, int y) {
        RenderHelpers.bindTexture(getResourceLocation());
        gui.drawTexturedModalRect(x + posX, y + posY, u, v, width, height);
    }

}
