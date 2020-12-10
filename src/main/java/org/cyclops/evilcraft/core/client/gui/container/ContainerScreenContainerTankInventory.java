package org.cyclops.evilcraft.core.client.gui.container;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenExtended;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.item.DamageIndicatedItemComponent;
import org.cyclops.evilcraft.core.inventory.container.ContainerInventoryTickingTank;
import org.cyclops.evilcraft.core.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * A GUI container that has support for the display of inventories and a tank.
 * @author rubensworks
 *
 * @param <T> The {@link TankInventoryTileEntity} class, mostly just the extension class.
 */
public abstract class ContainerScreenContainerTankInventory<C extends ContainerInventoryTickingTank<T>, T extends TickingTankInventoryTileEntity<T>>
        extends ContainerScreenExtended<C> {
	
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

    public ContainerScreenContainerTankInventory(C container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
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
            this.blit(guiLeft + progressTargetX, guiTop + progressTargetY, progressX, progressY,
            		getProgressXScaled(progressWidth), getProgressYScaled(progressHeight));
        }
    }

    protected abstract ITextComponent getName();
    
	protected void drawForgegroundString() {
    	font.drawString(getName().getFormattedText(), 8 + offsetX, 4 + offsetY, 4210752);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawForgegroundString();
        RenderHelpers.bindTexture(texture);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        FluidStack fluidStack = getContainer().getFluidStack();
        if(shouldRenderTank(fluidStack) && getContainer().getFluidCapacity() > 0) {
            int tankSize = Math.min(getContainer().getFluidCapacity(), Math.min(getContainer().getFluidCapacity(), fluidStack.getAmount()) * tankHeight / getContainer().getFluidCapacity());
            drawTank(tankTargetX, tankTargetY, fluidStack.getFluid(), tankSize);
        }
        drawAdditionalForeground(mouseX, mouseY);
        GlStateManager.disableBlend();
    }
    
	protected void drawAdditionalForeground(int mouseX, int mouseY) {
    	
    }
    
    @Override
    public void drawCurrentScreen(int mouseX, int mouseY, float gameTicks) {
        super.drawCurrentScreen(mouseX, mouseY, gameTicks);
        drawTooltips(mouseX, mouseY);
    }
    
	protected boolean shouldRenderTank(FluidStack fluidStack) {
        if(!showTank)
            return false;
        return fluidStack.getAmount() > 0;
    }
    
	protected void drawTank(int xOffset, int yOffset, Fluid fluid, int level) {
        if(fluid != null) {
            FluidStack stack = new FluidStack(fluid, 1);
            TextureAtlasSprite icon = RenderHelpers.getFluidIcon(stack, Direction.UP);
            
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
                
                RenderHelpers.bindTexture(org.cyclops.evilcraft.core.helper.RenderHelpers.TEXTURE_MAP);
                blit(xOffset, yOffset - textureHeight - verticalOffset, 0, tankWidth, textureHeight, icon);
                verticalOffset = verticalOffset + 16;
            }
            
            RenderHelpers.bindTexture(texture);
            blit(xOffset, yOffset - tankHeight, tankX, tankY, tankWidth, tankHeight);
        }
    }
    
	protected void drawTooltips(int mouseX, int mouseY) {
        FluidStack fluidStack = getContainer().getFluidStack();
        if(isPointInRegion(tankTargetX, tankTargetY - tankHeight, tankWidth, tankHeight, mouseX, mouseY) && shouldRenderTank(fluidStack)) {
            ITextComponent fluidName = fluidStack.getDisplayName();
            drawBarTooltipTank(fluidName, fluidStack, fluidStack.getAmount(), getContainer().getFluidCapacity(), mouseX, mouseY);
        }
    }
    
	protected void drawBarTooltipTank(ITextComponent name, FluidStack fluidStack, int amount, int capacity, int x, int y) {
        List<ITextComponent> lines = Lists.newArrayList();
        lines.add(name);
        lines.add(DamageIndicatedItemComponent.getInfo(fluidStack, amount, capacity));
        drawTooltip(lines, x, y);
    }

}
