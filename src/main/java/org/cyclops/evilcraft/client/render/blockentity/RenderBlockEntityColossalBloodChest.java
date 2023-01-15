package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockColossalBloodChest;
import org.cyclops.evilcraft.blockentity.BlockEntityColossalBloodChest;

/**
 * Renderer for the {@link BlockColossalBloodChest}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityColossalBloodChest extends RenderBlockEntityChestBase<BlockEntityColossalBloodChest> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "model/colossal_blood_chest");

    public RenderBlockEntityColossalBloodChest(BlockEntityRendererProvider.Context p_i226008_1_) {
        super(p_i226008_1_);
    }

    @Override
    protected Material getMaterial(BlockEntityColossalBloodChest tile) {
        return new Material(Sheets.CHEST_SHEET, TEXTURE);
    }

    @Override
    public boolean shouldRenderOffScreen(BlockEntityColossalBloodChest tile) {
        return true;
    }

    @Override
    protected void handleRotation(BlockEntityColossalBloodChest tile, PoseStack matrixStack) {
        // Move origin to center of chest
        if(tile.isStructureComplete()) {
            Vec3i renderOffset = tile.getRenderOffset();
            matrixStack.translate(-renderOffset.getX(), -renderOffset.getY(), -renderOffset.getZ());
        }

        // Rotate
        super.handleRotation(tile, matrixStack);

        // Move chest slightly higher
        matrixStack.translate(0F, tile.getSizeSingular() * 0.0625F, 0F);

        // Scale
        float size = tile.getSizeSingular() * 1.125F;
        matrixStack.scale(size, size, size);
    }

    @Override
    public void render(BlockEntityColossalBloodChest tile, float partialTicks, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        if (tile.isStructureComplete()) {
            super.render(tile, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        }
    }

    @Override
    protected Direction getDirection(BlockEntityColossalBloodChest tileEntityIn) {
        return tileEntityIn.getRotation().getOpposite();
    }
}
