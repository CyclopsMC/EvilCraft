package evilcraft.infobook.pageelement;

import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.infobook.InfoSection;
import lombok.Data;
import org.lwjgl.opengl.GL11;

/**
 * Separate elements that can be appended to sections.
 * @author rubensworks
 */
@Data public abstract class SectionAppendix {

    private int page;
    private int lineStart;

    public SectionAppendix() {

    }

    /**
     * @return The full height of this element with offsets.
     */
    public int getFullHeight() {
        return getHeight() + getOffsetY() * 2;
    }

    protected abstract int getOffsetY();
    protected abstract int getWidth();
    protected abstract int getHeight();

    /**
     * Draw the appendix.
     * @param gui The gui.
     * @param x Start X.
     * @param y Start Y.
     * @param width Max width.
     * @param height Max height.
     * @param page Current page.
     * @param mx Mouse X.
     * @param my Mouse Y.
     * @param pre If the normal drawing should occur, otherwise post-drawing: things like tooltips.
     */
    public void drawScreen(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my, boolean pre) {
        int xc = x + width / 2 - getWidth() / 2;
        int yc = y + getOffsetY();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        if(pre) {
            drawElement(gui, xc, yc, getWidth(), getHeight(), page, mx, my);
        } else {
            postDrawElement(gui, xc, yc, getWidth(), getHeight(), page, mx, my);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected abstract void drawElement(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my);
    protected abstract void postDrawElement(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my);

    public abstract void preBakeElement(InfoSection infoSection);
    /**
     * Bake this appendix, only called once before changing pages.
     * @param infoSection The section this appendix is part of.
     */
    public abstract void bakeElement(InfoSection infoSection);

}
