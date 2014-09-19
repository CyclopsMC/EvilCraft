package evilcraft.client.gui.container;

import net.minecraft.entity.player.EntityPlayer;
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
    
	private EntityPlayer player;
	
    /**
     * Make a new instance.
     * @param player The player.
     */
    public GuiExaltedCrafter(EntityPlayer player) {
        super(new ContainerExaltedCrafter(player));
        this.player = player;
        ySize = 225; 
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.fontRendererObj.drawString(L10NHelpers.localize("gui.exaltedCrafting"), 28, 6, 4210752);
    }
    
}
