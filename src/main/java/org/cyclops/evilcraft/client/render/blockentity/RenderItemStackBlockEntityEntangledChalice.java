package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.item.ItemEntangledChalice;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

/**
 * @author rubensworks
 */
public class RenderItemStackBlockEntityEntangledChalice implements SpecialModelRenderer<ItemStack> {

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public RenderItemStackBlockEntityEntangledChalice() {
        this.blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
    }

    @Override
    public void submit(@Nullable ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int i1, boolean b, int i2) {
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        String tankId = fluidHandler == null ? "null" : fluidHandler.getTankID();
        BlockEntityEntangledChalice tile = new BlockEntityEntangledChalice(BlockPos.ZERO, RegistryEntries.BLOCK_ENTANGLED_CHALICE.get().defaultBlockState());
        tile.setWorldTankId(tankId);
        BlockEntityRenderer<BlockEntityEntangledChalice, BlockEntityRenderState> renderer = this.blockEntityRenderDispatcher.getRenderer(tile);
        BlockEntityRenderState renderState = renderer.createRenderState();
        renderer.extractRenderState(tile, renderState, 0, Vec3.ZERO, null);
        this.blockEntityRenderDispatcher.submit(renderState, poseStack, submitNodeCollector, new CameraRenderState());
    }

    @Override
    public void getExtents(Set<Vector3f> p_428206_) {

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
