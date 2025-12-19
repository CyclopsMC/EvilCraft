package org.cyclops.evilcraft.core.client.gui.container;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/**
 * Simple tab gui element.
 * @author rubensworks
 */
public abstract class WidgetTab {

    private int width;
    private int height;
    protected int posX;
    protected int posY;
    private int u;
    private int v;
    protected AbstractContainerScreen gui;

    public WidgetTab(int width, int height, int posX, int posY, int u, int v, AbstractContainerScreen gui) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.u = u;
        this.v = v;
        this.gui = gui;
    }

    protected abstract Identifier getIdentifier();

    /**
     * Draw the tab.
     * @param guiGraphics The matrix stack.
     * @param x Origin X.
     * @param y Origin Y.
     */
    public void drawBackground(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, getIdentifier(), x + posX, y + posY, u, v, width, height, 256, 256);
    }

}
