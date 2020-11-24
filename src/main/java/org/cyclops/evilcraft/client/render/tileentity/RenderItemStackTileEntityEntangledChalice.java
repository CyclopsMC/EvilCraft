package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.client.render.tileentity.ItemStackTileEntityRendererBase;
import org.cyclops.evilcraft.item.ItemEntangledChalice;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

/**
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class RenderItemStackTileEntityEntangledChalice extends ItemStackTileEntityRenderer {

    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStackIn).orElse(null);
        String tankId = fluidHandler == null ? "null" : fluidHandler.getTankID();
        TileEntangledChalice tile = new TileEntangledChalice();
        tile.setWorldTankId(tankId);
        TileEntityRendererDispatcher.instance.renderItem(tile, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

}
