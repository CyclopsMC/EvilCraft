package evilcraft.client.gui.container;

import evilcraft.core.client.gui.container.GuiWorking;
import evilcraft.inventory.container.ContainerColossalBloodChest;
import evilcraft.tileentity.TileColossalBloodChest;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * GUI for the {@link evilcraft.block.ColossalBloodChest}.
 * @author rubensworks
 *
 */
public class GuiColossalBloodChest extends GuiWorking<TileColossalBloodChest> {

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
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public GuiColossalBloodChest(InventoryPlayer inventory, TileColossalBloodChest tile) {
        super(new ContainerColossalBloodChest(inventory, tile), tile);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
    }

    @Override
    protected int getBaseYSize() {
        return TEXTUREHEIGHT;
    }

    @Override
    protected int getBaseXSize() {
        return TEXTUREWIDTH;
    }

    @Override
    protected void drawForgegroundString() {
        fontRendererObj.drawString(tile.getInventoryName(), 8 + offsetX, 4 + offsetY, 4210752);
    }
    
}
