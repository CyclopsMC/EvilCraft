package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.ColossalBloodChest;
import org.cyclops.evilcraft.core.client.gui.container.GuiWorking;
import org.cyclops.evilcraft.inventory.container.ContainerColossalBloodChest;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;

/**
 * GUI for the {@link ColossalBloodChest}.
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
     * Tank width.
     */
    public static final int EFFICIENCYBARWIDTH = 2;
    /**
     * Tank height.
     */
    public static final int EFFICIENCYBARHEIGHT = 58;
    /**
     * Tank X.
     */
    public static final int EFFICIENCYBARX = TEXTUREWIDTH;
    /**
     * Tank Y.
     */
    public static final int EFFICIENCYBARY = 58;
    /**
     * Tank target X.
     */
    public static final int EFFICIENCYBARTARGETX = 46;
    /**
     * Tank target Y.
     */
    public static final int EFFICIENCYBARTARGETY = 82;

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
    public String getGuiTexture() {
        return Reference.TEXTURE_PATH_GUI + "colossalBloodChest_gui.png";
    }

    @Override
    protected int getBaseXSize() {
        return TEXTUREWIDTH;
    }

    @Override
    protected void drawForgegroundString() {
        fontRendererObj.drawString(tile.getName(), 8 + offsetX, 4 + offsetY, 4210752);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        this.mc.renderEngine.bindTexture(texture);
        int minusFactor = (int) (((float) (TileColossalBloodChest.MAX_EFFICIENCY - tile.getEfficiency()) * EFFICIENCYBARHEIGHT) / TileColossalBloodChest.MAX_EFFICIENCY);
        this.drawTexturedModalRect(EFFICIENCYBARTARGETX + offsetX, EFFICIENCYBARTARGETY - EFFICIENCYBARHEIGHT + minusFactor,
                EFFICIENCYBARX, EFFICIENCYBARY + minusFactor, EFFICIENCYBARWIDTH, EFFICIENCYBARHEIGHT - minusFactor);
    }
    
}
