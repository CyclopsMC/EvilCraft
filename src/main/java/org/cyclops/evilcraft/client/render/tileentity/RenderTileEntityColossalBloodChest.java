package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import org.cyclops.cyclopscore.client.render.tileentity.RenderTileEntityModel;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.block.ColossalBloodChest;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for the {@link ColossalBloodChest}.
 * @author rubensworks
 *
 */
public class RenderTileEntityColossalBloodChest extends RenderTileEntityModel<TileColossalBloodChest> {

	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderTileEntityColossalBloodChest(ModelBase model, ResourceLocation texture) {
        super(model, texture);
    }

    @Override
    protected void preRotate(TileColossalBloodChest tile) {
        TileColossalBloodChest chestTile = (TileColossalBloodChest) tile;
        if(chestTile.canWork()) {
            int[] renderOffset = chestTile.getRenderOffset().getCoordinates();
            GlStateManager.translate(-renderOffset[0], renderOffset[1], renderOffset[2]);
        }
        GlStateManager.translate(0.5F, 0, 0.5F);
        GlStateManager.scale(3, 3, 3);
    }

    @Override
    protected void postRotate(TileColossalBloodChest tile) {
        GL11.glTranslatef(-0.5F, 0, -0.5F);
    }

    @Override
    protected void renderModel(TileColossalBloodChest tile, ModelBase model, float partialTick) {
        TileColossalBloodChest chestTile = (TileColossalBloodChest) tile;
        if(chestTile.canWork()) {
            ModelChest modelchest = (ModelChest) model;
            float lidangle = chestTile.prevLidAngle + (chestTile.lidAngle - chestTile.prevLidAngle) * partialTick;
            lidangle = 1.0F - lidangle;
            lidangle = 1.0F - lidangle * lidangle * lidangle;
            modelchest.chestLid.rotateAngleX = -(lidangle * (float) Math.PI / 2.0F);
            GlStateManager.translate(0, -0.3333F, 0);
            modelchest.renderAll();
            GlStateManager.scale(1 / 3, 1 / 3, 1 / 3);
        }
    }
}
