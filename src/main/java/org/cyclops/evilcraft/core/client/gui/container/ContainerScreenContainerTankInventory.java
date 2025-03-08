package org.cyclops.evilcraft.core.client.gui.container;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenExtended;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.item.DamageIndicatedItemComponent;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTankInventory;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTickingTankInventory;
import org.cyclops.evilcraft.core.inventory.container.ContainerInventoryTickingTank;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * A GUI container that has support for the display of inventories and a tank.
 * @author rubensworks
 *
 * @param <T> The {@link BlockEntityTankInventory} class, mostly just the extension class.
 */
public abstract class ContainerScreenContainerTankInventory<C extends ContainerInventoryTickingTank<T>, T extends BlockEntityTickingTankInventory<T>>
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

    public ContainerScreenContainerTankInventory(C container, Inventory playerInventory, Component title) {
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
    protected void renderBg(GuiGraphics guiGraphics, float f, int x, int y) {
        super.renderBg(guiGraphics, f, x, y);
        if(isShowProgress()) {
            guiGraphics.blit(RenderType::guiTextured, getGuiTexture(), leftPos + progressTargetX, topPos + progressTargetY, progressX, progressY,
                    getProgressXScaled(progressWidth), getProgressYScaled(progressHeight), 256, 256);
        }
    }

    protected abstract Component getName();

    protected void drawForgegroundString(GuiGraphics guiGraphics) {
        // MCP: drawString
        guiGraphics.drawString(font, getName(), 8 + offsetX, 4 + offsetY, 4210752, false);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawForgegroundString(guiGraphics);
        IModHelpers.get().getRenderHelpers().bindTexture(texture);
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        FluidStack fluidStack = getMenu().getFluidStack();
        if(shouldRenderTank(fluidStack) && getMenu().getFluidCapacity() > 0) {
            int tankSize = Math.min(getMenu().getFluidCapacity(), Math.min(getMenu().getFluidCapacity(), fluidStack.getAmount()) * tankHeight / getMenu().getFluidCapacity());
            drawTank(guiGraphics, tankTargetX, tankTargetY, fluidStack.getFluid(), tankSize);
        }
        drawAdditionalForeground(guiGraphics, mouseX, mouseY);
        GlStateManager._disableBlend();
    }

    protected void drawAdditionalForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @Override
    public void drawCurrentScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float gameTicks) {
        super.drawCurrentScreen(guiGraphics, mouseX, mouseY, gameTicks);
        drawTooltips(guiGraphics, mouseX, mouseY);
    }

    protected boolean shouldRenderTank(FluidStack fluidStack) {
        if(!showTank)
            return false;
        return fluidStack.getAmount() > 0;
    }

    protected void drawTank(GuiGraphics guiGraphics, int xOffset, int yOffset, Fluid fluid, int level) {
        if(fluid != null) {
            FluidStack stack = new FluidStack(fluid, 1);
            TextureAtlasSprite icon = IModHelpersNeoForge.get().getRenderHelpers().getFluidIcon(stack, Direction.UP);

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

                guiGraphics.blitSprite(RenderType::guiTextured, icon, xOffset, yOffset - textureHeight - verticalOffset, tankWidth, textureHeight);
                verticalOffset = verticalOffset + 16;
            }

            guiGraphics.blit(RenderType::guiTextured, texture, xOffset, yOffset - tankHeight, tankX, tankY, tankWidth, tankHeight, 256, 256);
        }
    }

    protected void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        FluidStack fluidStack = getMenu().getFluidStack();
        if(isHovering(tankTargetX, tankTargetY - tankHeight, tankWidth, tankHeight, mouseX, mouseY) && shouldRenderTank(fluidStack)) {
            Component fluidName = fluidStack.getHoverName();
            drawBarTooltipTank(guiGraphics, fluidName, fluidStack, fluidStack.getAmount(), getMenu().getFluidCapacity(), mouseX, mouseY);
        }
    }

    protected void drawBarTooltipTank(GuiGraphics guiGraphics, Component name, FluidStack fluidStack, int amount, int capacity, int x, int y) {
        List<Component> lines = Lists.newArrayList();
        lines.add(name);
        lines.add(DamageIndicatedItemComponent.getInfo(fluidStack, amount, capacity));
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.leftPos, this.topPos, 0);
        drawTooltip(lines, guiGraphics.pose(), x - this.leftPos, y - this.topPos);
        guiGraphics.pose().popPose();
    }

}
