package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

/**
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class RenderItemStackBlockEntityEntangledChalice extends BlockEntityWithoutLevelRenderer {

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public RenderItemStackBlockEntityEntangledChalice() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
    }

    public void render(ItemStack itemStackIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStackIn).orElse(null);
        String tankId = fluidHandler == null ? "null" : fluidHandler.getTankID();
        BlockEntityEntangledChalice tile = new BlockEntityEntangledChalice(BlockPos.ZERO, RegistryEntries.BLOCK_ENTANGLED_CHALICE.defaultBlockState());
        tile.setWorldTankId(tankId);
        this.blockEntityRenderDispatcher.renderItem(tile, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    public static class ItemRenderProperties implements IClientItemExtensions {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return new RenderItemStackBlockEntityEntangledChalice();
        }
    }

}
