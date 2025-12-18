package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.cyclops.evilcraft.block.BlockEnvironmentalAccumulator;
import org.cyclops.evilcraft.blockentity.BlockEntityEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.display.RecipeDisplayEnvironmentalAccumulator;
import org.jetbrains.annotations.Nullable;


/**
 * Renderer for the {@link BlockEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityEnvironmentalAccumulator extends RenderBlockEntityBeacon<BlockEntityEnvironmentalAccumulator, RenderBlockEntityEnvironmentalAccumulator.RenderState> {

    // Speed at which the item should spin in the animation
    private static final int ITEM_SPIN_SPEED = 3;

    public RenderBlockEntityEnvironmentalAccumulator(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(BlockEntityEnvironmentalAccumulator blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.movingItemY = blockEntity.getMovingItemY();
        renderState.level = blockEntity.getLevel();
        renderState.recipeDisplay = blockEntity.getRecipeDisplay();
    }

    @Override
    protected void submitBeacon(RenderState renderState, float partialTicks, PoseStack matrixStackIn, SubmitNodeCollector submitNodeCollector) {
        // Render the an item moving up if we're currently processing one
        if (renderState.movingItemY != -1.0f) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(-0.5f, -0.5f + renderState.movingItemY, -0.5f);
            submitProcessingItem(matrixStackIn, submitNodeCollector, renderState.recipeDisplay, renderState.level, partialTicks);
            matrixStackIn.popPose();
        }

        super.submitBeacon(renderState, partialTicks, matrixStackIn, submitNodeCollector);
    }

    @Override
    protected boolean isInnerBeam(BlockEntityEnvironmentalAccumulator tile) {
        return tile.getMovingItemY() >= 0;
    }

    private void submitProcessingItem(PoseStack matrixStackIn, SubmitNodeCollector submitNodeCollector, RecipeDisplayEnvironmentalAccumulator recipe, Level world, float partialTickTime) {
        if (recipe == null)
            return;

        ItemStack stack = recipe.inputIngredient().resolveForFirstStack(SlotDisplayContext.fromLevel(world));
        ItemStackRenderState renderState = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(renderState, stack, ItemDisplayContext.FIXED, world, null, 0);

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

        renderState.submit(matrixStackIn, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
    }

    public static class RenderState extends RenderBlockEntityBeacon.RenderState {
        public float movingItemY;
        public RecipeDisplayEnvironmentalAccumulator recipeDisplay;
        public Level level;
    }
}
