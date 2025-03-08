package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.item.ItemEntangledChalice;
import org.jetbrains.annotations.Nullable;

/**
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class RenderItemStackBlockEntityEntangledChalice implements SpecialModelRenderer<ItemStack> {

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public RenderItemStackBlockEntityEntangledChalice() {
        this.blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
    }

    @Override
    public void render(@Nullable ItemStack patterns, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(patterns).orElse(null);
        String tankId = fluidHandler == null ? "null" : fluidHandler.getTankID();
        BlockEntityEntangledChalice tile = new BlockEntityEntangledChalice(BlockPos.ZERO, RegistryEntries.BLOCK_ENTANGLED_CHALICE.get().defaultBlockState());
        tile.setWorldTankId(tankId);
        this.blockEntityRenderDispatcher.render(tile, 0, poseStack, bufferSource);
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
        public SpecialModelRenderer<?> bake(EntityModelSet entityModelSet) {
            return new RenderItemStackBlockEntityEntangledChalice();
        }
    }

}
