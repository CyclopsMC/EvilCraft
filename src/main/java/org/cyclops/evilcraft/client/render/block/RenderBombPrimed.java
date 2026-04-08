package org.cyclops.evilcraft.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Block;

/**
 * Renderer for a primed bomb.
 * @author rubensworks
 *
 */
public class RenderBombPrimed extends TntRenderer {

    protected final Block block;
    private final BlockModelResolver blockModelResolver;

    public RenderBombPrimed(EntityRendererProvider.Context renderContext, Block block) {
        super(renderContext);
        this.block = block;
        this.blockModelResolver = renderContext.getBlockModelResolver();
    }

    @Override
    public void extractRenderState(PrimedTnt entity, TntRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        // Override blockState with our custom block
        this.blockModelResolver.update(state.blockState, this.block.defaultBlockState(), BLOCK_DISPLAY_CONTEXT);
    }

    @Override
    public void submit(TntRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
    }

}
