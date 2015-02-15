package evilcraft.core.client.gui.container;

import evilcraft.Reference;
import evilcraft.core.inventory.container.ExtendedInventoryContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.List;

/**
 * An extended GUI container.
 * @author rubensworks
 */
public class GuiContainerExtended extends GuiContainer {
	
    protected ResourceLocation texture;
    protected int offsetX = 0;
    protected int offsetY = 0;

    /**
     * Make a new instance.
     * @param container The container to make the GUI for.
     */
    public GuiContainerExtended(ExtendedInventoryContainer container) {
        super(container);
        this.texture = new ResourceLocation(Reference.MOD_ID, container.getGuiProvider().getGuiTexture());
    }

    @Override
    public void initGui() {
        this.xSize = getBaseXSize() + offsetX * 2;
        this.ySize = getBaseYSize() + offsetY * 2;
        super.initGui();
    }

    protected int getBaseXSize() {
        return 176;
    }

    protected int getBaseYSize() {
        return 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft + offsetX, guiTop + offsetY, 0, 0, xSize - 2 * offsetX, ySize - 2 * offsetY);
    }
    
    protected boolean isPointInRegion(Rectangle region, Point mouse) {
    	return isPointInRegion(region.x, region.y, region.width, region.height, mouse.x, mouse.y);
    }
    
    protected boolean isPointInRegion(int x, int y, int width, int height, int mouseX, int mouseY) {
    	// MCP isPointInRegion
    	return func_146978_c(x, y, width, height, mouseX, mouseY);
    }
    
    protected void drawTooltip(List<String> lines, int x, int y) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        
        int tooltipWidth = 0;
        int tempWidth;
        int xStart;
        int yStart;
        
        for(String line : lines) {
            tempWidth = this.fontRendererObj.getStringWidth(line);
            
            if(tempWidth > tooltipWidth) {
                tooltipWidth = tempWidth;
            }
        }
        
        xStart = x + 12;
        yStart = y - 12;
        int tooltipHeight = 8;
        
        if(lines.size() > 1) {
            tooltipHeight += 2 + (lines.size() - 1) * 10;
        }
        
        if(this.guiTop + yStart + tooltipHeight + 6 > this.height) {
            yStart = this.height - tooltipHeight - this.guiTop - 6;
        }
        
        this.zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
        int color1 = -267386864;
        this.drawGradientRect(xStart - 3, yStart - 4, xStart + tooltipWidth + 3, yStart - 3, color1, color1);
        this.drawGradientRect(xStart - 3, yStart + tooltipHeight + 3, xStart + tooltipWidth + 3, yStart + tooltipHeight + 4, color1, color1);
        this.drawGradientRect(xStart - 3, yStart - 3, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3, color1, color1);
        this.drawGradientRect(xStart - 4, yStart - 3, xStart - 3, yStart + tooltipHeight + 3, color1, color1);
        this.drawGradientRect(xStart + tooltipWidth + 3, yStart - 3, xStart + tooltipWidth + 4, yStart + tooltipHeight + 3, color1, color1);
        int color2 = 1347420415;
        int color3 = (color2 & 16711422) >> 1 | color2 & -16777216;
        this.drawGradientRect(xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + tooltipHeight + 3 - 1, color2, color3);
        this.drawGradientRect(xStart + tooltipWidth + 2, yStart - 3 + 1, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3 - 1, color2, color3);
        this.drawGradientRect(xStart - 3, yStart - 3, xStart + tooltipWidth + 3, yStart - 3 + 1, color2, color2);
        this.drawGradientRect(xStart - 3, yStart + tooltipHeight + 2, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3, color3, color3);
        
        for(int stringIndex = 0; stringIndex < lines.size(); ++stringIndex) {
            String line = lines.get(stringIndex);
            
            if(stringIndex == 0) {
                line = "\u00a7" + Integer.toHexString(15) + line;
            } else {
                line = "\u00a77" + line;
            }
            
            this.fontRendererObj.drawStringWithShadow(line, xStart, yStart, -1);
            
            if(stringIndex == 0) {
                yStart += 2;
            }
            
            yStart += 10;
        }
        
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
    }

}
