package evilcraft.infobook.pageelement;

import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Images that can be added to sections.
 * @author rubensworks
 */
public class ImageAppendix extends SectionAppendix {

    private static final int OFFSET_Y = 0;

    private ResourceLocation resource;
    private int width;
    private int height;

    public ImageAppendix(ResourceLocation resource, int width, int height) {
        this.resource = resource;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getHeight() {
        return height + 2 * OFFSET_Y;
    }

    @Override
    public void drawScreen(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
        int xc = x + width / 2 - this.width / 2;
        int yc = y + OFFSET_Y;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        gui.drawTexturedModalRect(xc, yc, 0, 0, this.width, this.height);
        GL11.glDisable(GL11.GL_BLEND);

        gui.drawOuterBorder(xc, yc, this.width, this.height, 0.5F, 0.5F, 0.5F, 0.4f);
    }
}
