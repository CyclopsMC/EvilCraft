package evilcraft.core.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import evilcraft.core.entities.tileentitites.EvilCraftTileEntity;

/**
 * General renderer for {@link EvilCraftTileEntity} with models.
 * @author rubensworks
 *
 */
public class TileEntityModelRenderer extends TileEntitySpecialRenderer {

    private ModelBase model;

	private ResourceLocation texture;

    /**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public TileEntityModelRenderer(ModelBase model, ResourceLocation texture) {
        this.model = model;
        this.texture = texture;
    }
    
    /**
     * Get the model.
     * @return The model.
     */
    public ModelBase getModel() {
		return model;
	}

    /**
     * Get the texture.
     * @return The texture.
     */
	public ResourceLocation getTexture() {
		return texture;
	}
    
    protected void renderTileEntityAt(EvilCraftTileEntity tile, double x, double y, double z, float partialTick) {
        ForgeDirection direction = tile.getRotation();
        this.bindTexture(getTexture());

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

        renderModel(tile, getModel(), partialTick);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
	
	@Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
        this.renderTileEntityAt((EvilCraftTileEntity)tile, x, y, z, partialTick);
    }
	
	/**
     * Render the actual model, override this to change the way the model should be rendered.
     */
    protected void renderModel(EvilCraftTileEntity tile, ModelBase model, float partialTick) {
    	model.render(null, 0, 0, 0, 0, 0, 0);
    }
}
