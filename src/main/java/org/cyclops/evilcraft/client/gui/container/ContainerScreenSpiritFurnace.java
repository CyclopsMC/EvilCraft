package org.cyclops.evilcraft.client.gui.container;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSpiritFurnace;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenWorking;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritFurnace;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

import java.util.List;

/**
 * GUI for the {@link BlockSpiritFurnace}.
 * @author rubensworks
 *
 */
public class ContainerScreenSpiritFurnace extends ContainerScreenWorking<ContainerSpiritFurnace, TileSpiritFurnace> {
    
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

    public ContainerScreenSpiritFurnace(ContainerSpiritFurnace container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
        this.setProgress(PROGRESSWIDTH, PROGRESSHEIGHT, PROGRESSX, PROGRESSY, PROGRESSTARGETX, PROGRESSTARGETY);
    }

    @Override
    protected ITextComponent getName() {
        return new TranslationTextComponent("tile.blocks.evilcraft.spirit_furnace.name");
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.TEXTURE_PATH_GUI, "spirit_furnace_gui.png");
    }
    
    private String prettyPrintSize(Vec3i size) {
        return size.getX() + "x" + size.getY() + "x" + size.getZ();
    }
    
    @Override
	protected void drawAdditionalForeground(int mouseX, int mouseY) {
    	String prefix = RegistryEntries.BLOCK_SPIRIT_FURNACE.getTranslationKey() + ".help.invalid";
    	List<ITextComponent> lines = Lists.newArrayList();
    	lines.add(new TranslationTextComponent(prefix));
        if (!getContainer().hasEntity()) {
        	lines.add(new TranslationTextComponent(prefix + ".no_entity"));
        } else if (!getContainer().isSizeValidForEntity()) {
        	lines.add(new TranslationTextComponent(prefix + ".content_size", prettyPrintSize(getContainer().getInnerSize())));
        	lines.add(new TranslationTextComponent(prefix + ".required_size", prettyPrintSize(getContainer().getEntitySize())));
        } else if (getContainer().isForceHalt()) {
        	lines.add(new TranslationTextComponent(prefix + ".force_halt"));
        }
        else if (getContainer().isCaughtError()) {
        	lines.add(new TranslationTextComponent(prefix + ".caught_error"));
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
    
}
