package evilcraft.api.gui.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import evilcraft.Reference;
import evilcraft.api.entities.tileentitites.TankInventoryTileEntity;
import evilcraft.api.fluids.SingleUseTank;

/**
 * A GUI container that has support for the display of inventories and a tank.
 * @author rubensworks
 *
 * @param <T> The {@link TankInventoryTileEntity} class, mostly just the extension class.
 */
public class GuiContainerTankInventory<T extends TankInventoryTileEntity> extends GuiContainer {

    private boolean showTank = false;
    private int tankWidth;
    private int tankHeight;
    private int tankX;
    private int tankY;
    private int tankTargetX;
    private int tankTargetY;
    
    private boolean showProgress = false;
    private int progressWidth;
    private int progressHeight;
    private int progressX;
    private int progressY;
    private int progressTargetX;
    private int progressTargetY;
    
    protected T tile;
    private ResourceLocation texture;

    /**
     * Make a new instance.
     * @param container The container to make the GUI for.
     * @param tile The tile entity to make the GUI for.
     */
    public GuiContainerTankInventory(Container container, T tile) {
        super(container);
        this.tile = tile;
        this.texture = new ResourceLocation(Reference.MOD_ID, tile.getBlock().getGuiTexture());
    }
    
    protected void setTank(int tankWidth, int tankHeight, int tankX, int tankY, int tankTargetX, int tankTargetY) {
        this.showTank = true;
        this.tankWidth = tankWidth;
        this.tankHeight = tankHeight;
        this.tankX = tankX;
        this.tankY = tankY;
        this.tankTargetX = tankTargetX;
        this.tankTargetY = tankTargetY;
    }
    
    protected void setProgress(int progressWidth, int progressHeight, int progressX, int progressY, int progressTargetX, int progressTargetY) {
        this.showProgress = true;
        this.progressWidth = progressWidth;
        this.progressHeight = progressHeight;
        this.progressX = progressX;
        this.progressY = progressY;
        this.progressTargetX = progressTargetX;
        this.progressTargetY = progressTargetY;
    }
    
    protected boolean isShowProgress() {
        return showProgress;
    }
    
    protected int getProgressScaled(int scale) {
        return 0;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        
        if (isShowProgress()) {
            int progressScaled = getProgressScaled(progressWidth);
            this.drawTexturedModalRect(j + progressTargetX, k + progressTargetY, progressX, progressY, progressScaled, progressHeight);
        }
    }
    
    protected void drawForgegroundString() {
    	fontRendererObj.drawString(tile.getInventoryName(), 8, 4, 4210752);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawForgegroundString();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        SingleUseTank tank = tile.getTank();
        if(shouldRenderTank()) {
            int tankSize = tank.getFluidAmount() * tankHeight / tank.getCapacity();
            drawTank(tankTargetX, tankTargetY, tank.getAcceptedFluid().getID(), tankSize);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float gameTicks) {
        super.drawScreen(mouseX, mouseY, gameTicks);
        drawTooltips(mouseX, mouseY);
    }
    
    protected boolean shouldRenderTank() {
        if(!showTank)
            return false;
        SingleUseTank tank = tile.getTank();
        return tank != null && tank.getAcceptedFluid() != null && tank.getFluidAmount() > 0;
    }
    
    protected void drawTank(int xOffset, int yOffset, int fluidID, int level) {
        FluidStack stack = new FluidStack(fluidID, 1);
        if(fluidID > 0 && stack != null) {
            IIcon icon = stack.getFluid().getIcon();
            if (icon == null) icon = Blocks.water.getIcon(0, 0);
            
            int verticalOffset = 0;
            
            while(level > 0) {
                int textureHeight = 0;
                
                if(level > 16) {
                    textureHeight = 16;
                    level -= 16;
                } else {
                    textureHeight = level;
                    level = 0;
                }
                
                mc.renderEngine.bindTexture(mc.renderEngine.getResourceLocation(0));
                drawTexturedModelRectFromIcon(xOffset, yOffset - textureHeight - verticalOffset, icon, tankWidth, textureHeight);
                verticalOffset = verticalOffset + 16;
            }
            
            this.mc.renderEngine.bindTexture(texture);
            this.drawTexturedModalRect(xOffset, yOffset - tankHeight, tankX, tankY, tankWidth, tankHeight);
        }
    }
    
    protected void drawTooltips(int mouseX, int mouseY) {
    	// MCP isPointInRegion
        if(func_146978_c(tankTargetX, tankTargetY - tankHeight, tankWidth, tankHeight, mouseX, mouseY) && shouldRenderTank()) {
            SingleUseTank tank = tile.getTank();
            String fluidName = StatCollector.translateToLocal("fluid.fluids." + FluidRegistry.getFluidName(tank.getFluid()));
            drawBarTooltip(fluidName, "mB", tank.getFluidAmount(), tank.getCapacity(), mouseX, mouseY);
        }
    }
    
    protected void drawBarTooltip(String name, String unit, int value, int max, int x, int y) {
        List<String> lines = new ArrayList<String>();
        lines.add(name);
        lines.add(value + " / " + max + " " + unit);
        drawTooltip(lines, x, y);
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
        
        for(int i = 0; i < lines.size(); i++) {
            tempWidth = this.fontRendererObj.getStringWidth(lines.get(i));
            
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
