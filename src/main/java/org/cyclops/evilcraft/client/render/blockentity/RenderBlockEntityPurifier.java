package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;
import org.cyclops.evilcraft.blockentity.tickaction.purifier.DisenchantPurifyAction;
import org.jetbrains.annotations.Nullable;

/**
 * Renderer for the item inside the {@link org.cyclops.evilcraft.block.BlockPurifier}.
 *
 * @author rubensworks
 *
 */
public class RenderBlockEntityPurifier implements BlockEntityRenderer<BlockEntityPurifier, RenderBlockEntityPurifier.RenderState> {

    public static final SpriteId TEXTURE_BLOOK = new SpriteId(Sheets.CHEST_SHEET, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "entity/blook"));
    private final SpriteGetter sprites;
    private final BookModel enchantmentBook;

    public RenderBlockEntityPurifier(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.enchantmentBook = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(BlockEntityPurifier blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.level = blockEntity.getLevel();
        renderState.additionalItem = blockEntity.getAdditionalItem();
        renderState.purifyItem = blockEntity.getPurifyItem();
        renderState.randomRotation = blockEntity.getRandomRotation();
        renderState.fluid = blockEntity.getTank().getFluid();
        renderState.capacity = blockEntity.getTank().getCapacity();
        renderState.partialTicks = partialTick;
        renderState.tickCount = blockEntity.tickCount;
        renderState.additionalRotation2 = blockEntity.additionalRotation2;
        renderState.additionalRotationPrev = blockEntity.additionalRotationPrev;
        renderState.oFlip = blockEntity.oFlip;
        renderState.flip = blockEntity.flip;
        renderState.oOpen = blockEntity.oOpen;
        renderState.open = blockEntity.open;
    }

    @Override
    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        // Render item above
        ItemStack additionalItem = renderState.additionalItem;
        if (!additionalItem.isEmpty()) {
            if (additionalItem.getItem() == DisenchantPurifyAction.ALLOWED_BOOK.get() || additionalItem.getItem() == Items.ENCHANTED_BOOK) {
                renderBook(renderState, poseStack, submitNodeCollector, additionalItem, renderState.partialTicks);
            } else {
                renderAdditionalItem(renderState, poseStack, submitNodeCollector, additionalItem, renderState.partialTicks);
            }
        }

        // Render item inside
        poseStack.pushPose();
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        if (!renderState.purifyItem.isEmpty()) {
            renderItem(poseStack, submitNodeCollector, renderState.purifyItem, renderState.randomRotation, renderState.level);
        }
        poseStack.popPose();

        // Render fluid
        FluidStack fluid = renderState.fluid;
        IModHelpersNeoForge.get().getRenderHelpers().renderFluidContext(fluid, poseStack, () -> {
            float height = (float) ((fluid.getAmount() * 0.7D) / renderState.capacity + 0.23D + 0.01D);
            int brightness = Math.max(renderState.lightCoords, fluid.getFluid().getFluidType().getLightLevel(fluid));
            int l2 = brightness >> 0x10 & 0xFFFF;
            int i3 = brightness & 0xFFFF;

            TextureAtlasSprite icon = IModHelpersNeoForge.get().getRenderHelpers().getFluidIcon(fluid, Direction.UP);
            Triple<Float, Float, Float> color = IModHelpers.get().getBaseHelpers().intToRGB(IModHelpersNeoForge.get().getRenderHelpers().getFluidBakedQuadColor(fluid));

            submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(icon.atlasLocation()), (pose, vb1) -> {
                vb1.addVertex(pose, 0.0625F, height, 0.0625F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU0(), icon.getV1()).setUv2(l2, i3);
                vb1.addVertex(pose, 0.0625F, height, 0.9375F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU0(), icon.getV0()).setUv2(l2, i3);
                vb1.addVertex(pose, 0.9375F, height, 0.9375F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU1(), icon.getV0()).setUv2(l2, i3);
                vb1.addVertex(pose, 0.9375F, height, 0.0625F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU1(), icon.getV1()).setUv2(l2, i3);
            });
        });
    }

    private void renderItem(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, float rotation, Level level) {
        poseStack.pushPose();
        ItemStackRenderState renderState = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(renderState, itemStack, ItemDisplayContext.FIXED, level, null, 0);
        if (itemStack.getItem() instanceof BlockItem) {
            poseStack.translate(1F, 1.2F, 1F);
            poseStack.scale(0.6F, 0.6F, 0.6F);
        } else {
            poseStack.translate(1F, 1.2F, 1F);
            poseStack.mulPose(Axis.XP.rotationDegrees(25F));
            poseStack.mulPose(Axis.YP.rotationDegrees(25F));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        }

        renderState.submit(poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    private void renderAdditionalItem(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, float partialTickTime) {
        poseStack.pushPose();
        float tick = (float)renderState.tickCount + partialTickTime;
        poseStack.translate(0.5F, 0.75F, 0.5F);
        poseStack.translate(0.0F, 0.1F + Mth.sin(tick * 0.1F) * 0.01F, 0.0F);
        if (itemStack.getItem() instanceof BlockItem) {
            poseStack.translate(1F, 0.675F, 1F);
        }

        float speedUp;

        for (speedUp = renderState.additionalRotation2 - renderState.additionalRotationPrev; speedUp >= (float)Math.PI; speedUp -= ((float)Math.PI * 2F)) { }

        while (speedUp < -(float)Math.PI) {
            speedUp += ((float)Math.PI * 2F);
        }

        float rotation = renderState.additionalRotationPrev + speedUp * partialTickTime;
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotation * 180.0F / (float) Math.PI));

        poseStack.translate(0F, 0.5F, 0F);
        if (!(itemStack.getItem() instanceof BlockItem)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(25));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        }

        ItemStackRenderState renderStateItem = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(renderStateItem, itemStack, ItemDisplayContext.FIXED, null, null, 0);
        renderStateItem.submit(poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }

    private void renderBook(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, float partialTickTime) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.75F, 0.5F);
        float tick = (float)renderState.tickCount + partialTickTime;
        poseStack.translate(0.0F, 0.1F + Mth.sin(tick * 0.1F) * 0.01F, 0.0F);
        float speedUp;

        for (speedUp = renderState.additionalRotation2 - renderState.additionalRotationPrev; speedUp >= (float)Math.PI; speedUp -= ((float)Math.PI * 2F)) { }

        while (speedUp < -(float)Math.PI) {
            speedUp += ((float)Math.PI * 2F);
        }

        float rotation = renderState.additionalRotationPrev + speedUp * partialTickTime;
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotation * 180.0F / (float) Math.PI));
        poseStack.mulPose(Axis.ZP.rotationDegrees(80.0F));

        float f3 = Mth.lerp(partialTickTime, renderState.oFlip, renderState.flip);
        float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = Mth.lerp(partialTickTime, renderState.oOpen, renderState.open);
        this.enchantmentBook.setupAnim(BookModel.State.forAnimation(rotation, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6));
        SpriteId material = itemStack.getItem() == DisenchantPurifyAction.ALLOWED_BOOK.get() ? TEXTURE_BLOOK : EnchantTableRenderer.BOOK_TEXTURE;
        BookModel.State bookmodel$state = BookModel.State.forAnimation(tick, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), renderState.open);
        submitNodeCollector.submitModel(this.enchantmentBook, bookmodel$state, poseStack, renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, material, this.sprites, 0, renderState.breakProgress);

        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        public Level level;
        public ItemStack additionalItem;
        public ItemStack purifyItem;
        public float randomRotation;
        public FluidStack fluid;
        public int capacity;
        public float partialTicks;
        public Integer tickCount;
        public Float additionalRotation2;
        public Float additionalRotationPrev;
        public float oFlip;
        public float flip;
        public float oOpen;
        public float open;
    }

}
