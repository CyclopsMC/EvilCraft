package org.cyclops.evilcraft.client.gui.container;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSpiritFurnace;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritFurnace;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenTileWorking;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritFurnace;

import javax.annotation.Nullable;
import java.util.List;

/**
 * GUI for the {@link BlockSpiritFurnace}.
 * @author rubensworks
 *
 */
public class ContainerScreenSpiritFurnace extends ContainerScreenTileWorking<ContainerSpiritFurnace, BlockEntitySpiritFurnace> {

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

    public ContainerScreenSpiritFurnace(ContainerSpiritFurnace container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
        this.setProgress(PROGRESSWIDTH, PROGRESSHEIGHT, PROGRESSX, PROGRESSY, PROGRESSTARGETX, PROGRESSTARGETY);
    }

    @Override
    protected Component getName() {
        return Component.translatable("block.evilcraft.spirit_furnace");
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "spirit_furnace_gui.png");
    }

    private String prettyPrintSize(@Nullable Vec3i size) {
        if (size == null) {
            return "Loading...";
        }
        return size.getX() + "x" + size.getY() + "x" + size.getZ();
    }

    @Override
    protected void drawAdditionalForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        String prefix = RegistryEntries.BLOCK_SPIRIT_FURNACE.getDescriptionId() + ".help.invalid";
        List<Component> lines = Lists.newArrayList();
        lines.add(Component.translatable(prefix));
        if (!getMenu().hasEntity()) {
            lines.add(Component.translatable(prefix + ".no_entity"));
        } else if (!getMenu().isSizeValidForEntity()) {
            lines.add(Component.translatable(prefix + ".content_size", prettyPrintSize(getMenu().getInnerSize())));
            lines.add(Component.translatable(prefix + ".required_size", prettyPrintSize(getMenu().getEntitySize())));
        } else if (getMenu().isForceHalt()) {
            lines.add(Component.translatable(prefix + ".force_halt"));
        }
        else if (getMenu().isCaughtError()) {
            lines.add(Component.translatable(prefix + ".caught_error"));
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

}
