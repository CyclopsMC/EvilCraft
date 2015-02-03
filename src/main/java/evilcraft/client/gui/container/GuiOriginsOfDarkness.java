package evilcraft.client.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.infobook.AdvancedButton;
import evilcraft.infobook.HyperLink;
import evilcraft.infobook.InfoBookRegistry;
import evilcraft.infobook.InfoSection;
import evilcraft.item.OriginsOfDarkness;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Gui for the Origins of Darkness book.
 * @author rubensworks
 */
public class GuiOriginsOfDarkness extends GuiScreen {

    protected static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, OriginsOfDarkness.getInstance().getGuiTexture());
    private static final int BUTTON_NEXT = 1;
    private static final int BUTTON_PREVIOUS = 2;
    private static final int BUTTON_PARENT = 3;
    private static final int BUTTON_HYPERLINKS_START = 4;

    private static final int HR_WIDTH = 88;
    private static final int HR_HEIGHT = 10;
    public static final int BANNER_WIDTH = 91;
    private static final int BANNER_HEIGHT = 12;
    private static final int ARROW_WIDTH = 22;
    private static final int ARROW_HEIGHT = 13;

    private static final int BORDER_CORNER = 4;
    private static final int BORDER_WIDTH = 2;
    private static final int BORDER_X = 0;
    private static final int BORDER_Y = 206;

    public static final int X_OFFSET_OUTER = 20;
    public static final int X_OFFSET_INNER = 7;
    public static final int X_OFFSET_TOTAL = X_OFFSET_OUTER + X_OFFSET_INNER;

    protected final ItemStack itemStack;

    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private NextPageButton buttonParent;

    private InfoSection currentSection;
    private int page;
    private int guiWidth = 283;
    private int pageWidth = 142;
    private int guiHeight = 180;
    private int left, top;

    public GuiOriginsOfDarkness(EntityPlayer player, int itemIndex) {
        itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex);
        currentSection = InfoBookRegistry.getInstance().getRoot();
        page = 0;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();

        left = (width - guiWidth) / 2;
        top = (height - guiHeight) / 2;

        this.buttonList.add(this.buttonNextPage = new NextPageButton(BUTTON_NEXT, left + pageWidth + 100, top + 156, 0, 180, 18, 13));
        this.buttonList.add(this.buttonPreviousPage = new NextPageButton(BUTTON_PREVIOUS, left + 23, top + 156, 0, 193, 18, 13));
        this.buttonList.add(this.buttonParent = new NextPageButton(BUTTON_PARENT, left + 2, top + 2, 36, 180, 8, 8));
        this.updateGui();
        int nextId = BUTTON_HYPERLINKS_START;
        for(int innerPage = page; innerPage <= page + 1; innerPage++) {
            for (HyperLink link : currentSection.getLinks(innerPage)) {
                int xOffset = innerPage % 2 == 1 ? X_OFFSET_INNER : X_OFFSET_OUTER;
                this.buttonList.add(new TextOverlayButton(nextId++, link, left + xOffset + link.getX(), top + link.getY(),
                        InfoSection.getFontHeight(getFontRenderer())));
            }
            this.buttonList.addAll(currentSection.getAdvancedButtons(innerPage));
        }
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(texture);

        drawTexturedModalRect(left, top, 0, 0, pageWidth, guiHeight);
        drawTexturedModalRectMirrored(left + pageWidth - 1, top, 0, 0, pageWidth, guiHeight);
        int x0 = left + X_OFFSET_OUTER;
        int x1 = left + pageWidth - 1 + X_OFFSET_INNER;
        int width = pageWidth - X_OFFSET_TOTAL;
        currentSection.drawScreen(this, x0, top, width, guiHeight, page, x, y);
        currentSection.drawScreen(this, x1, top, width, guiHeight, page + 1, x, y);
        currentSection.postDrawScreen(this, x0, top, width, guiHeight, page, x, y);
        currentSection.postDrawScreen(this, x1, top, width, guiHeight, page + 1, x, y);
        super.drawScreen(x, y, f);
    }

    public void drawTexturedModalRectMirrored(int x, int y, int u, int v, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((float)(u + width) * f), (double)((float)(v + height) * f1));
        tessellator.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(u + 0) * f), (double)((float)(v + height) * f1));
        tessellator.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)((float)(u + 0) * f), (double)((float)(v + 0) * f1));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(u + width) * f), (double)((float)(v + 0) * f1));
        tessellator.draw();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }

    private void updateGui() {
        boolean oldUnicode = mc.fontRenderer.getUnicodeFlag();
        mc.fontRenderer.setUnicodeFlag(true);
        int lineHeight = InfoSection.getFontHeight(getFontRenderer());
        currentSection.bakeSection(getFontRenderer(), pageWidth - X_OFFSET_TOTAL, (guiHeight - 2 * InfoSection.Y_OFFSET - 5) / lineHeight, lineHeight);
        updateButtons();
        mc.fontRenderer.setUnicodeFlag(oldUnicode);
    }

    private void updateButtons() {
        this.buttonNextPage.visible = currentSection != null && ((page < currentSection.getPages() - 2) ||
                currentSection.getSubSections() > 0 ||
                !currentSection.isRoot() && currentSection.getParent().getSubSections() > currentSection.getChildIndex() + 1);
        this.buttonPreviousPage.visible = currentSection != null && (page > 0 ||
                !currentSection.isRoot() && currentSection.getChildIndex() >= 0);
        this.buttonParent.visible = currentSection != null && currentSection.getParent() != null;
    }

    protected void actionPerformed(GuiButton button) {
        boolean goToLastPage = false;
        if(button.id == BUTTON_NEXT && button.visible) {
            if(page < currentSection.getPages() - 2 && !MinecraftHelpers.isShifted()) {
                page+=2;
            } else if(currentSection.getSubSections() > 0) {
                currentSection = currentSection.getSubSection(0);
                page = 0;
            } else if(!currentSection.isRoot() && currentSection.getParent().getSubSections() > currentSection.getChildIndex() + 1) {
                currentSection = currentSection.getParent().getSubSection(currentSection.getChildIndex() + 1);
                page = 0;
            }
        } else if(button.id == BUTTON_PREVIOUS && button.visible) {
            if (page > 0) {
                page-=2;
            } else if (currentSection.getChildIndex() == 0) {
                currentSection = currentSection.getParent();
                page = 0;
            } else {
                currentSection = currentSection.getParent().getSubSection(currentSection.getChildIndex() - 1);
                goToLastPage = !MinecraftHelpers.isShifted();
                // We can not set the new 'page', because the currentSection hasn't been baked yet.
            }
        } else if(button.id == BUTTON_PARENT) {
            if(MinecraftHelpers.isShifted()) {
                while(currentSection.getParent() != null) {
                    currentSection = currentSection.getParent();
                }
            } else {
                currentSection = currentSection.getParent();
            }
            page = 0;
        } else if(button instanceof TextOverlayButton) {
            currentSection = ((TextOverlayButton) button).getLink().getTarget();
            page = 0;
        } else if(button instanceof AdvancedButton && ((AdvancedButton) button).isVisible()) {
            currentSection = ((AdvancedButton) button).getTarget();
            page = 0;
        } else {
            super.actionPerformed(button);
        }
        if(goToLastPage) {
            page = Math.max(0, currentSection.getPages() - 2);
            page += page % 2;
        }
        this.initGui();
    }

    public void drawScaledCenteredString(String string, int x, int y, int width, float scale, int color) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, 1.0f);
        int titleLength = fontRendererObj.getStringWidth(string);
        int titleHeight = fontRendererObj.FONT_HEIGHT;
        fontRendererObj.drawString(string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color);
        GL11.glPopMatrix();
    }

    public void drawHorizontalRule(int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x - HR_WIDTH / 2, y - HR_HEIGHT / 2, 52, 180, HR_WIDTH, HR_HEIGHT);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawTextBanner(int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x - BANNER_WIDTH / 2, y - BANNER_HEIGHT / 2, 52, 191, BANNER_WIDTH, BANNER_HEIGHT);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawArrowRight(int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 210, ARROW_WIDTH, ARROW_HEIGHT);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawOuterBorder(int x, int y, int width, int height) {
        drawOuterBorder(x, y, width, height, 1, 1, 1, 1);
    }

    public void drawOuterBorder(int x, int y, int width, int height, float r, float g, float b, float alpha) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, alpha);
        mc.getTextureManager().bindTexture(texture);

        // Corners
        this.drawTexturedModalRect(x - BORDER_WIDTH, y - BORDER_WIDTH, BORDER_X, BORDER_Y, BORDER_CORNER, BORDER_CORNER);
        this.drawTexturedModalRect(x + width - BORDER_WIDTH, y - BORDER_WIDTH, BORDER_X + BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER);
        this.drawTexturedModalRect(x - BORDER_WIDTH, y + height - BORDER_WIDTH, BORDER_X + 3 * BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER);
        this.drawTexturedModalRect(x + width - BORDER_WIDTH, y + height - BORDER_WIDTH, BORDER_X + 2 * BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER);

        // Sides
        for(int i = BORDER_WIDTH; i < width - BORDER_WIDTH; i+=BORDER_WIDTH) {
            int drawWidth = BORDER_WIDTH;
            if(i + BORDER_WIDTH >= width - BORDER_CORNER) {
                drawWidth -= i - (width - BORDER_CORNER);
            }
            this.drawTexturedModalRect(x + i, y - BORDER_WIDTH, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, drawWidth, BORDER_WIDTH);
            this.drawTexturedModalRect(x + i, y + height, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, drawWidth, BORDER_WIDTH);
        }
        for(int i = BORDER_WIDTH; i < height - BORDER_WIDTH; i+=BORDER_WIDTH) {
            int drawHeight = BORDER_WIDTH;
            if(i + BORDER_WIDTH >= height - BORDER_CORNER) {
                drawHeight -= i - (height - BORDER_CORNER);
            }
            if(drawHeight > 0) {
                this.drawTexturedModalRect(x - BORDER_WIDTH, y + i, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, BORDER_WIDTH, drawHeight);
                this.drawTexturedModalRect(x + width, y + i, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, BORDER_WIDTH, drawHeight);
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    public void renderToolTip(ItemStack itemStack, int x, int y) {
        super.renderToolTip(itemStack, x, y);
    }

    public int getTick() {
        return (int) mc.theWorld.getWorldTime();
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton {

        private int x, y;

        public NextPageButton(int id, int xPosition, int yPosition, int x, int y, int width, int height) {
            super(id, xPosition, yPosition, width, height, "");
            this.x = x;
            this.y = y;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
            if (this.visible) {
                boolean isHover = mouseX >= this.xPosition && mouseY >= this.yPosition &&
                               mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                minecraft.getTextureManager().bindTexture(texture);
                int k = x;
                int l = y;

                if (isHover) {
                    k += width;
                }

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, width, height);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class TextOverlayButton extends GuiButton {

        @Getter private HyperLink link;

        public TextOverlayButton(int id, HyperLink link, int x, int y, int height) {
            super(id, x, y, 0, height, L10NHelpers.localize(link.getUnlocalizedName()));
            this.link = link;
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            boolean oldUnicode = fontRenderer.getUnicodeFlag();
            fontRenderer.setUnicodeFlag(true);
            this.width = fontRenderer.getStringWidth(displayString);
            fontRenderer.setUnicodeFlag(oldUnicode);
        }

        @Override
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
            if (this.visible) {
                boolean isHover = mouseX >= this.xPosition && mouseY >= this.yPosition &&
                        mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                boolean oldUnicode = minecraft.fontRenderer.getUnicodeFlag();
                minecraft.fontRenderer.setUnicodeFlag(true);
                minecraft.fontRenderer.drawString((isHover ? "§n" : "") +
                                displayString + "§r", xPosition, yPosition,
                        RenderHelpers.RGBToInt(isHover ? 100 : 0, isHover ? 100 : 0, isHover ? 150 : 125));
                minecraft.fontRenderer.setUnicodeFlag(oldUnicode);
            }
        }

    }

}
