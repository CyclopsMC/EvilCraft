package org.cyclops.evilcraft.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockBloodChest;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenContainerTankInventory;
import org.cyclops.evilcraft.inventory.container.ContainerBloodChest;
import org.cyclops.evilcraft.tileentity.TileBloodChest;

/**
 * GUI for the {@link BlockBloodChest}.
 * @author rubensworks
 *
 */
public class ContainerScreenBloodChest extends ContainerScreenContainerTankInventory<ContainerBloodChest, TileBloodChest> {

    private static final int TEXTUREWIDTH = 196;
	//private static final int TEXTUREHEIGHT = 166;
    
    private static final int TANKWIDTH = 16;
    private static final int TANKHEIGHT = 58;
    private static final int TANKX = TEXTUREWIDTH;
    private static final int TANKY = 0;
    private static final int TANKTARGETX = 63;
    private static final int TANKTARGETY = 72;

    public ContainerScreenBloodChest(ContainerBloodChest container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
    }

    @Override
    protected ITextComponent getName() {
        return new TranslationTextComponent("block.evilcraft.blood_chest");
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "blood_chest_gui.png");
    }

    @Override
    protected int getBaseXSize() {
        return TEXTUREWIDTH;
    }

    @Override
    protected void drawForgegroundString(MatrixStack matrixStack) {
        // MCP: drawString
        font.func_243248_b(matrixStack, getName(), 28 + offsetX, 4 + offsetY, 4210752);
    }

}
