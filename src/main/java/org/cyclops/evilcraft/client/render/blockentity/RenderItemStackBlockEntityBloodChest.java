package org.cyclops.evilcraft.client.render.blockentity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.cyclops.cyclopscore.client.render.blockentity.ItemStackBlockEntityRendererBase;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodChest;

/**
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class RenderItemStackBlockEntityBloodChest extends ItemStackBlockEntityRendererBase {

    public RenderItemStackBlockEntityBloodChest() {
        super(() -> new BlockEntityBloodChest(BlockPos.ZERO, RegistryEntries.BLOCK_BLOOD_CHEST.defaultBlockState()));
    }

    public static class ItemRenderProperties implements IClientItemExtensions {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return new RenderItemStackBlockEntityBloodChest();
        }
    }
}
