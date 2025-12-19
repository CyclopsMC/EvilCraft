package org.cyclops.evilcraft.client.render.blockentity;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodChest;
import org.jetbrains.annotations.Nullable;

/**
 * Renderer for the {@link org.cyclops.evilcraft.block.BlockBloodChest}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityBloodChest extends RenderBlockEntityChestBase<BlockEntityBloodChest, RenderBlockEntityBloodChest.RenderState> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "model/blood_chest");

    public RenderBlockEntityBloodChest(BlockEntityRendererProvider.Context p_i226008_1_) {
        super(p_i226008_1_);
    }

    @Override
    public void extractRenderState(BlockEntityBloodChest blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.rotation = blockEntity.getRotation();
    }

    @Override
    protected Direction getDirection(RenderState renderState) {
        return renderState.rotation;
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public boolean shouldRender(BlockEntityBloodChest blockEntity, Vec3 cameraPos) {
        return blockEntity.getBlockPos() == BlockPos.ZERO || super.shouldRender(blockEntity, cameraPos);
    }

    @Override
    protected Material getMaterial(RenderState renderState) {
        return new Material(Sheets.CHEST_SHEET, TEXTURE);
    }

    public static class RenderState extends RenderBlockEntityChestBase.RenderState {
        public Direction rotation;
    }

}
