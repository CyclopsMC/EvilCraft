package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;

/**
 * Copy of AbstractEndPortalRenderer without strict type param
 * @author rubensworks
 */
public abstract class RendererBlockEntityEndPortalBase<T extends BlockEntity, S extends BlockEntityRenderState> implements BlockEntityRenderer<T, S> {

    public RendererBlockEntityEndPortalBase(BlockEntityRendererProvider.Context context) {

    }

    public void submit(S p_446622_, PoseStack p_446303_, SubmitNodeCollector p_447279_, CameraRenderState p_451548_) {
        p_447279_.submitCustomGeometry(p_446303_, this.renderType(), (p_446067_, p_445990_) -> this.renderCube(p_446067_.pose(), p_445990_));
    }

    protected void renderCube(Matrix4f pose, VertexConsumer consumer) {
        float f = this.getOffsetDown();
        float f1 = this.getOffsetUp();
        this.renderFace(pose, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderFace(pose, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderFace(pose, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(pose, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderFace(pose, consumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderFace(pose, consumer, 0.0F, 1.0F, f1, f1, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    protected void renderFace(Matrix4f pose, VertexConsumer consumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction direction) {
        if (shouldRenderFace(direction)) {
            consumer.addVertex(pose, x1, y1, z1);
            consumer.addVertex(pose, x2, y1, z2);
            consumer.addVertex(pose, x2, y2, z3);
            consumer.addVertex(pose, x1, y2, z4);
        }

    }

    protected float getOffsetUp() {
        return 0.75F;
    }

    protected float getOffsetDown() {
        return 0.375F;
    }

    protected RenderType renderType() {
        return RenderType.endPortal();
    }

    public abstract boolean shouldRenderFace(Direction direction);
}
