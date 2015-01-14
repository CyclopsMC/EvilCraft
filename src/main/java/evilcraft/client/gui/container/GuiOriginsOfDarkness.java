package evilcraft.client.gui.container;

import evilcraft.Reference;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.item.OriginsOfDarkness;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Gui for the Origins of Darkness book.
 * @author rubensworks
 */
public class GuiOriginsOfDarkness extends GuiScreen {

    protected final ItemStack itemStack;

    protected static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, OriginsOfDarkness.getInstance().getGuiTexture());

    private int guiWidth = 146;
    private int guiHeight = 180;
    private int left, top;

    public GuiOriginsOfDarkness(EntityPlayer player, int itemIndex) {
        itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex);
    }

    @Override
    public void initGui() {
        super.initGui();

        left = (width - guiWidth) / 2;
        top = (height - guiHeight) / 2;
    }

    @Override
    public void drawScreen(int f, int x, float y) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
