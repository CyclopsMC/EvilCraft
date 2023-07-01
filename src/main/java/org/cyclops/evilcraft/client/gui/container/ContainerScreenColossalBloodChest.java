package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockColossalBloodChest;
import org.cyclops.evilcraft.blockentity.BlockEntityColossalBloodChest;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenTileWorking;
import org.cyclops.evilcraft.inventory.container.ContainerColossalBloodChest;

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
    protected void drawForgegroundString(GuiGraphics guiGraphics) {
        // MCP: drawString
        font.drawInBatch(getName(), 8 + offsetX, 4 + offsetY, 4210752, false,
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        int minusFactor = (int) (((float) (BlockEntityColossalBloodChest.MAX_EFFICIENCY - ((ContainerColossalBloodChest) getMenu()).getEfficiency()) * EFFICIENCYBARHEIGHT) / BlockEntityColossalBloodChest.MAX_EFFICIENCY);
        guiGraphics.blit(texture, EFFICIENCYBARTARGETX + offsetX, EFFICIENCYBARTARGETY - EFFICIENCYBARHEIGHT + minusFactor,
                EFFICIENCYBARX, EFFICIENCYBARY + minusFactor, EFFICIENCYBARWIDTH, EFFICIENCYBARHEIGHT - minusFactor);
    }

}
