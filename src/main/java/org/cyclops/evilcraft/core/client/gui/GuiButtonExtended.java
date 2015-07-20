package org.cyclops.evilcraft.core.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

/**
 * An extended {@link GuiButton} which is better resizable.
 * Based on chickenbones' GuiNEIButton.
 * @author rubensworks
 *
 */
public class GuiButtonExtended extends GuiButton {

	/**
	 * Make a new instance.
	 * @param id The ID.
	 * @param x X
	 * @param y Y
	 * @param width Width
	 * @param height Height
	 * @param string The string to print.
	 */
	public GuiButtonExtended(int id, int x, int y,
			int width, int height, String string) {
		super(id, x, y, width, height, string);
	}

	protected String getDisplayString() {
		return this.displayString;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int i, int j) {
        if(visible) {
	        FontRenderer fontrenderer = minecraft.fontRendererObj;
	        minecraft.renderEngine.bindTexture(buttonTextures);
	        GlStateManager.color(1, 1, 1, 1);
	        
	        boolean mouseOver = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
	        int k = getHoverState(mouseOver);
	        
	        drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, width / 2, height / 2);//top left
	        drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height / 2);//top right
	        drawTexturedModalRect(xPosition, yPosition + height / 2, 0, 46 + k * 20 + 20 - height / 2, width / 2, height / 2);//bottom left
	        drawTexturedModalRect(xPosition + width / 2, yPosition + height / 2, 200 - width / 2, 46 + k * 20 + 20 - height / 2, width / 2, height / 2);//bottom right
	        mouseDragged(minecraft, i, j);
	        
	        int color = 0xe0e0e0;
	        if(!enabled) {
	        	color = 0xffa0a0a0;
	        } else if (mouseOver) {
	        	color = 0xffffa0;
	        }
	        
	        drawCenteredString(fontrenderer, getDisplayString(), xPosition + width / 2, yPosition + (height - 8) / 2, color);
        }
    }

}
