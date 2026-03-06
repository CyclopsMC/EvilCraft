package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;
import org.cyclops.evilcraft.item.ItemEntangledChalice;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * @author rubensworks
 */
public class RenderItemStackBlockEntityEntangledChalice implements SpecialModelRenderer<ItemStack> {

    @Override
    public void submit(@Nullable ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean isFoil, int seed) {
        if (itemStack == null || itemStack.isEmpty()) return;
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        String tankId = fluidHandler == null ? "" : fluidHandler.getTankID();
        FluidStack fluid = WorldSharedTankCache.getInstance().getTankContent(tankId);
        if (!fluid.isEmpty()) {
            IModHelpersNeoForge.get().getRenderHelpers().renderFluidContext(fluid, poseStack, () -> {
                float height = Math.min(0.95F, ((float) fluid.getAmount() / (float) BlockEntityEntangledChalice.BASE_CAPACITY)) * 0.1875F + 0.8125F;
                int brightness = fluid.getFluid().getFluidType().getLightLevel(fluid);
                int l2 = brightness >> 0x10 & 0xFFFF;
                int i3 = brightness & 0xFFFF;

                TextureAtlasSprite icon = IModHelpersNeoForge.get().getRenderHelpers().getFluidIcon(fluid, Direction.UP);
                Triple<Float, Float, Float> color = IModHelpersNeoForge.get().getRenderHelpers().getFluidVertexBufferColor(fluid);

                submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(icon.atlasLocation()), (pose, vb) -> {
                    vb.addVertex(pose, 0.1875F, height, 0.1875F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU0(), icon.getV1()).setUv2(l2, i3);
                    vb.addVertex(pose, 0.1875F, height, 0.8125F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU0(), icon.getV0()).setUv2(l2, i3);
                    vb.addVertex(pose, 0.8125F, height, 0.8125F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU1(), icon.getV0()).setUv2(l2, i3);
                    vb.addVertex(pose, 0.8125F, height, 0.1875F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU1(), icon.getV1()).setUv2(l2, i3);
                });
            });
        }
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {

    }

    @Override
    public @Nullable ItemStack extractArgument(ItemStack stack) {
        return stack;
    }

    public static record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<RenderItemStackBlockEntityEntangledChalice.Unbaked> MAP_CODEC = MapCodec.unit(RenderItemStackBlockEntityEntangledChalice.Unbaked::new);

        @Override
        public MapCodec<RenderItemStackBlockEntityEntangledChalice.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(BakingContext bakingContext) {
            return new RenderItemStackBlockEntityEntangledChalice();
        }
    }

}
