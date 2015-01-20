package evilcraft.client.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.helper.RenderHelpers;
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
        for(HyperLink link : currentSection.getLinks(page)) {
            this.buttonList.add(new TextOverlayButton(nextId++, link, left + link.getX(), top + link.getY(),
                    InfoSection.getFontHeight(getFontRenderer())));
        }
    }

    @Override
    public void drawScreen(int f, int x, float y) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(texture);

        drawTexturedModalRect(left, top, 0, 0, pageWidth, guiHeight);
        drawTexturedModalRectMirrored(left + pageWidth - 1, top, 0, 0, pageWidth, guiHeight);
        currentSection.drawScreen(this, left, top, pageWidth, guiHeight, page);
        currentSection.drawScreen(this, left + pageWidth - 1, top, pageWidth, guiHeight, page + 1);
        super.drawScreen(f, x, y);
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
        currentSection.bakeSection(getFontRenderer(), pageWidth, (guiHeight - 2 * InfoSection.Y_OFFSET - 5) / lineHeight, lineHeight);
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
        } else {
            super.actionPerformed(button);
        }
        this.initGui();
        if(goToLastPage) {
            page = currentSection.getPages() - 1;
        }
    }

    public void drawHorizontalRule(int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x - HR_WIDTH / 2, y - HR_HEIGHT / 2, 52, 180, HR_WIDTH, HR_HEIGHT);
        GL11.glDisable(GL11.GL_BLEND);
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

                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, width, height);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class TextOverlayButton extends GuiButton {

        @Getter private HyperLink link;

        public TextOverlayButton(int id, HyperLink link, int x, int y, int height) {
            super(id, x, y, 0, height, link.getTarget().getLocalizedTitle());
            this.link = link;
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            boolean oldUnicode = fontRenderer.getUnicodeFlag();
            fontRenderer.setUnicodeFlag(true);
            this.width = fontRenderer.getStringWidth(displayString);
            fontRenderer.setUnicodeFlag(oldUnicode);
        }

        /**
         * Draws this button to the screen.
         */
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
