package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockPurifier;
import org.cyclops.evilcraft.tileentity.TilePurifier;
import org.cyclops.evilcraft.tileentity.tickaction.purifier.DisenchantPurifyAction;

/**
 * Renderer for the item inside the {@link BlockPurifier}.
 * 
 * @author rubensworks
 *
 */
public class RenderTileEntityPurifier extends TileEntityRenderer<TilePurifier> {

    private static final ResourceLocation TEXTURE_BLOOK = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "blook.png");
    private static final ResourceLocation TEXTURE_ENCHANTEDBOOK = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "enchanted_book.png");
    private BookModel enchantmentBook = new BookModel();

    public RenderTileEntityPurifier(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
	public void render(TilePurifier tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack additionalItem = tile.getAdditionalItem();
        if(!additionalItem.isEmpty()) {
            if(additionalItem.getItem() == DisenchantPurifyAction.ALLOWED_BOOK.get() || additionalItem.getItem() == Items.ENCHANTED_BOOK) {
                renderBook(tile, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, additionalItem, partialTicks);
            } else {
                renderAdditionalItem(tile, matrixStackIn, bufferIn, additionalItem, partialTicks);
            }
        }

        matrixStackIn.push();
	    float var10 = -0.5F;
        float var11 = -0.5F;
        float var12 = -0.5F;
        matrixStackIn.translate(var10, var11, var12);

        if(!tile.getPurifyItem().isEmpty()) {
            renderItem(matrixStackIn, bufferIn, tile.getPurifyItem(), tile.getRandomRotation());
        }

        matrixStackIn.pop();
	}
	
	private void renderItem(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, ItemStack itemStack, float rotation) {
        matrixStackIn.push();
        if (itemStack.getItem() instanceof BlockItem) {
            matrixStackIn.translate(1F, 1.2F, 1F);
            matrixStackIn.scale(0.6F, 0.6F, 0.6F);
        } else {
            matrixStackIn.translate(1F, 1.2F, 1F);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(25F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(25F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
        }

        Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();
    }

    private void renderAdditionalItem(TilePurifier tile, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, ItemStack itemStack, float partialTickTime) {
        matrixStackIn.push();
        float tick = (float)tile.tickCount + partialTickTime;
        matrixStackIn.translate(0.0F, 0.1F + MathHelper.sin(tick * 0.1F) * 0.01F, 0.0F);
        if (itemStack.getItem() instanceof BlockItem) {
            matrixStackIn.translate(1F, 0.675F, 1F);
        }

        float speedUp;

        for (speedUp = tile.additionalRotation2 - tile.additionalRotationPrev; speedUp >= (float)Math.PI; speedUp -= ((float)Math.PI * 2F)) { }

        while (speedUp < -(float)Math.PI) {
            speedUp += ((float)Math.PI * 2F);
        }

        float rotation = tile.additionalRotationPrev + speedUp * partialTickTime;
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-rotation * 180.0F / (float) Math.PI));

        matrixStackIn.translate(0F, 0.5F, 0F);
        if (!(itemStack.getItem() instanceof BlockItem)) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(25));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
        }

        Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);

        matrixStackIn.pop();
    }

	private void renderBook(TilePurifier tile, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, ItemStack itemStack, float partialTickTime) {
	    GlStateManager.pushMatrix();
        float tick = (float)tile.tickCount + partialTickTime;
        GlStateManager.translatef(0.0F, 0.1F + MathHelper.sin(tick * 0.1F) * 0.01F, 0.0F);
        float speedUp;

        for (speedUp = tile.additionalRotation2 - tile.additionalRotationPrev; speedUp >= (float)Math.PI; speedUp -= ((float)Math.PI * 2F)) { }

        while (speedUp < -(float)Math.PI) {
            speedUp += ((float)Math.PI * 2F);
        }

        float rotation = tile.additionalRotationPrev + speedUp * partialTickTime;
        GlStateManager.rotatef(-rotation * 180.0F / (float) Math.PI, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(80.0F, 0.0F, 0.0F, 1.0F);

        GlStateManager.enableCull();
        Material material = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, itemStack.getItem() == DisenchantPurifyAction.ALLOWED_BOOK.get() ? TEXTURE_BLOOK : TEXTURE_ENCHANTEDBOOK);
        IVertexBuilder ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntitySolid);
        this.enchantmentBook.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 0, 0, 0, 0);
        GlStateManager.popMatrix();
	}

}
