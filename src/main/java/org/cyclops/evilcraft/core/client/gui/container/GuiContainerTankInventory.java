package org.cyclops.evilcraft.core.client.gui.container;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.client.gui.container.GuiContainerExtended;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.inventory.container.ExtendedInventoryContainer;
import org.cyclops.cyclopscore.item.DamageIndicatedItemComponent;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * A GUI container that has support for the display of inventories and a tank.
 * @author rubensworks
 *
 * @param <T> The {@link TankInventoryTileEntity} class, mostly just the extension class.
 */
public abstract class GuiContainerTankInventory<T extends TankInventoryTileEntity> extends GuiContainerExtended {
	
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
        this.tankTargetX = tankTargetX + offsetX;
        this.tankTargetY = tankTargetY + offsetY;
    }

	protected void setProgress(int progressWidth, int progressHeight, int progressX, int progressY, int progressTargetX, int progressTargetY) {
        this.showProgress = true;
        this.progressWidth = progressWidth;
        this.progressHeight = progressHeight;
        this.progressX = progressX;
        this.progressY = progressY;
        this.progressTargetX = progressTargetX + offsetX;
        this.progressTargetY = progressTargetY + offsetY;
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
        if(isShowProgress()) {
            this.drawTexturedModalRect(guiLeft + progressTargetX, guiTop + progressTargetY, progressX, progressY,
            		getProgressXScaled(progressWidth), getProgressYScaled(progressHeight));
        }
    }
    
	protected void drawForgegroundString() {
    	fontRendererObj.drawString(tile.getName(), 8 + offsetX, 4 + offsetY, 4210752);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawForgegroundString();
        this.mc.renderEngine.bindTexture(texture);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        SingleUseTank tank = tile.getTank();
        if(shouldRenderTank()) {
            int tankSize = Math.min(tank.getCapacity(), tank.getFluidAmount()) * tankHeight / tank.getCapacity();
            drawTank(tankTargetX, tankTargetY, tank.getAcceptedFluid(), tankSize);
        }
        drawAdditionalForeground(mouseX, mouseY);
        GlStateManager.disableBlend();
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
    
	protected void drawTank(int xOffset, int yOffset, Fluid fluid, int level) {
        if(fluid != null) {
            FluidStack stack = new FluidStack(fluid, 1);
            TextureAtlasSprite icon = RenderHelpers.getFluidIcon(stack, EnumFacing.UP);
            
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
                
                mc.renderEngine.bindTexture(org.cyclops.evilcraft.core.helper.RenderHelpers.TEXTURE_MAP);
                drawTexturedModalRect(xOffset, yOffset - textureHeight - verticalOffset, icon, tankWidth, textureHeight);
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
            drawBarTooltipTank(fluidName, tank.getFluid(), tank.getFluidAmount(), tank.getCapacity(), mouseX, mouseY);
        }
    }
    
	protected void drawBarTooltipTank(String name, FluidStack fluidStack, int amount, int capacity, int x, int y) {
        List<String> lines = new ArrayList<String>();
        lines.add(name);
        lines.add(DamageIndicatedItemComponent.getInfo(fluidStack, amount, capacity));
        drawTooltip(lines, x, y);
    }

}
