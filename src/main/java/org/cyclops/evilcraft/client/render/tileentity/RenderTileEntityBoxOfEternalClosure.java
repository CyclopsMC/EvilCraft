package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

    public RenderTileEntityBoxOfEternalClosure(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileBoxOfEternalClosure tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = BlockHelpers.getSafeBlockStateProperty(
                tile.getWorld().getBlockState(tile.getPos()), BlockBoxOfEternalClosure.FACING, Direction.NORTH);
        short rotation = 0;
        if (direction == Direction.SOUTH) {
            rotation = 180;
        }
        if (direction == Direction.NORTH) {
            rotation = 0;
        }
        if (direction == Direction.WEST) {
            rotation = 90;
        }
        if (direction == Direction.EAST) {
            rotation = -90;
        }
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
        matrixStackIn.translate(-0.5F, -0.5F, -0.5F);

        BlockState blockState = tile.getBlockState();
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModelBrightnessColor(matrixStackIn.getLast(), bufferIn.getBuffer(Atlases.getSolidBlockType()), blockState, ModelBoxOfEternalClosureBaked.boxModel, 1.0F, 1.0F, 1.0F, combinedLightIn, OverlayTexture.NO_OVERLAY);

        float angle = tile.getPreviousLidAngle()
                + (tile.getLidAngle() - tile.getPreviousLidAngle()) * partialTicks;
        matrixStackIn.push();
        matrixStackIn.translate(0F, 0.375F, 0.25F);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-angle));
        matrixStackIn.translate(0F, -0.375F, -0.25F);
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModelBrightnessColor(matrixStackIn.getLast(), bufferIn.getBuffer(Atlases.getSolidBlockType()), blockState, ModelBoxOfEternalClosureBaked.boxLidModel, 1.0F, 1.0F, 1.0F, combinedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.pop();

        if(angle > 0) {
            /*
            TODO
            double edgeX = axis == Direction.Axis.Y ? 0.3125D : 0;
            double edgeZ = axis == Direction.Axis.Y ? 0 : 0.3125D;
            worldrenderer.pos(0 + edgeX, (double)f3, 0.0D + edgeZ).color(f11, f12, f13, 1.0F).endVertex();
            worldrenderer.pos(0 + edgeX, (double)f3, 1.0D - edgeZ).color(f11, f12, f13, 1.0F).endVertex();
            worldrenderer.pos(1.0D - edgeX, (double)f3, 1.0D - edgeZ).color(f11, f12, f13, 1.0F).endVertex();
            worldrenderer.pos(1.0D - edgeX, (double)f3, 0.0D + edgeZ).color(f11, f12, f13, 1.0F).endVertex();
             */
            super.render(tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    	
    	// Optionally render beam
        // Copied from EnderCrystalRenderer
    	EntityVengeanceSpirit target = tile.getTargetSpirit();
    	if(target != null) {
            float f = func_229051_a_(tile, partialTicks);
            BlockPos blockpos = tile.getPos();
            float f3 = (float)blockpos.getX() + 0.5F;
            float f4 = (float)blockpos.getY() + 0.5F;
            float f5 = (float)blockpos.getZ() + 0.5F;
            float f6 = (float)((double)f3 - target.getPosX());
            float f7 = (float)((double)f4 - target.getPosY());
            float f8 = (float)((double)f5 - target.getPosZ());
            matrixStackIn.translate((double)f6, (double)f7, (double)f8);
            EnderDragonRenderer.func_229059_a_(-f6, -f7 + f, -f8, partialTicks, tile.innerRotation, matrixStackIn, bufferIn, combinedLightIn);
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
    
}
