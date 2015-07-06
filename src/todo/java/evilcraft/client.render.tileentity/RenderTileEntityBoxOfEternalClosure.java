package evilcraft.client.render.tileentity;

import evilcraft.Reference;
import evilcraft.block.BoxOfEternalClosure;
import evilcraft.entity.monster.VengeanceSpirit;
import evilcraft.tileentity.TileBoxOfEternalClosure;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.client.render.tileentity.RenderTileEntityModel;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class RenderTileEntityBoxOfEternalClosure extends RenderTileEntityModel<TileBoxOfEternalClosure, ModelBase> {

	private static final ResourceLocation beamTexture =
			new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "beam.png");
	
	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderTileEntityBoxOfEternalClosure(ModelBase model, ResourceLocation texture) {
        super(model, texture);
    }
    
    @Override
    protected void renderTileEntityAt(TileBoxOfEternalClosure tile, double x, double y, double z, float partialTick, int partialDamage) {
    	super.renderTileEntityAt(tile, x, y, z, partialTick, partialDamage);
    	
    	// Make sure the beam originates from the center of the box.
    	x += 0.5D;
    	y -= 0.5D;
    	z += 0.5D;
    	
    	// Optionally render beam
    	VengeanceSpirit target = box.getTargetSpirit();
    	if(target != null) {
            // The 'bouncy' effect
    		float innerRotation = (float)box.innerRotation + partialTick;
            float yOffset = MathHelper.sin(innerRotation * 0.2F) / 4.0F + 0.5F;
            yOffset = (yOffset * yOffset + yOffset) * 0.2F;
            
            // Calculate the coordinates of the end of the beam
            float rotateX = -(target.width / 2) -(float)(box.getPos().getX() - target.posX - (target.prevPosX - target.posX) * (double)(1.0F - partialTick));
            float rotateY = (target.height / 2) - (float)((double)yOffset + box.getPos().getY() - target.posY - (target.prevPosY - target.posY) * (double)(1.0F - partialTick));
            float rotateZ = -(target.width / 2) -(float)(box.getPos().getZ() - target.posZ - (target.prevPosZ - target.posZ) * (double)(1.0F - partialTick));
            float distance = MathHelper.sqrt_float(rotateX * rotateX + rotateZ * rotateZ);
            
            // Set the scene coordinates right for the beam rendering
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z);
            GL11.glRotatef((float)(-Math.atan2((double)rotateZ, (double)rotateX)) * 180.0F / (float)Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef((float)(-Math.atan2((double)distance, (double)rotateY)) * 180.0F / (float)Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
            
            // Start tesselator
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_CULL_FACE);
            this.bindTexture(beamTexture);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            worldRenderer.startDrawing(GL11.GL_TRIANGLE_STRIP);
            
            // Calculate UV coordinates for the beam
            float zuv = MathHelper.sqrt_float(rotateX * rotateX + rotateY * rotateY + rotateZ * rotateZ);
            float v1 = MathHelper.sqrt_float(rotateX * rotateX + rotateY * rotateY
            		+ rotateZ * rotateZ) / 32.0F
            		- ((float)target.ticksExisted + partialTick) * 0.01F;
            float v2 = 0.0F - ((float)target.ticksExisted + partialTick) * 0.01F;
            
            // Draw the beam in a circle shape
            int amount = 8;
            for (int i = 0; i <= amount; ++i) {
                float xuv = MathHelper.sin((float)(i % amount) * (float)Math.PI * 2.0F / (float)amount) * 0.75F;
                float yuv = MathHelper.cos((float)(i % amount) * (float)Math.PI * 2.0F / (float)amount) * 0.75F;
                float u = (float)(i % amount) * 1.0F / (float)amount;
                worldRenderer.func_178991_c(0); // mcp: setColorOpaque
                worldRenderer.addVertexWithUV((double)(xuv * 0.2F), (double)(yuv * 0.2F), 0.0D, (double)u, (double)v1);
                worldRenderer.func_178991_c(16777215); // mcp: setColorOpaque
                worldRenderer.addVertexWithUV((double)xuv, (double)yuv, (double)zuv, (double)u, (double)v2);
            }

            // Finish drawing
            tessellator.draw();
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glShadeModel(GL11.GL_FLAT);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
        }
    }

    @Override
    protected void renderModel(TileBoxOfEternalClosure tile, ModelBase model, float partialTick) {
    	// Render box
    	ModelBoxOfEternalClosure boxModel = (ModelBoxOfEternalClosure)model;
    	float angle = box.getPreviousLidAngle()
    			+ (box.getLidAngle() - box.getPreviousLidAngle()) * partialTick;
    	boxModel.setCoverAngle(angle);
    	((ModelBoxOfEternalClosure)model).renderAll();
    }
    
}
