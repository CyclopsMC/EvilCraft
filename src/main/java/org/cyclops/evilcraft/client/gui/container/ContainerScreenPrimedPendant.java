package org.cyclops.evilcraft.client.gui.container;

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
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
        ItemStack itemStack = container.getItemStack(playerInventory.player);
        this.font.drawString(itemStack.getDisplayName().getFormattedText(), 28, 6, 4210752);
    }
    
}
