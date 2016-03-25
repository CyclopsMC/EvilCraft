package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.Vec3i;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.SpiritFurnace;
import org.cyclops.evilcraft.core.client.gui.container.GuiWorking;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritFurnace;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI for the {@link SpiritFurnace}.
 * @author rubensworks
 *
 */
public class GuiSpiritFurnace extends GuiWorking<TileSpiritFurnace> {
    
    /**
     * Texture width.
     */
    public static final int TEXTUREWIDTH = 176;
    /**
     * Texture height.
     */
    public static final int TEXTUREHEIGHT = 166;

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
    public static final int TANKTARGETX = 43;
    /**
     * Tank target Y.
     */
    public static final int TANKTARGETY = 72;

    /**
     * Progress width.
     */
    public static final int PROGRESSWIDTH = 24;
    /**
     * Progress height.
     */
    public static final int PROGRESSHEIGHT = 16;
    /**
     * Progress X.
     */
    public static final int PROGRESSX = 192;
    /**
     * Progress Y.
     */
    public static final int PROGRESSY = 0;
    /**
     * Progress target X.
     */
    public static final int PROGRESSTARGETX = 102;
    /**
     * Progress target Y.
     */
    public static final int PROGRESSTARGETY = 36;
    
    /**
     * Progress target X.
     */
    public static final int PROGRESS_INVALIDX = 192;
    /**
     * Progress target Y.
     */
    public static final int PROGRESS_INVALIDY = 18;
    
    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public GuiSpiritFurnace(InventoryPlayer inventory, TileSpiritFurnace tile) {
        super(new ContainerSpiritFurnace(inventory, tile), tile);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
        this.setProgress(PROGRESSWIDTH, PROGRESSHEIGHT, PROGRESSX, PROGRESSY, PROGRESSTARGETX, PROGRESSTARGETY);
    }

    @Override
    public String getGuiTexture() {
        return Reference.TEXTURE_PATH_GUI + "spiritFurnace_gui.png";
    }
    
    private String prettyPrintSize(Vec3i size) {
        return size.getX() + "x" + size.getY() + "x" + size.getZ();
    }
    
    @Override
	protected void drawAdditionalForeground(int mouseX, int mouseY) {
    	String prefix = SpiritFurnace.getInstance().getUnlocalizedName() + ".help.invalid";
    	List<String> lines = new ArrayList<String>();
    	lines.add(L10NHelpers.localize(prefix));
        if(tile.getEntity() == null) {
        	lines.add(L10NHelpers.localize(prefix + ".noEntity"));
        } else if(!tile.isSizeValidForEntity()) {
        	lines.add(L10NHelpers.localize(prefix + ".contentSize", prettyPrintSize(tile.getInnerSize())));
        	lines.add(L10NHelpers.localize(prefix + ".requiredSize", prettyPrintSize(tile.getEntitySize())));
        } else if(tile.isForceHalt()) {
        	lines.add(L10NHelpers.localize(prefix + ".forceHalt"));
        }
        else if(tile.isCaughtError()) {
        	lines.add(L10NHelpers.localize(prefix + ".caughtError"));
        }
        if(lines.size() > 1) {
        	this.drawTexturedModalRect(PROGRESSTARGETX + offsetX, PROGRESSTARGETY + offsetY, PROGRESS_INVALIDX,
            		PROGRESS_INVALIDY, PROGRESSWIDTH, PROGRESSHEIGHT);
            if(isPointInRegion(PROGRESSTARGETX + offsetX, PROGRESSTARGETY + offsetY, PROGRESSWIDTH, PROGRESSHEIGHT,
                    mouseX, mouseY)) {
	    		mouseX -= guiLeft;
	        	mouseY -= guiTop;
	            drawTooltip(lines, mouseX, mouseY);
	        }
        }
    }
    
}
