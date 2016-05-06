package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.SpiritFurnace;
import org.cyclops.evilcraft.block.SpiritReanimator;
import org.cyclops.evilcraft.core.client.gui.container.GuiWorking;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritReanimator;
import org.cyclops.evilcraft.tileentity.TileSpiritReanimator;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI for the {@link SpiritFurnace}.
 * @author rubensworks
 *
 */
public class GuiSpiritReanimator extends GuiWorking<TileSpiritReanimator> {
    
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
    public static final int PROGRESSWIDTH = 10;
    /**
     * Progress height.
     */
    public static final int PROGRESSHEIGHT = 24;
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
    public static final int PROGRESSTARGETX = 119;
    /**
     * Progress target Y.
     */
    public static final int PROGRESSTARGETY = 26;
    
    /**
     * Progress target X.
     */
    public static final int PROGRESS_INVALIDX = 192;
    /**
     * Progress target Y.
     */
    public static final int PROGRESS_INVALIDY = 24;
    
    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public GuiSpiritReanimator(InventoryPlayer inventory, TileSpiritReanimator tile) {
        super(new ContainerSpiritReanimator(inventory, tile), tile);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
        this.setProgress(PROGRESSWIDTH, PROGRESSHEIGHT, PROGRESSX, PROGRESSY, PROGRESSTARGETX, PROGRESSTARGETY);
    }

    @Override
    public String getGuiTexture() {
        return Reference.TEXTURE_PATH_GUI + "spiritReanimator_gui.png";
    }
    
    @Override
	protected void drawAdditionalForeground(int mouseX, int mouseY) {
    	String prefix = SpiritReanimator.getInstance().getUnlocalizedName() + ".help.invalid";
    	List<String> lines = new ArrayList<String>();
    	lines.add(L10NHelpers.localize(prefix));
        String entityName = tile.getEntityName();
        if(entityName == null) {
        	lines.add(L10NHelpers.localize(prefix + ".noEntity"));
        } else if(!EntityList.entityEggs.containsKey(entityName)) {
            lines.add(L10NHelpers.localize(prefix + ".invalidEntity"));
        }
        else {
        	ItemStack outputStack = tile.getStackInSlot(TileSpiritReanimator.SLOTS_OUTPUT);
        	if(outputStack != null &&
                    !entityName.equals(ItemMonsterPlacer.getEntityIdFromItem(outputStack))) {
        		lines.add(L10NHelpers.localize(prefix + ".differentEgg"));
        	}
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
    
    @Override
    protected int getProgressXScaled(int width) {
        return width;
    }
    
    @Override
    protected int getProgressYScaled(int height) {
        return tile.getWorkTickScaled(PROGRESSHEIGHT);
    }
    
}
