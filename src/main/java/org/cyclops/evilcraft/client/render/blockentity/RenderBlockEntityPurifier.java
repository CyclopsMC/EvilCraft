package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;
import org.cyclops.evilcraft.blockentity.tickaction.purifier.DisenchantPurifyAction;
import org.joml.Matrix4f;

/**
 * Renderer for the item inside the {@link org.cyclops.evilcraft.block.BlockPurifier}.
 *
 * @author rubensworks
 *
 */
public class RenderBlockEntityPurifier implements BlockEntityRenderer<BlockEntityPurifier> {

    public static final Material TEXTURE_BLOOK = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(Reference.MOD_ID, "entity/blook"));
    private final BookModel enchantmentBook;

    public RenderBlockEntityPurifier(BlockEntityRendererProvider.Context context) {
        this.enchantmentBook = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void render(BlockEntityPurifier tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
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
        matrixStackIn.pushPose();
        matrixStackIn.translate(-0.5F, -0.5F, -0.5F);
        if(!tile.getPurifyItem().isEmpty()) {
            renderItem(matrixStackIn, bufferIn, tile.getPurifyItem(), tile.getRandomRotation(), tile.getLevel());
        }
        matrixStackIn.popPose();

        // Render fluid
        FluidStack fluid = tile.getTank().getFluid();
        RenderHelpers.renderFluidContext(fluid, matrixStackIn, () -> {
            float height = (float) ((fluid.getAmount() * 0.7D) / tile.getTank().getCapacity() + 0.23D + 0.01D);
            int brightness = Math.max(combinedLightIn, fluid.getFluid().getFluidType().getLightLevel(fluid));
            int l2 = brightness >> 0x10 & 0xFFFF;
            int i3 = brightness & 0xFFFF;

            TextureAtlasSprite icon = RenderHelpers.getFluidIcon(fluid, Direction.UP);
            IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid.getFluid());
            Triple<Float, Float, Float> color = Helpers.intToRGB(renderProperties.getTintColor(fluid.getFluid().defaultFluidState(), tile.getLevel(), tile.getBlockPos()));

            VertexConsumer vb = bufferIn.getBuffer(RenderType.text(icon.atlasLocation()));
            Matrix4f matrix = matrixStackIn.last().pose();
            vb.vertex(matrix, 0.0625F, height, 0.0625F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).uv(icon.getU0(), icon.getV1()).uv2(l2, i3).endVertex();
            vb.vertex(matrix, 0.0625F, height, 0.9375F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).uv(icon.getU0(), icon.getV0()).uv2(l2, i3).endVertex();
            vb.vertex(matrix, 0.9375F, height, 0.9375F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).uv(icon.getU1(), icon.getV0()).uv2(l2, i3).endVertex();
            vb.vertex(matrix, 0.9375F, height, 0.0625F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).uv(icon.getU1(), icon.getV1()).uv2(l2, i3).endVertex();
        });
    }

    private void renderItem(PoseStack matrixStackIn, MultiBufferSource bufferIn, ItemStack itemStack, float rotation, Level level) {
        matrixStackIn.pushPose();
        if (itemStack.getItem() instanceof BlockItem) {
            matrixStackIn.translate(1F, 1.2F, 1F);
            matrixStackIn.scale(0.6F, 0.6F, 0.6F);
        } else {
            matrixStackIn.translate(1F, 1.2F, 1F);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(25F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(25F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
        }

        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, level, 0);
        matrixStackIn.popPose();
    }

    private void renderAdditionalItem(BlockEntityPurifier tile, PoseStack matrixStackIn, MultiBufferSource bufferIn, ItemStack itemStack, float partialTickTime) {
        matrixStackIn.pushPose();
        float tick = (float)tile.tickCount + partialTickTime;
        matrixStackIn.translate(0.5F, 0.75F, 0.5F);
        matrixStackIn.translate(0.0F, 0.1F + Mth.sin(tick * 0.1F) * 0.01F, 0.0F);
        if (itemStack.getItem() instanceof BlockItem) {
            matrixStackIn.translate(1F, 0.675F, 1F);
        }

        float speedUp;

        for (speedUp = tile.additionalRotation2 - tile.additionalRotationPrev; speedUp >= (float)Math.PI; speedUp -= ((float)Math.PI * 2F)) { }

        while (speedUp < -(float)Math.PI) {
            speedUp += ((float)Math.PI * 2F);
        }

        float rotation = tile.additionalRotationPrev + speedUp * partialTickTime;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-rotation * 180.0F / (float) Math.PI));

        matrixStackIn.translate(0F, 0.5F, 0F);
        if (!(itemStack.getItem() instanceof BlockItem)) {
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(25));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
        }

        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, tile.getLevel(), 0);

        matrixStackIn.popPose();
    }

    private void renderBook(BlockEntityPurifier tile, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, ItemStack itemStack, float partialTickTime) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 0.75F, 0.5F);
        float tick = (float)tile.tickCount + partialTickTime;
        matrixStackIn.translate(0.0F, 0.1F + Mth.sin(tick * 0.1F) * 0.01F, 0.0F);
        float speedUp;

        for (speedUp = tile.additionalRotation2 - tile.additionalRotationPrev; speedUp >= (float)Math.PI; speedUp -= ((float)Math.PI * 2F)) { }

        while (speedUp < -(float)Math.PI) {
            speedUp += ((float)Math.PI * 2F);
        }

        float rotation = tile.additionalRotationPrev + speedUp * partialTickTime;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-rotation * 180.0F / (float) Math.PI));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(80.0F));

        float f3 = Mth.lerp(partialTickTime, tile.oFlip, tile.flip);
        float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = Mth.lerp(partialTickTime, tile.oOpen, tile.open);
        this.enchantmentBook.setupAnim(rotation, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);
        Material material = itemStack.getItem() == DisenchantPurifyAction.ALLOWED_BOOK.get() ? TEXTURE_BLOOK : EnchantTableRenderer.BOOK_LOCATION;
        VertexConsumer ivertexbuilder = material.buffer(bufferIn, RenderType::entitySolid);
        this.enchantmentBook.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.popPose();
    }

}
