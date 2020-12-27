package org.cyclops.evilcraft.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
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

    public ContainerScreenPrimedPendant(ContainerPrimedPendant container, PlayerInventory inventory, ITextComponent title) {
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
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        // super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        ItemStack itemStack = container.getItemStack(playerInventory.player);
        // MCP: drawString
        this.font.func_243248_b(matrixStack, itemStack.getDisplayName(), 28, 6, 4210752);
    }
    
}
