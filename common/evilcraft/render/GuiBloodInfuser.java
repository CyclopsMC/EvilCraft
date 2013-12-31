package evilcraft.render;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import evilcraft.Reference;
import evilcraft.entities.tileentities.TileBloodInfuser;

public class GuiBloodInfuser extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "bloodInfuser_gui.png");

    public GuiBloodInfuser(InventoryPlayer inventory, TileBloodInfuser tile) {
        super(new ContainerBloodInfuser(inventory, tile));
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(TEXTURE);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        super.drawGuiContainerForegroundLayer(par1, par2);
    }
}
