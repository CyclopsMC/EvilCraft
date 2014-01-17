package evilcraft.gui.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import evilcraft.Reference;
import evilcraft.api.fluids.SingleUseTank;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.container.ContainerBloodInfuser;

public class GuiBloodInfuser extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "bloodInfuser_gui.png");

    private static final int TANKHEIGHT = 58;
    private static final int TANKWIDTH = 16;
    private static final int TANKX = 43;
    private static final int TANKY = 72;
    
    protected TileBloodInfuser tile;
    
    
    public GuiBloodInfuser(InventoryPlayer inventory, TileBloodInfuser tile) {
        super(new ContainerBloodInfuser(inventory, tile));
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(TEXTURE);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        
        if (this.tile.isInfusing()) {
            int progressScaled = this.tile.getInfuseTickScaled(24);
            this.drawTexturedModalRect(j + 102, k + 36, 192, 0, progressScaled, 16);
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tile.getInvName(), 8, 4, 4210752);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        SingleUseTank tank = tile.getTank();
        if(shouldRenderTank()) {
            int tankSize = tank.getFluidAmount() * TANKHEIGHT / tank.getCapacity();
            drawTank(TANKX, TANKY, tank.getAcceptedFluid().getID(), tankSize);
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float gameTicks) {
        super.drawScreen(mouseX, mouseY, gameTicks);
        drawTooltips(mouseX, mouseY);
    }
    
    protected boolean shouldRenderTank() {
        SingleUseTank tank = tile.getTank();
        return tank != null && tank.getAcceptedFluid() != null && tank.getFluidAmount() > 0;
    }
    
    protected void drawTank(int xOffset, int yOffset, int fluidID, int level) {
        FluidStack stack = new FluidStack(fluidID, 1);
        if(fluidID > 0 && stack != null) {
            Icon icon = stack.getFluid().getIcon();
            if (icon == null) icon = Block.waterMoving.getIcon(0, 0);
            
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
                drawTexturedModelRectFromIcon(xOffset, yOffset - textureHeight - verticalOffset, icon, TANKWIDTH, textureHeight);
                verticalOffset = verticalOffset + 16;
            }
            
            this.mc.renderEngine.bindTexture(TEXTURE);
            this.drawTexturedModalRect(xOffset, yOffset - TANKHEIGHT, 176, 0, 16, TANKHEIGHT);
        }
    }
    
    protected void drawTooltips(int mouseX, int mouseY) {
        if(isPointInRegion(TANKX, TANKY - TANKHEIGHT, TANKWIDTH, TANKHEIGHT, mouseX, mouseY) && shouldRenderTank()) {
            SingleUseTank tank = tile.getTank();
            drawBarTooltip(FluidRegistry.getFluidName(tank.getFluid().fluidID),
                    "mB", tank.getFluidAmount(), tank.getCapacity(), mouseX, mouseY);
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
            tempWidth = this.fontRenderer.getStringWidth(lines.get(i));
            
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
        itemRenderer.zLevel = 300.0F;
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
            
            this.fontRenderer.drawStringWithShadow(line, xStart, yStart, -1);
            
            if(stringIndex == 0) {
                yStart += 2;
            }
            
            yStart += 10;
        }
        
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        this.zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }
}
