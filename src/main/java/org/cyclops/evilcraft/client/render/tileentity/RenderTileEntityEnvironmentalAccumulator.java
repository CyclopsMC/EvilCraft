package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.block.BlockEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.TileEnvironmentalAccumulator;


/**
 * Renderer for the {@link BlockEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class RenderTileEntityEnvironmentalAccumulator extends RenderTileEntityBeacon<TileEnvironmentalAccumulator> {
    
    // Speed at which the item should spin in the animation
    private static final int ITEM_SPIN_SPEED = 3;

    public RenderTileEntityEnvironmentalAccumulator(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void renderBeacon(TileEnvironmentalAccumulator tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        // Render the an item moving up if we're currently processing one
        if (tile.getMovingItemY() != -1.0f) {
            matrixStack.pushPose();
            matrixStack.translate(-0.5f, -0.5f + tile.getMovingItemY(), -0.5f);
            renderProcessingItem(matrixStack, bufferIn, tile.getRecipe(), tile.getDegradationWorld(), partialTicks);
            matrixStack.popPose();
        }

        super.renderBeacon(tile, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    @Override
    protected boolean isInnerBeam(TileEnvironmentalAccumulator tile) {
        return tile.getMovingItemY() >= 0;
    }

    private void renderProcessingItem(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, RecipeEnvironmentalAccumulator recipe, World world, float partialTickTime) {
        if (recipe == null)
            return;
        
        ItemStack stack = recipe.getInputIngredient().getItems()[0];
        if (stack.isEmpty())
            return;
        
        // Calculate angle for the spinning item
        double totalTickTime = world.getGameTime() + partialTickTime;
        float angle = (float) (ITEM_SPIN_SPEED * (totalTickTime % 360));
        
        // Draw the actual item at the origin
        if (stack.getItem() instanceof BlockItem) {
            matrixStackIn.translate(1F, 0.675F, 1F);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(angle));

        } else {
            matrixStackIn.translate(1F, 1F, 1F);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(angle));
        }
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
    }
}
