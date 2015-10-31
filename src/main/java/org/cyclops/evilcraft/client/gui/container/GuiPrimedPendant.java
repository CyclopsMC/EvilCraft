package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.client.gui.container.GuiContainerExtended;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.inventory.container.ContainerPrimedPendant;
import org.cyclops.evilcraft.item.PrimedPendant;

/**
 * GUI for the {@link PrimedPendant}.
 * @author rubensworks
 *
 */
public class GuiPrimedPendant extends GuiContainerExtended {

	private static final int TEXTUREHEIGHT = 165;

    private EntityPlayer player;
    private int itemIndex;

    public GuiPrimedPendant(EntityPlayer player, int itemIndex) {
        super(new ContainerPrimedPendant(player, itemIndex));
        this.player = player;
        this.itemIndex = itemIndex;
    }

    @Override
    public String getGuiTexture() {
        return Reference.TEXTURE_PATH_GUI + "primedPendant_gui.png";
    }

    @Override
    protected int getBaseYSize() {
        return TEXTUREHEIGHT;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
        ItemStack itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex);
        this.fontRendererObj.drawString(itemStack.getDisplayName(), 28, 6, 4210752);
    }
    
}
