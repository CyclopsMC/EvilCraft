package org.cyclops.evilcraft.client.gui.container;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntitySanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenTileWorking;
import org.cyclops.evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;

import java.util.List;

/**
 * GUI for the {@link ContainerScreenSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class ContainerScreenSanguinaryEnvironmentalAccumulator extends ContainerScreenTileWorking<ContainerSanguinaryEnvironmentalAccumulator, BlockEntitySanguinaryEnvironmentalAccumulator> {

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

    public ContainerScreenSanguinaryEnvironmentalAccumulator(ContainerSanguinaryEnvironmentalAccumulator container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.setProgress(PROGRESSWIDTH, PROGRESSHEIGHT, PROGRESSX, PROGRESSY, PROGRESSTARGETX, PROGRESSTARGETY);
    }

    @Override
    protected Component getName() {
        return Component.translatable("block.evilcraft.sanguinary_environmental_accumulator");
    }

    @Override
    protected void drawAdditionalForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.drawAdditionalForeground(guiGraphics, mouseX, mouseY);
        String prefix = RegistryEntries.BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR.getDescriptionId() + ".help.invalid";
        List<Component> lines = Lists.newArrayList();
        lines.add(Component.translatable(prefix));
        if (!getMenu().getTileCanWork()){
            lines.add(Component.translatable(prefix + ".invalid_locations"));
            for(Vec3i location : getMenu().getInvalidLocations()) {
                lines.add(Component.literal(String.format("  X=%s Y=%s Z=%s", location.getX(), location.getY(), location.getZ())));
            }
        }
        if (lines.size() > 1) {
            guiGraphics.blit(getGuiTexture(), PROGRESSTARGETX + offsetX, PROGRESSTARGETY + offsetY, PROGRESS_INVALIDX,
                    PROGRESS_INVALIDY, PROGRESSWIDTH, PROGRESSHEIGHT);
            if(isHovering(PROGRESSTARGETX + offsetX, PROGRESSTARGETY + offsetY, PROGRESSWIDTH, PROGRESSHEIGHT,
                    mouseX, mouseY)) {
                mouseX -= leftPos;
                mouseY -= topPos;
                drawTooltip(lines, guiGraphics.pose(), mouseX, mouseY);
            }
        }
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "sanguinary_environmental_accumulator_gui.png");
    }
}
