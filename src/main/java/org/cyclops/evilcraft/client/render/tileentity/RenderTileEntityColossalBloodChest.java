package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockColossalBloodChest;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;

/**
 * Renderer for the {@link BlockColossalBloodChest}.
 * @author rubensworks
 *
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderTileEntityColossalBloodChest extends RenderTileEntityChestBase<TileColossalBloodChest> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "colossal_blood_chest.png");

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
            event.addSprite(TEXTURE);
        }
    }

    public RenderTileEntityColossalBloodChest(TileEntityRendererDispatcher p_i226008_1_) {
        super(p_i226008_1_);
    }

    @Override
    protected Material getMaterial(TileColossalBloodChest tile) {
        return new Material(Atlases.CHEST_ATLAS, TEXTURE);
    }

    @Override
    public boolean isGlobalRenderer(TileColossalBloodChest tile) {
        return true;
    }

    @Override
    protected void handleRotation(TileColossalBloodChest tile, MatrixStack matrixStack) {
        // Move origin to center of chest
        if(tile.isStructureComplete()) {
            Vec3i renderOffset = tile.getRenderOffset();
            matrixStack.translate(-renderOffset.getX(), -renderOffset.getY(), -renderOffset.getZ());
        }

        // Rotate
        super.handleRotation(tile, matrixStack);

        // Move chest slightly higher
        matrixStack.translate(0F, tile.getSizeSingular() * 0.0625F, 0F);

        // Scale
        float size = tile.getSizeSingular() * 1.125F;
        matrixStack.scale(size, size, size);
    }

    @Override
    public void render(TileColossalBloodChest tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        if (tile.isStructureComplete()) {
            super.render(tile, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        }
    }

    @Override
    protected Direction getDirection(TileColossalBloodChest tileEntityIn) {
        return tileEntityIn.getRotation().getOpposite();
    }
}
