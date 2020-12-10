package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.EnchantmentTableTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
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

    public static final Material TEXTURE_BLOOK = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(Reference.MOD_ID, "entity/blook"));
    private final BookModel enchantmentBook = new BookModel();

    public RenderTileEntityPurifier(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
	public void render(TilePurifier tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        // Render item above
        ItemStack additionalItem = tile.getAdditionalItem();
        if(!additionalItem.isEmpty()) {
            if(additionalItem.getItem() == DisenchantPurifyAction.ALLOWED_BOOK.get() || additionalItem.getItem() == Items.ENCHANTED_BOOK) {
                renderBook(tile, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, additionalItem, partialTicks);
            } else {
                renderAdditionalItem(tile, matrixStackIn, bufferIn, additionalItem, partialTicks);
            }
        }

        // Render item inside
        matrixStackIn.push();
        matrixStackIn.translate(-0.5F, -0.5F, -0.5F);
        if(!tile.getPurifyItem().isEmpty()) {
            renderItem(matrixStackIn, bufferIn, tile.getPurifyItem(), tile.getRandomRotation());
        }
        matrixStackIn.pop();

        // Render fluid
        FluidStack fluid = tile.getTank().getFluid();
        RenderHelpers.renderFluidContext(fluid, matrixStackIn, () -> {
            float height = (float) ((fluid.getAmount() * 0.7D) / tile.getTank().getCapacity() + 0.23D + 0.01D);
            int brightness = Math.max(combinedLightIn, fluid.getFluid().getAttributes().getLuminosity(fluid));
            int l2 = brightness >> 0x10 & 0xFFFF;
            int i3 = brightness & 0xFFFF;

            TextureAtlasSprite icon = RenderHelpers.getFluidIcon(fluid, Direction.UP);
            Triple<Float, Float, Float> color = Helpers.intToRGB(fluid.getFluid().getAttributes().getColor(tile.getWorld(), tile.getPos()));

            IVertexBuilder vb = bufferIn.getBuffer(RenderType.getText(icon.getAtlasTexture().getTextureLocation()));
            Matrix4f matrix = matrixStackIn.getLast().getMatrix();
            vb.pos(matrix, 0.0625F, height, 0.0625F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).tex(icon.getMinU(), icon.getMaxV()).lightmap(l2, i3).endVertex();
            vb.pos(matrix, 0.0625F, height, 0.9375F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).tex(icon.getMinU(), icon.getMinV()).lightmap(l2, i3).endVertex();
            vb.pos(matrix, 0.9375F, height, 0.9375F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).tex(icon.getMaxU(), icon.getMinV()).lightmap(l2, i3).endVertex();
            vb.pos(matrix, 0.9375F, height, 0.0625F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).tex(icon.getMaxU(), icon.getMaxV()).lightmap(l2, i3).endVertex();
        });
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
        matrixStackIn.translate(0.5F, 0.75F, 0.5F);
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
        matrixStackIn.push();
        matrixStackIn.translate(0.5F, 0.75F, 0.5F);
        float tick = (float)tile.tickCount + partialTickTime;
        matrixStackIn.translate(0.0F, 0.1F + MathHelper.sin(tick * 0.1F) * 0.01F, 0.0F);
        float speedUp;

        for (speedUp = tile.additionalRotation2 - tile.additionalRotationPrev; speedUp >= (float)Math.PI; speedUp -= ((float)Math.PI * 2F)) { }

        while (speedUp < -(float)Math.PI) {
            speedUp += ((float)Math.PI * 2F);
        }

        float rotation = tile.additionalRotationPrev + speedUp * partialTickTime;
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-rotation * 180.0F / (float) Math.PI));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(80.0F));

        float f3 = MathHelper.lerp(partialTickTime, tile.field_195524_g, tile.field_195523_f);
        float f4 = MathHelper.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = MathHelper.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = MathHelper.lerp(partialTickTime, tile.field_195528_k, tile.field_195527_j);
        this.enchantmentBook.func_228247_a_(rotation, MathHelper.clamp(f4, 0.0F, 1.0F), MathHelper.clamp(f5, 0.0F, 1.0F), f6);
        Material material = itemStack.getItem() == DisenchantPurifyAction.ALLOWED_BOOK.get() ? TEXTURE_BLOOK : EnchantmentTableTileEntityRenderer.TEXTURE_BOOK;
        IVertexBuilder ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntitySolid);
        this.enchantmentBook.func_228249_b_(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.pop();
	}

}
