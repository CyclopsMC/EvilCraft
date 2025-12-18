package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import org.cyclops.cyclopscore.client.render.blockentity.ItemStackBlockEntityRendererBase;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;

/**
 * @author rubensworks
 */
public class RenderItemStackBlockEntityBoxOfEternalClosure extends ItemStackBlockEntityRendererBase {

    public RenderItemStackBlockEntityBoxOfEternalClosure() {
        super(() -> new BlockEntityBoxOfEternalClosure(BlockPos.ZERO, RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.get().defaultBlockState()));
    }

    public static record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<RenderItemStackBlockEntityBoxOfEternalClosure.Unbaked> MAP_CODEC = MapCodec.unit(RenderItemStackBlockEntityBoxOfEternalClosure.Unbaked::new);

        @Override
        public MapCodec<RenderItemStackBlockEntityBoxOfEternalClosure.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(BakingContext bakingContext) {
            return new RenderItemStackBlockEntityBoxOfEternalClosure();
        }
    }
}
