package evilcraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import evilcraft.core.fluid.SingleUseTank;
import evilcraft.core.inventory.container.ExtendedInventoryContainer;
import evilcraft.core.tileentity.TankInventoryTileEntity;

/**
 * A GUI container that has support for the display of inventories and a tank.
 * @author rubensworks
 *
 * @param <T> The {@link TankInventoryTileEntity} class, mostly just the extension class.
 */
public class GuiContainerTankInventory<T extends TankInventoryTileEntity> extends GuiContainerExtended {
	
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

    /**
     * Make a new instance.
     * @param container The container to make the GUI for.
     * @param tile The tile entity to make the GUI for.
     */
    public GuiContainerTankInventory(ExtendedInventoryContainer container, T tile) {
        super(container);
        this.tile = tile;
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
    
	protected int getProgressXScaled(int width) {
        return width;
    }
	
	protected int getProgressYScaled(int height) {
        return height;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        if(isShowProgress()) {
            this.drawTexturedModalRect(j + progressTargetX, k + progressTargetY, progressX, progressY,
            		getProgressXScaled(progressWidth), getProgressYScaled(progressHeight));
        }
    }
    
	protected void drawForgegroundString() {
    	fontRendererObj.drawString(tile.getInventoryName(), 8, 4, 4210752);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawForgegroundString();
        this.mc.renderEngine.bindTexture(texture);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        SingleUseTank tank = tile.getTank();
        if(shouldRenderTank()) {
            int tankSize = tank.getFluidAmount() * tankHeight / tank.getCapacity();
            drawTank(tankTargetX, tankTargetY, tank.getAcceptedFluid().getID(), tankSize);
        }
        drawAdditionalForeground(mouseX, mouseY);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
	protected void drawAdditionalForeground(int mouseX, int mouseY) {
    	
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
        if(isPointInRegion(tankTargetX, tankTargetY - tankHeight, tankWidth, tankHeight, mouseX, mouseY) && shouldRenderTank()) {
            SingleUseTank tank = tile.getTank();
            String fluidName = tank.getFluid().getLocalizedName();
            drawBarTooltipTank(fluidName, "mB", tank.getFluidAmount(), tank.getCapacity(), mouseX, mouseY);
        }
    }
    
	protected void drawBarTooltipTank(String name, String unit, int value, int max, int x, int y) {
        List<String> lines = new ArrayList<String>();
        lines.add(name);
        lines.add(value + " / " + max + " " + unit);
        drawTooltip(lines, x, y);
    }

}
