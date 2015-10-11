package evilcraft.client.gui.container;

import evilcraft.core.client.gui.container.GuiWorking;
import evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;
import evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import net.minecraft.entity.player.InventoryPlayer;

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
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public GuiSanguinaryEnvironmentalAccumulator(InventoryPlayer inventory, TileSanguinaryEnvironmentalAccumulator tile) {
        super(new ContainerSanguinaryEnvironmentalAccumulator(inventory, tile), tile);
        this.setProgress(PROGRESSWIDTH, PROGRESSHEIGHT, PROGRESSX, PROGRESSY, PROGRESSTARGETX, PROGRESSTARGETY);
    }
    
}
