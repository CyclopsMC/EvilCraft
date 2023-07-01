package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenExtended;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.inventory.container.ContainerPrimedPendant;
import org.cyclops.evilcraft.item.ItemPrimedPendant;

/**
 * GUI for the {@link ItemPrimedPendant}.
 * @author rubensworks
 *
 */
public class ContainerScreenPrimedPendant extends ContainerScreenExtended<ContainerPrimedPendant> {

    private static final int TEXTUREHEIGHT = 165;

    public ContainerScreenPrimedPendant(ContainerPrimedPendant container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "primed_pendant_gui.png");
    }

    @Override
    protected int getBaseYSize() {
        return TEXTUREHEIGHT;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        // super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        ItemStack itemStack = container.getItemStack(getMinecraft().player);
        this.font.drawInBatch(itemStack.getHoverName(), 28, 6, 4210752, false,
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
    }

}
