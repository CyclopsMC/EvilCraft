package evilcraft.infobook.pageelement;

import com.google.common.collect.Lists;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
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

    public RecipeAppendix(T recipe) {
        this.recipe = recipe;
    }

    @Override
    public int getHeight() {
        return 58;
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
        gui.drawOuterBorder(x, y, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);

        RenderItem renderItem = RenderItem.getInstance();
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        renderItem.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemStack, x, y);
        renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
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

}
