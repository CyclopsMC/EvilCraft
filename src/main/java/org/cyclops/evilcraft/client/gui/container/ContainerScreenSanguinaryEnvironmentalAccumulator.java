package org.cyclops.evilcraft.client.gui.container;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenTileWorking;
import org.cyclops.evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;

import java.util.List;

/**
 * GUI for the {@link ContainerScreenSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class ContainerScreenSanguinaryEnvironmentalAccumulator extends ContainerScreenTileWorking<ContainerSanguinaryEnvironmentalAccumulator, TileSanguinaryEnvironmentalAccumulator> {

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

    public ContainerScreenSanguinaryEnvironmentalAccumulator(ContainerSanguinaryEnvironmentalAccumulator container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.setProgress(PROGRESSWIDTH, PROGRESSHEIGHT, PROGRESSX, PROGRESSY, PROGRESSTARGETX, PROGRESSTARGETY);
    }

    @Override
    protected ITextComponent getName() {
        return new TranslationTextComponent("block.evilcraft.sanguinary_environmental_accumulator");
    }

    @Override
    protected void drawAdditionalForeground(int mouseX, int mouseY) {
        super.drawAdditionalForeground(mouseX, mouseY);
        String prefix = RegistryEntries.BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR.getTranslationKey() + ".help.invalid";
        List<ITextComponent> lines = Lists.newArrayList();
        lines.add(new TranslationTextComponent(prefix));
        if (!getContainer().getTileCanWork()){
            lines.add(new TranslationTextComponent(prefix + ".invalid_locations"));
            for(BlockPos location : getContainer().getInvalidLocations()) {
                lines.add(new StringTextComponent("  " + location));
            }
        }
        if (lines.size() > 1) {
            this.blit(PROGRESSTARGETX + offsetX, PROGRESSTARGETY + offsetY, PROGRESS_INVALIDX,
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
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "sanguinary_environmental_accumulator_gui.png");
    }
}
