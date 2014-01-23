package evilcraft.render;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import evilcraft.Reference;
import evilcraft.entities.tileentities.TileBloodChest;

public class RenderBloodChest extends TileEntitySpecialRenderer {
    
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "bloodChest.png");

    private ModelChest chestModel = new ModelChest();

    public void renderTileEntityChestAt(TileBloodChest tile, double x, double y, double z, float partialTick) {
            ForgeDirection direction = tile.getRotation();
            ModelChest modelchest = this.chestModel;
            this.bindTexture(TEXTURE);

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short rotation = 0;

            if (direction == ForgeDirection.SOUTH) {
                rotation = 180;
            }
            if (direction == ForgeDirection.NORTH) {
                rotation = 0;
            }
            if (direction == ForgeDirection.EAST) {
                rotation = 90;
            }
            if (direction == ForgeDirection.WEST) {
                rotation = -90;
            }

            GL11.glRotatef((float)rotation, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float lidangle = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTick;

            lidangle = 1.0F - lidangle;
            lidangle = 1.0F - lidangle * lidangle * lidangle;
            modelchest.chestLid.rotateAngleX = -(lidangle * (float)Math.PI / 2.0F);
            modelchest.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
        this.renderTileEntityChestAt((TileBloodChest)tile, x, y, z, partialTick);
    }
}
