package evilcraft.client.gui.container;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import evilcraft.core.client.gui.GuiButtonExtended;
import evilcraft.core.client.gui.container.GuiContainerExtended;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.inventory.container.ContainerExaltedCrafter;
import evilcraft.item.ExaltedCrafter;

/**
 * GUI for the {@link ExaltedCrafter}.
 * @author rubensworks
 *
 */
public class GuiExaltedCrafter extends GuiContainerExtended {
	
	private static final int BUTTON_CLEAR = 1;
	
    /**
     * Make a new instance.
     * @param player The player.
     * @param itemIndex The index of the item in use inside the player inventory.
     */
    public GuiExaltedCrafter(EntityPlayer player, int itemIndex) {
        super(new ContainerExaltedCrafter(player, itemIndex));
        this.ySize = 225;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void initGui() {
    	super.initGui();
    	buttonList.add(new GuiButtonExtended(BUTTON_CLEAR, this.guiLeft + 90, this.guiTop + 58, 13, 12, "C"));
    }
    
    @Override
    protected void actionPerformed(GuiButton guibutton) {
    	if(guibutton.id == BUTTON_CLEAR) {
    		((ContainerExaltedCrafter) this.inventorySlots).sendClearGrid();
    	}
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
    	super.drawGuiContainerForegroundLayer(x, y);
        this.fontRendererObj.drawString(L10NHelpers.localize("gui.exaltedCrafting"), 28, 6, 4210752);
    }
    
}
