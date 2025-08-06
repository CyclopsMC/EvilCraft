package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import org.cyclops.cyclopscore.client.render.blockentity.ItemStackBlockEntityRendererBase;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodChest;

/**
 * @author rubensworks
 */
public class RenderItemStackBlockEntityBloodChest extends ItemStackBlockEntityRendererBase {

    public RenderItemStackBlockEntityBloodChest() {
        super(() -> new BlockEntityBloodChest(BlockPos.ZERO, RegistryEntries.BLOCK_BLOOD_CHEST.get().defaultBlockState()));
    }

    public static record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<RenderItemStackBlockEntityBloodChest.Unbaked> MAP_CODEC = MapCodec.unit(RenderItemStackBlockEntityBloodChest.Unbaked::new);

        @Override
        public MapCodec<RenderItemStackBlockEntityBloodChest.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet entityModelSet) {
            return new RenderItemStackBlockEntityBloodChest();
        }
    }
}
