package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosureBaked;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

/**
 * Renderer for the {@link BlockBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class RenderTileEntityBoxOfEternalClosure extends RendererTileEntityEndPortalBase<TileBoxOfEternalClosure> {

	private static final ResourceLocation beamTexture =
			new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "beam.png");
    private static final RenderType renderTypeBeam = RenderType.getEntitySmoothCutout(beamTexture);

    public RenderTileEntityBoxOfEternalClosure(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileBoxOfEternalClosure tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = BlockHelpers.getSafeBlockStateProperty(
                tile.getWorld().getBlockState(tile.getPos()), BlockBoxOfEternalClosure.FACING, Direction.NORTH);
        matrixStackIn.push();
        short rotation = 0;
        if (direction == Direction.SOUTH) {
            rotation = -90;
        }
        if (direction == Direction.NORTH) {
            rotation = 90;
        }
        if (direction == Direction.WEST) {
            rotation = 180;
        }
        if (direction == Direction.EAST) {
            rotation = 0;
        }

        matrixStackIn.translate(0.5F, 0.5F, 0.5F);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
        matrixStackIn.translate(-0.5F, -0.5F, -0.5F);

        // Render box
        BlockState blockState = tile.getBlockState();
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModelBrightnessColor(matrixStackIn.getLast(), bufferIn.getBuffer(Atlases.getSolidBlockType()), blockState, ModelBoxOfEternalClosureBaked.boxModel, 1.0F, 1.0F, 1.0F, combinedLightIn, OverlayTexture.NO_OVERLAY);

        // Render lid
        float angle = tile.getPreviousLidAngle()
                + (tile.getLidAngle() - tile.getPreviousLidAngle()) * partialTicks;
        matrixStackIn.push();
        matrixStackIn.translate(0.75F, 0.375F, 0F);
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-angle));
        matrixStackIn.translate(-0.75F, -0.375F, 0F);
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModelBrightnessColor(matrixStackIn.getLast(), bufferIn.getBuffer(Atlases.getSolidBlockType()), blockState, ModelBoxOfEternalClosureBaked.boxLidModel, 1.0F, 1.0F, 1.0F, combinedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.pop();

        // Render box inside
        if(angle > 0) {
            super.render(tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        matrixStackIn.pop();
    	
    	// Optionally render beam
        // Copied from EnderCrystalRenderer
    	EntityVengeanceSpirit target = tile.getTargetSpirit();
    	if(target != null) {
            float f = func_229051_a_(tile, partialTicks);
            BlockPos blockpos = tile.getPos();

            float f3 = (float)target.getPosX() + 0.5F;
            float f4 = (float)target.getPosY() + 0.5F - (target.getEyeHeight() / 2);
            float f5 = (float)target.getPosZ()  + 0.5F;
            float f6 = (float)((double)f3 - blockpos.getX());
            float f7 = (float)((double)f4 - blockpos.getY());
            float f8 = (float)((double)f5 - blockpos.getZ());

            matrixStackIn.translate(f6, f7, f8);
            IRenderTypeBuffer bufferOverride = (type) -> bufferIn.getBuffer(renderTypeBeam);
            EnderDragonRenderer.func_229059_a_(-f6, -f7 + f, -f8, partialTicks, tile.innerRotation, matrixStackIn, bufferOverride, combinedLightIn);
        }
    }

    @Override
    public boolean shouldRenderFace(Direction direction) {
        return direction == Direction.UP;
    }

    public static float func_229051_a_(TileBoxOfEternalClosure p_229051_0_, float p_229051_1_) {
        float f = (float)p_229051_0_.innerRotation + p_229051_1_;
        float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
        f1 = (f1 * f1 + f1) * 0.4F;
        return f1 - 1.4F;
    }

    @Override
    protected void renderCube(TileBoxOfEternalClosure tile, float p_228883_2_, float p_228883_3_, Matrix4f p_228883_4_, IVertexBuilder vb) {
        float f = (RANDOM.nextFloat() * 0.5F + 0.1F) * p_228883_3_;
        float f1 = (RANDOM.nextFloat() * 0.5F + 0.4F) * p_228883_3_;
        float f2 = (RANDOM.nextFloat() * 0.5F + 0.5F) * p_228883_3_;
        this.renderFace(tile, p_228883_4_, vb, 0.3125F, 1.0F - 0.3125F, p_228883_2_ - 0.5F, p_228883_2_ - 0.5F, 1.0F, 1.0F, 0.0F, 0.0F, f, f1, f2, Direction.UP);
    }
    
}
