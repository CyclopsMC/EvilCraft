package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.block.BlockEnvironmentalAccumulator;
import org.cyclops.evilcraft.blockentity.BlockEntityEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;


/**
 * Renderer for the {@link BlockEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityEnvironmentalAccumulator extends RenderBlockEntityBeacon<BlockEntityEnvironmentalAccumulator> {

    // Speed at which the item should spin in the animation
    private static final int ITEM_SPIN_SPEED = 3;

    public RenderBlockEntityEnvironmentalAccumulator(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void renderBeacon(BlockEntityEnvironmentalAccumulator tile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
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
    protected boolean isInnerBeam(BlockEntityEnvironmentalAccumulator tile) {
        return tile.getMovingItemY() >= 0;
    }

    private void renderProcessingItem(PoseStack matrixStackIn, MultiBufferSource bufferIn, RecipeEnvironmentalAccumulator recipe, Level world, float partialTickTime) {
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
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(angle));

        } else {
            matrixStackIn.translate(1F, 1F, 1F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(angle));
        }
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, world, 0);
    }
}
