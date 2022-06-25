package org.cyclops.evilcraft.client.gui.container;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockColossalBloodChest;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenTileWorking;
import org.cyclops.evilcraft.inventory.container.ContainerColossalBloodChest;
import org.cyclops.evilcraft.blockentity.BlockEntityColossalBloodChest;

/**
 * GUI for the {@link BlockColossalBloodChest}.
 * @author rubensworks
 *
 */
public class ContainerScreenColossalBloodChest extends ContainerScreenTileWorking<ContainerColossalBloodChest, BlockEntityColossalBloodChest> {

    /**
     * Texture width.
     */
    public static final int TEXTUREWIDTH = 236;
    /**
     * Texture height.
     */
    public static final int TEXTUREHEIGHT = 189;

    /**
     * Tank width.
     */
    public static final int TANKWIDTH = 16;
    /**
     * Tank height.
     */
    public static final int TANKHEIGHT = 58;
    /**
     * Tank X.
     */
    public static final int TANKX = TEXTUREWIDTH;
    /**
     * Tank Y.
     */
    public static final int TANKY = 0;
    /**
     * Tank target X.
     */
    public static final int TANKTARGETX = 28;
    /**
     * Tank target Y.
     */
    public static final int TANKTARGETY = 82;

    /**
     * Tank width.
     */
    public static final int EFFICIENCYBARWIDTH = 2;
    /**
     * Tank height.
     */
    public static final int EFFICIENCYBARHEIGHT = 58;
    /**
     * Tank X.
     */
    public static final int EFFICIENCYBARX = TEXTUREWIDTH;
    /**
     * Tank Y.
     */
    public static final int EFFICIENCYBARY = 58;
    /**
     * Tank target X.
     */
    public static final int EFFICIENCYBARTARGETX = 46;
    /**
     * Tank target Y.
     */
    public static final int EFFICIENCYBARTARGETY = 82;

    public ContainerScreenColossalBloodChest(ContainerColossalBloodChest container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
    }

    @Override
    protected Component getName() {
        return Component.translatable("block.evilcraft.colossal_blood_chest");
    }

    @Override
    protected int getBaseYSize() {
        return TEXTUREHEIGHT;
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "colossal_blood_chest_gui.png");
    }

    @Override
    protected int getBaseXSize() {
        return TEXTUREWIDTH;
    }

    @Override
    protected void drawForgegroundString(PoseStack matrixStack) {
        // MCP: drawString
        font.draw(matrixStack, getName(), 8 + offsetX, 4 + offsetY, 4210752);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);

        RenderHelpers.bindTexture(texture);
        int minusFactor = (int) (((float) (BlockEntityColossalBloodChest.MAX_EFFICIENCY - ((ContainerColossalBloodChest) getMenu()).getEfficiency()) * EFFICIENCYBARHEIGHT) / BlockEntityColossalBloodChest.MAX_EFFICIENCY);
        blit(matrixStack, EFFICIENCYBARTARGETX + offsetX, EFFICIENCYBARTARGETY - EFFICIENCYBARHEIGHT + minusFactor,
                EFFICIENCYBARX, EFFICIENCYBARY + minusFactor, EFFICIENCYBARWIDTH, EFFICIENCYBARHEIGHT - minusFactor);
    }

}
