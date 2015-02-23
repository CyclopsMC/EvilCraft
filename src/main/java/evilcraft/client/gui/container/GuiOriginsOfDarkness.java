package evilcraft.client.gui.container;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.algorithm.EvictingStack;
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
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Gui for the Origins of Darkness book.
 * @author rubensworks
 */
public class GuiOriginsOfDarkness extends GuiScreen {

    protected static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, OriginsOfDarkness.getInstance().getGuiTexture());
    private static final int BUTTON_NEXT = 1;
    private static final int BUTTON_PREVIOUS = 2;
    private static final int BUTTON_PARENT = 3;
    private static final int BUTTON_BACK = 4;
    private static final int BUTTON_HYPERLINKS_START = 5;

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
    private NextPageButton buttonBack;

    private static InfoSection currentSection;
    private static int page;

    private static EvictingStack<InfoSection.Location> history = new EvictingStack<InfoSection.Location>(128);
    private InfoSection nextSection;
    private int nextPage;
    private boolean goToLastPage;

    private int guiWidth = 283;
    private int pageWidth = 142;
    private int guiHeight = 180;
    private int left, top;

    public GuiOriginsOfDarkness(EntityPlayer player, int itemIndex) {
        itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex);
        if(currentSection == null) {
            currentSection = InfoBookRegistry.getInstance().getRoot();
            page = 0;
        }
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
        this.buttonList.add(this.buttonBack = new NextPageButton(BUTTON_BACK, left + pageWidth + 127, top + 2, 0, 223, 13, 18));
        this.updateGui();

        if (goToLastPage) {
            page = Math.max(0, currentSection.getPages() - 2);
            page += page % 2;
        }

        int nextId = BUTTON_HYPERLINKS_START;
        for(int innerPage = page; innerPage <= page + 1; innerPage++) {
            for (HyperLink link : currentSection.getLinks(innerPage)) {
                int xOffset = innerPage % 2 == 1 ? X_OFFSET_INNER + pageWidth : X_OFFSET_OUTER;
                this.buttonList.add(new TextOverlayButton(nextId++, link, left + xOffset + link.getX(), top + InfoSection.Y_OFFSET / 2 + link.getY(),
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
        super.drawScreen(x, y, f);
        currentSection.postDrawScreen(this, x0, top, width, guiHeight, page, x, y);
        currentSection.postDrawScreen(this, x1, top, width, guiHeight, page + 1, x, y);
    }

    public void drawTexturedModalRectMirrored(int x, int y, int u, int v, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (x + 0), (double) (y + height), (double) this.zLevel, (double) ((float) (u + width) * f), (double) ((float) (v + height) * f1));
        tessellator.addVertexWithUV((double) (x + width), (double) (y + height), (double) this.zLevel, (double) ((float) (u + 0) * f), (double) ((float) (v + height) * f1));
        tessellator.addVertexWithUV((double) (x + width), (double) (y + 0), (double) this.zLevel, (double) ((float) (u + 0) * f), (double) ((float) (v + 0) * f1));
        tessellator.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) this.zLevel, (double) ((float) (u + width) * f), (double) ((float) (v + 0) * f1));
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
        int width = pageWidth - X_OFFSET_TOTAL;
        int lineHeight = InfoSection.getFontHeight(getFontRenderer());
        int maxLines = (guiHeight - 2 * InfoSection.Y_OFFSET - 5) / lineHeight;

        // Bake current and all reachable sections.
        List<InfoSection> infoSectionsToBake = Lists.newLinkedList();
        infoSectionsToBake.add(currentSection);
        getPreviousSections(infoSectionsToBake);
        getNextSections(infoSectionsToBake);
        for(InfoSection infoSection : infoSectionsToBake) {
            if(infoSection != null) infoSection.bakeSection(getFontRenderer(), width, maxLines, lineHeight);
        }

        updateButtons();
        mc.fontRenderer.setUnicodeFlag(oldUnicode);
    }

    protected void getPreviousSections(List<InfoSection> sections) {
        InfoSection.Location location = currentSection.getPrevious(page, false);
        if(location != null) {
            sections.add(location.getInfoSection());
        }
    }

    protected void getNextSections(List<InfoSection> sections) {
        InfoSection.Location location = currentSection.getNext(page + 1, false);
        if(location != null) {
            sections.add(location.getInfoSection());
        }
    }

    private void updateButtons() {
        this.buttonNextPage.visible = currentSection.getNext(page + 1, false) != null;
        this.buttonPreviousPage.visible = currentSection.getPrevious(page, false) != null;
        this.buttonParent.visible = currentSection != null && currentSection.getParent() != null;
        this.buttonBack.visible = history.currentSize() > 0;
    }

    protected void actionPerformed(GuiButton button) {
        goToLastPage = false;
        nextSection = currentSection;
        nextPage = page;
        if(button.id == BUTTON_NEXT && button.visible) {
            InfoSection.Location location = currentSection.getNext(page + 1, MinecraftHelpers.isShifted());
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            history.push(new InfoSection.Location(page, currentSection));
        } else if(button.id == BUTTON_PREVIOUS && button.visible) {
            InfoSection.Location location = currentSection.getPrevious(page, MinecraftHelpers.isShifted());
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            // We can not set the new 'page', because the currentSection hasn't been baked yet and we do not know the last page yet.
            goToLastPage = nextSection != currentSection && !MinecraftHelpers.isShifted();
            history.push(new InfoSection.Location(page, currentSection));
        } else if(button.id == BUTTON_PARENT && button.visible) {
            if(MinecraftHelpers.isShifted()) {
                nextSection = currentSection.getParent();
                while(nextSection.getParent() != null) {
                    nextSection = nextSection.getParent();
                }
            } else {
                nextSection = currentSection.getParent();
            }
            nextPage = 0;
            history.push(new InfoSection.Location(page, currentSection));
        } else if(button.id == BUTTON_BACK && button.visible && history.currentSize() > 0) {
            InfoSection.Location location = history.pop();
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
        } else if(button instanceof TextOverlayButton) {
            nextSection = ((TextOverlayButton) button).getLink().getTarget();
            nextPage = 0;
            if(nextSection != currentSection) history.push(new InfoSection.Location(page, currentSection));
        } else if(button instanceof AdvancedButton && ((AdvancedButton) button).isVisible()) {
            nextSection = ((AdvancedButton) button).getTarget();
            nextPage = 0;
            if(nextSection != currentSection) history.push(new InfoSection.Location(page, currentSection));
        } else {
            super.actionPerformed(button);
        }
    }

    protected void mouseClicked(int x, int y, int p_73864_3_) {
        super.mouseClicked(x, y, p_73864_3_);
        if(p_73864_3_ == 0 && (nextSection != null && (nextSection != currentSection || page != nextPage))) {
            currentSection = nextSection;
            nextSection = null;
            page = nextPage;
            this.initGui();
        }
    }

    public void drawScaledCenteredString(String string, int x, int y, int width, float originalScale, int maxWidth, int color) {
        float originalWidth = getFontRenderer().getStringWidth(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(string, x, y, width, scale, color);
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

    @Override
    public void updateScreen() {
        super.updateScreen();

        if(!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
            this.mc.thePlayer.closeScreen();
        }
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

        @Override
        public void func_146113_a(SoundHandler soundHandler) {
            soundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation(Reference.MOD_ID, "pageFlip"), 1.0F));
        }

    }

    @SideOnly(Side.CLIENT)
    static class TextOverlayButton extends GuiButton {

        @Getter private HyperLink link;

        public TextOverlayButton(int id, HyperLink link, int x, int y, int height) {
            super(id, x, y, 0, height, InfoSection.formatString(L10NHelpers.localize(link.getUnlocalizedName())));
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

        @Override
        public void func_146113_a(SoundHandler soundHandler) {
            soundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation(Reference.MOD_ID, "pageFlip"), 1.0F));
        }

    }

}
