package evilcraft.client.gui.container;

import evilcraft.api.ILocation;
import evilcraft.block.SanguinaryEnvironmentalAccumulator;
import evilcraft.core.client.gui.container.GuiWorking;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;
import evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI for the {@link GuiSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class GuiSanguinaryEnvironmentalAccumulator extends GuiWorking<TileSanguinaryEnvironmentalAccumulator> {

    /**
     * Texture width.
     */
    public static final int TEXTUREWIDTH = 176;
    /**
     * Texture height.
     */
    public static final int TEXTUREHEIGHT = 166;

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
    public static final int PROGRESSTARGETX = 77;
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
    public GuiSanguinaryEnvironmentalAccumulator(InventoryPlayer inventory, TileSanguinaryEnvironmentalAccumulator tile) {
        super(new ContainerSanguinaryEnvironmentalAccumulator(inventory, tile), tile);
        this.setProgress(PROGRESSWIDTH, PROGRESSHEIGHT, PROGRESSX, PROGRESSY, PROGRESSTARGETX, PROGRESSTARGETY);
    }

    @Override
    protected void drawAdditionalForeground(int mouseX, int mouseY) {
        super.drawAdditionalForeground(mouseX, mouseY);
        String prefix = SanguinaryEnvironmentalAccumulator.getInstance().getUnlocalizedName() + ".help.invalid";
        List<String> lines = new ArrayList<String>();
        lines.add(L10NHelpers.localize(prefix));
        if(!tile.canWork()) {
            lines.add(L10NHelpers.localize(prefix + ".invalidLocations"));
            for(ILocation location : tile.getInvalidLocations()) {
                lines.add("  " + location);
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
    
}
