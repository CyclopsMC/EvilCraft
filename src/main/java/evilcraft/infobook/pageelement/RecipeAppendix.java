package evilcraft.infobook.pageelement;

import com.google.common.collect.Lists;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.infobook.InfoSection;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

/**
 * Recipes that can be added to sections.
 * @author rubensworks
 */
public abstract class RecipeAppendix<T> extends SectionAppendix {

    protected static final int SLOT_SIZE = 16;
    protected static final int TICK_DELAY = 30;

    protected T recipe;

    private List<RenderItemHolder> renderItemHolders = Lists.newLinkedList();

    public RecipeAppendix(T recipe) {
        this.recipe = recipe;
    }

    protected int getTick(GuiOriginsOfDarkness gui) {
        return gui.getTick() / TICK_DELAY;
    }

    protected ItemStack prepareItemStacks(List<ItemStack> itemStacks, int tick) {
        return prepareItemStack(itemStacks.get(tick % itemStacks.size()).copy(), tick);
    }

    protected ItemStack prepareItemStack(ItemStack itemStack, int tick) {
        if(itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            List<ItemStack> itemStacks = Lists.newLinkedList();
            itemStack.getItem().getSubItems(itemStack.getItem(), null, itemStacks);
            if(itemStacks.isEmpty()) return itemStack;
            return itemStacks.get(tick % itemStacks.size());
        }
        return itemStack;
    }

    protected void renderItem(GuiOriginsOfDarkness gui, int x, int y, ItemStack itemStack, int mx, int my) {
        renderItem(gui, x, y, itemStack, mx, my, true);
    }

    protected void renderItem(GuiOriginsOfDarkness gui, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays) {
        if(renderOverlays) gui.drawOuterBorder(x, y, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);

        RenderItem renderItem = RenderItem.getInstance();
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        renderItem.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemStack, x, y);
        if(renderOverlays) renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();

        if(renderOverlays) renderItemHolders.add(new RenderItemHolder(x, y, itemStack));
    }

    protected void renderIcon(GuiOriginsOfDarkness gui, int x, int y, IIcon icon) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderItem.getInstance().renderIcon(x, y, icon, SLOT_SIZE, SLOT_SIZE);RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }

    protected void renderItemTooltip(GuiOriginsOfDarkness gui, int x, int y, ItemStack itemStack, int mx, int my) {
        GL11.glPushMatrix();
        if(mx >= x && my >= y && mx <= x + SLOT_SIZE && my <= y + SLOT_SIZE) {
            gui.renderToolTip(itemStack, mx, my);
        }
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    protected int getHeight() {
        return getHeightInner() + getAdditionalHeight();
    }

    protected abstract int getHeightInner();

    protected int getAdditionalHeight() {
        return 5;
    }

    @Override
    protected int getOffsetY() {
        return getAdditionalHeight();
    }

    protected abstract String getUnlocalizedTitle();

    @Override
    public final void drawElement(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        int yOffset = getAdditionalHeight();
        gui.drawOuterBorder(x - 1, y - 1 - yOffset, getWidth() + 2, getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(x + width / 2, y - 2 - yOffset);
        float originalScale = 0.9f;
        float originalWidth = gui.getFontRenderer().getStringWidth(getUnlocalizedTitle()) * originalScale;
        int maxWidth = GuiOriginsOfDarkness.BANNER_WIDTH - 2;
        float scale = Math.min(originalScale, maxWidth / originalWidth);

        gui.drawScaledCenteredString(L10NHelpers.localize(getUnlocalizedTitle()), x, y - 2 - yOffset, width, scale, RenderHelpers.RGBToInt(120, 20, 30));

        drawElementInner(gui, x, y, width, height, page, mx, my);
    }

    protected void postDrawElement(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        renderToolTips(gui, mx, my);
    }

    protected abstract void drawElementInner(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my);

    protected void renderToolTips(GuiOriginsOfDarkness gui, int mx, int my) {
        for(RenderItemHolder renderItemHolder : renderItemHolders) {
            renderItemTooltip(gui, renderItemHolder.getX(), renderItemHolder.getY(), renderItemHolder.getItemStack(), mx, my);
        }
        renderItemHolders.clear();
    }

    @Override
    public void bakeElement(InfoSection infoSection) {

    }

    @Data
    @AllArgsConstructor
    private static class RenderItemHolder {

        private int x, y;
        private ItemStack itemStack;

    }

}
