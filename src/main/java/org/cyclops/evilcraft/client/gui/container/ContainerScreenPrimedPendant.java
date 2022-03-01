package org.cyclops.evilcraft.client.gui.container;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
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
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        // super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        ItemStack itemStack = container.getItemStack(getMinecraft().player);
        // MCP: drawString
        this.font.draw(matrixStack, itemStack.getHoverName(), 28, 6, 4210752);
    }

}
