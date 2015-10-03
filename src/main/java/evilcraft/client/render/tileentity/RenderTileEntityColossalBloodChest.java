package evilcraft.client.render.tileentity;

import evilcraft.core.client.render.tileentity.RenderTileEntityModel;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import evilcraft.tileentity.TileColossalBloodChest;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for the {@link evilcraft.block.ColossalBloodChest}.
 * @author rubensworks
 *
 */
public class RenderTileEntityColossalBloodChest extends RenderTileEntityModel {

	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderTileEntityColossalBloodChest(ModelBase model, ResourceLocation texture) {
        super(model, texture);
    }

    @Override
    protected void preRotate(EvilCraftTileEntity tile) {
        GL11.glTranslatef(0.5F, 0, -0.5F);
    }

    @Override
    protected void postRotate(EvilCraftTileEntity tile) {
        GL11.glTranslatef(-0.5F, 0, 0.5F);
    }

    @Override
    protected void renderModel(EvilCraftTileEntity tile, ModelBase model, float partialTick) {
        TileColossalBloodChest chestTile = (TileColossalBloodChest) tile;
        if(chestTile.canWork()) {
            ModelChest modelchest = (ModelChest) model;
            float lidangle = chestTile.prevLidAngle + (chestTile.lidAngle - chestTile.prevLidAngle) * partialTick;

            lidangle = 1.0F - lidangle;
            lidangle = 1.0F - lidangle * lidangle * lidangle;
            modelchest.chestLid.rotateAngleX = -(lidangle * (float) Math.PI / 2.0F);
            int[] renderOffset = chestTile.getRenderOffset().getCoordinates();
            GL11.glTranslatef(renderOffset[0] - 1, renderOffset[1] - 1, renderOffset[2] - 1);
            GL11.glScalef(3, 3, 3);
            modelchest.renderAll();
            GL11.glScalef(1 / 3, 1 / 3, 1 / 3);
        }
    }
}
