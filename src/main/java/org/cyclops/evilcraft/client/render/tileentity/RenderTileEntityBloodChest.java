package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockBloodChest;
import org.cyclops.evilcraft.tileentity.TileBloodChest;

/**
 * Renderer for the {@link BlockBloodChest}.
 * @author rubensworks
 *
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderTileEntityBloodChest extends RenderTileEntityChestBase<TileBloodChest> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "model/blood_chest");

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
            event.addSprite(TEXTURE);
        }
    }

    public RenderTileEntityBloodChest(TileEntityRendererDispatcher p_i226008_1_) {
        super(p_i226008_1_);
    }

    @Override
    protected Material getMaterial(TileBloodChest tile) {
        return new Material(Atlases.CHEST_ATLAS, TEXTURE);
    }

    @Override
    protected Direction getDirection(TileBloodChest tile) {
        return tile.getRotation();
    }

    @Override
    public void render(TileBloodChest tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.push();
        matrixStack.translate(0.325F, 0F, 0.325F);
        float size = 0.3F * 1.125F;
        matrixStack.scale(size, size, size);
        super.render(tile, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        matrixStack.pop();
    }
}
