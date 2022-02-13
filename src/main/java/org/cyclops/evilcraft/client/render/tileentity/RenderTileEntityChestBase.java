package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Calendar;

/**
 * A modified copy of {@link ChestTileEntityRenderer}.
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public abstract class RenderTileEntityChestBase<T extends TileEntity & IChestLid> extends TileEntityRenderer<T> {

    private final ModelRenderer lid;
    private final ModelRenderer bottom;
    private final ModelRenderer lock;
    private final ModelRenderer doubleLeftLid;
    private final ModelRenderer doubleLeftBottom;
    private final ModelRenderer doubleLeftLock;
    private final ModelRenderer doubleRightLid;
    private final ModelRenderer doubleRightBottom;
    private final ModelRenderer doubleRightLock;
    private boolean isChristmas;

    public RenderTileEntityChestBase(TileEntityRendererDispatcher p_i226008_1_) {
        super(p_i226008_1_);
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
            this.isChristmas = true;
        }

        this.bottom = new ModelRenderer(64, 64, 0, 19);
        this.bottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        this.lid = new ModelRenderer(64, 64, 0, 0);
        this.lid.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        this.lid.y = 9.0F;
        this.lid.z = 1.0F;
        this.lock = new ModelRenderer(64, 64, 0, 0);
        this.lock.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
        this.lock.y = 8.0F;
        this.doubleLeftBottom = new ModelRenderer(64, 64, 0, 19);
        this.doubleLeftBottom.addBox(1.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F, 0.0F);
        this.doubleLeftLid = new ModelRenderer(64, 64, 0, 0);
        this.doubleLeftLid.addBox(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
        this.doubleLeftLid.y = 9.0F;
        this.doubleLeftLid.z = 1.0F;
        this.doubleLeftLock = new ModelRenderer(64, 64, 0, 0);
        this.doubleLeftLock.addBox(15.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
        this.doubleLeftLock.y = 8.0F;
        this.doubleRightBottom = new ModelRenderer(64, 64, 0, 19);
        this.doubleRightBottom.addBox(0.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F, 0.0F);
        this.doubleRightLid = new ModelRenderer(64, 64, 0, 0);
        this.doubleRightLid.addBox(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
        this.doubleRightLid.y = 9.0F;
        this.doubleRightLid.z = 1.0F;
        this.doubleRightLock = new ModelRenderer(64, 64, 0, 0);
        this.doubleRightLock.addBox(0.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
        this.doubleRightLock.y = 8.0F;
    }

    protected abstract Direction getDirection(T tileEntityIn);

    protected RenderMaterial getMaterial(T tileEntity) {
        return Atlases.chooseMaterial(tileEntity, ChestType.SINGLE, this.isChristmas);
    }

    protected void handleRotation(T tile, MatrixStack matrixStack) {
        float f = getDirection(tile).toYRot();
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
    }

    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        handleRotation(tileEntityIn, matrixStackIn);
        matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

        float f1 = tileEntityIn.getOpenNess(partialTicks);
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        RenderMaterial material = this.getMaterial(tileEntityIn);
        IVertexBuilder ivertexbuilder = material.buffer(bufferIn, RenderType::entityCutout);
        this.render(matrixStackIn, ivertexbuilder, this.lid, this.lock, this.bottom, f1, combinedLightIn, combinedOverlayIn);

        matrixStackIn.popPose();
    }

    private void render(MatrixStack p_228871_1_, IVertexBuilder p_228871_2_, ModelRenderer p_228871_3_, ModelRenderer p_228871_4_, ModelRenderer p_228871_5_, float p_228871_6_, int p_228871_7_, int p_228871_8_) {
        p_228871_3_.xRot = -(p_228871_6_ * ((float)Math.PI / 2F));
        p_228871_4_.xRot = p_228871_3_.xRot;
        p_228871_3_.render(p_228871_1_, p_228871_2_, p_228871_7_, p_228871_8_);
        p_228871_4_.render(p_228871_1_, p_228871_2_, p_228871_7_, p_228871_8_);
        p_228871_5_.render(p_228871_1_, p_228871_2_, p_228871_7_, p_228871_8_);
    }

}
