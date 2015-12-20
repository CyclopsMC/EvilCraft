package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BloodChest;
import org.cyclops.evilcraft.core.client.gui.container.GuiContainerTankInventory;
import org.cyclops.evilcraft.inventory.container.ContainerBloodChest;
import org.cyclops.evilcraft.tileentity.TileBloodChest;

/**
 * GUI for the {@link BloodChest}.
 * @author rubensworks
 *
 */
public class GuiBloodChest extends GuiContainerTankInventory<TileBloodChest> {

    private static final int TEXTUREWIDTH = 196;
	//private static final int TEXTUREHEIGHT = 166;
    
    private static final int TANKWIDTH = 16;
    private static final int TANKHEIGHT = 58;
    private static final int TANKX = TEXTUREWIDTH;
    private static final int TANKY = 0;
    private static final int TANKTARGETX = 63;
    private static final int TANKTARGETY = 72;
    
    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public GuiBloodChest(InventoryPlayer inventory, TileBloodChest tile) {
        super(new ContainerBloodChest(inventory, tile), tile);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
    }

    @Override
    public String getGuiTexture() {
        return Reference.TEXTURE_PATH_GUI + "bloodChest_gui.png";
    }

    @Override
    protected int getBaseXSize() {
        return TEXTUREWIDTH;
    }

    @Override
    protected void drawForgegroundString() {
        fontRendererObj.drawString(tile.getName(), 28 + offsetX, 4 + offsetY, 4210752);
    }

}
