package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import org.cyclops.cyclopscore.client.render.tileentity.RenderTileEntityModel;
import org.cyclops.evilcraft.block.ColossalBloodChest;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for the {@link ColossalBloodChest}.
 * @author rubensworks
 *
 */
public class RenderTileEntityColossalBloodChest extends RenderTileEntityModel<TileColossalBloodChest, ModelChest> {

	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderTileEntityColossalBloodChest(ModelChest model, ResourceLocation texture) {
        super(model, texture);
    }

    @Override
    protected void preRotate(TileColossalBloodChest chestTile) {
        if(chestTile.canWork()) {
            Vec3i renderOffset = chestTile.getRenderOffset();
            GlStateManager.translate(-renderOffset.getX(), renderOffset.getY(), renderOffset.getZ());
        }
        GlStateManager.translate(0.5F, 0, 0.5F);
        GlStateManager.scale(3, 3, 3);
    }

    @Override
    protected void postRotate(TileColossalBloodChest tile) {
        GL11.glTranslatef(-0.5F, 0, -0.5F);
    }

    @Override
    protected void renderModel(TileColossalBloodChest chestTile, ModelChest model, float partialTick, int destroyStage) {
        if(chestTile.canWork()) {
            float lidangle = chestTile.prevLidAngle + (chestTile.lidAngle - chestTile.prevLidAngle) * partialTick;
            lidangle = 1.0F - lidangle;
            lidangle = 1.0F - lidangle * lidangle * lidangle;
            model.chestLid.rotateAngleX = -(lidangle * (float) Math.PI / 2.0F);
            GlStateManager.translate(0, -0.3333F, 0);
            model.renderAll();
            GlStateManager.scale(1 / 3, 1 / 3, 1 / 3);
        }
    }
}
