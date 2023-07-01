package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodChest;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenContainerTankInventory;
import org.cyclops.evilcraft.inventory.container.ContainerBloodChest;

/**
 * GUI for the {@link org.cyclops.evilcraft.block.BlockBloodChest}.
 * @author rubensworks
 *
 */
public class ContainerScreenBloodChest extends ContainerScreenContainerTankInventory<ContainerBloodChest, BlockEntityBloodChest> {

    private static final int TEXTUREWIDTH = 196;
    //private static final int TEXTUREHEIGHT = 166;

    private static final int TANKWIDTH = 16;
    private static final int TANKHEIGHT = 58;
    private static final int TANKX = TEXTUREWIDTH;
    private static final int TANKY = 0;
    private static final int TANKTARGETX = 63;
    private static final int TANKTARGETY = 72;

    public ContainerScreenBloodChest(ContainerBloodChest container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
    }

    @Override
    protected Component getName() {
        return Component.translatable("block.evilcraft.blood_chest");
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "blood_chest_gui.png");
    }

    @Override
    protected int getBaseXSize() {
        return TEXTUREWIDTH;
    }

    @Override
    protected void drawForgegroundString(GuiGraphics guiGraphics) {
        // MCP: drawString
        font.drawInBatch(getName(), 28 + offsetX, 4 + offsetY, 4210752, false,
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
    }

}
