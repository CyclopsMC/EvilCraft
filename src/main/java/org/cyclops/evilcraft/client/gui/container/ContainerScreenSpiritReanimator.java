package org.cyclops.evilcraft.client.gui.container;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSpiritFurnace;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritReanimator;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenTileWorking;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritReanimator;

import java.util.List;

/**
 * GUI for the {@link BlockSpiritFurnace}.
 * @author rubensworks
 *
 */
public class ContainerScreenSpiritReanimator extends ContainerScreenTileWorking<ContainerSpiritReanimator, BlockEntitySpiritReanimator> {

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

    public ContainerScreenSpiritReanimator(ContainerSpiritReanimator container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
        this.setProgress(PROGRESSWIDTH, PROGRESSHEIGHT, PROGRESSX, PROGRESSY, PROGRESSTARGETX, PROGRESSTARGETY);
    }

    @Override
    protected Component getName() {
        return Component.translatable("block.evilcraft.spirit_reanimator");
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "spirit_reanimator_gui.png");
    }

    @Override
    protected void drawAdditionalForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        String prefix = RegistryEntries.BLOCK_SPIRIT_REANIMATOR.getDescriptionId() + ".help.invalid";
        List<Component> lines = Lists.newArrayList();
        lines.add(Component.translatable(prefix));
        String entityName = getMenu().getEntityName();
        if (entityName == null) {
            lines.add(Component.translatable(prefix + ".no_entity"));
        } else if (entityName.isEmpty()) {
            lines.add(Component.translatable(prefix + ".invalid_entity"));
        }
        else {
            ItemStack outputStack = getMenu().getContainerInventory().getItem(BlockEntitySpiritReanimator.SLOTS_OUTPUT);
            if (!outputStack.isEmpty() && outputStack.getItem() instanceof SpawnEggItem
                    && ((SpawnEggItem) outputStack.getItem()).getType(outputStack.getTag()) != ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityName))) {
                lines.add(Component.translatable(prefix + ".different_egg"));
            }
        }
        if(lines.size() > 1) {
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
    protected int getProgressXScaled(int width) {
        return width;
    }

    @Override
    protected int getProgressYScaled(int height) {
        return getMenu().getProgress(0) * PROGRESSHEIGHT / getMenu().getMaxProgress(0);
    }

}
