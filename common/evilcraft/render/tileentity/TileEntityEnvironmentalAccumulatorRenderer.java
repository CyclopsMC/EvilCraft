package evilcraft.render.tileentity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import evilcraft.Reference;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.entities.tileentities.EvilCraftBeaconTileEntity;
import evilcraft.entities.tileentities.TileEnvironmentalAccumulator;
import evilcraft.items.WeatherContainerConfig;

/**
 * Renderer for the {@link EnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class TileEntityEnvironmentalAccumulatorRenderer extends TileEntityBeaconRenderer {

    private ResourceLocation weatherContainerTexture;
    
    private static final int ITEM_SPIN_SPEED = 3;
    
    private ResourceLocation getResourceLocation() {
        if(weatherContainerTexture == null)
            weatherContainerTexture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ITEMS + WeatherContainerConfig._instance.NAMEDID + ".png");
        return weatherContainerTexture;
    }
    
    @Override
    public void renderBeacon(EvilCraftBeaconTileEntity tileentity, double x, double y, double z, float partialTickTime) {
        super.renderBeacon(tileentity, x, y, z, partialTickTime);
        
        TileEnvironmentalAccumulator tile = (TileEnvironmentalAccumulator)tileentity;
        
        // Render the weather container moving up if the player just threw one in
        if (tile.getMovingItemY() != -1.0f)
            renderMovingWeatherContainer(tile, x, y + 1 + tile.getMovingItemY(), z, partialTickTime);
    }
    
    private void renderMovingWeatherContainer(TileEnvironmentalAccumulator tileentity, double x, double y, double z, float partialTickTime) {
        GL11.glPushMatrix();
        
        bindTexture(getResourceLocation());
        GL11.glDisable(GL11.GL_LIGHTING);
        
        float f2 = (float)tileentity.getWorldObj().getTotalWorldTime() + partialTickTime;
        float f3 = ITEM_SPIN_SPEED * (f2 % 180);
        
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        GL11.glRotatef(f3, 0, 1, 0);
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        
        tessellator.addVertexWithUV(-0.25, 0, 0, 1, 1);
        tessellator.addVertexWithUV(-0.25, 0.5, 0, 1, 0);
        tessellator.addVertexWithUV(0.25, 0.5, 0, 0, 0);
        tessellator.addVertexWithUV(0.25, 0, 0, 0, 1);
        
        /*
        // This code contains some experimentation with rendering different
        // things depending on the direction the player is looking in
        // it might be usefull for fancier effects
        RenderManager renderManager = RenderManager.instance;
        float yaw = renderManager.playerViewY % 360;
        
        if (yaw >= 270.0f || yaw <= 45.0f) {
            tessellator.addVertexWithUV(x + 0.25, y, z + 0.25, 1, 1);
            tessellator.addVertexWithUV(x + 0.25, y + 0.5, z + 0.25, 1, 0);
            tessellator.addVertexWithUV(x + 0.75, y + 0.5, z + 0.25, 0, 0);
            tessellator.addVertexWithUV(x + 0.75, y, z + 0.25, 0, 1);
        } else if (yaw > 45.0f && yaw <= 135.0f) {
            tessellator.addVertexWithUV(x + 0.75, y, z + 0.25, 1, 1);
            tessellator.addVertexWithUV(x + 0.75, y + 0.5, z + 0.25, 1, 0);
            tessellator.addVertexWithUV(x + 0.75, y + 0.5, z + 0.75, 0, 0);
            tessellator.addVertexWithUV(x + 0.75, y, z + 0.75, 0, 1);
        } else if (yaw > 135.0f && yaw <= 225.0f) {
            tessellator.addVertexWithUV(x + 0.75, y, z + 0.75, 1, 1);
            tessellator.addVertexWithUV(x + 0.75, y + 0.5, z + 0.75, 1, 0);
            tessellator.addVertexWithUV(x + 0.25, y + 0.5, z + 0.75, 0, 0);
            tessellator.addVertexWithUV(x + 0.25, y, z + 0.75, 0, 1);
        } else {
            tessellator.addVertexWithUV(x + 0.25, y, z + 0.75, 1, 1);
            tessellator.addVertexWithUV(x + 0.25, y + 0.5, z + 0.75, 1, 0);
            tessellator.addVertexWithUV(x + 0.25, y + 0.5, z + 0.25, 0, 0);
            tessellator.addVertexWithUV(x + 0.25, y, z + 0.25, 0, 1);
        }*/
        
        tessellator.draw();
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
