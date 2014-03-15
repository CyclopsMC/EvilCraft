package evilcraft.render.tileentity;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.boss.BossStatus;
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
    
    // Speed at which the item should spin in the animation
    private static final int ITEM_SPIN_SPEED = 3;
    
    // Tickness of the item that is spinning around
    private static final float ITEM_TICKNESS = 0.05f;
    
    private ResourceLocation getResourceLocation() {
        if(weatherContainerTexture == null)
            weatherContainerTexture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ITEMS + WeatherContainerConfig._instance.NAMEDID + ".png");
        return weatherContainerTexture;
    }
    
    @Override
    public void renderBeacon(EvilCraftBeaconTileEntity tileentity, double x, double y, double z, float partialTickTime) {
        super.renderBeacon(tileentity, x, y, z, partialTickTime);
        
        TileEnvironmentalAccumulator tile = (TileEnvironmentalAccumulator)tileentity;
        BossStatus.setBossStatus(tile, false);
        
        // Render the weather container moving up if the player just threw one in
        if (tile.getMovingItemY() != -1.0f)
            renderMovingWeatherContainer(tile, x, y + 1 + tile.getMovingItemY(), z, partialTickTime);
    }
    
    private void renderMovingWeatherContainer(TileEnvironmentalAccumulator tileentity, double x, double y, double z, float partialTickTime) {
        GL11.glPushMatrix();
        
        bindTexture(getResourceLocation());
        GL11.glDisable(GL11.GL_LIGHTING);
        
        // Calculate angle for the spinning item
        double totalTickTime = tileentity.getWorldObj().getTotalWorldTime() + partialTickTime;
        double angle = ITEM_SPIN_SPEED * (totalTickTime % 360);
        
        // Translate to the point we will rotate around
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        // Rotate
        GL11.glRotated(angle, 0, 1, 0);
        // Render the item with tickness and center the rendering around the rotation point
        GL11.glTranslated(-0.5, 0.0, ITEM_TICKNESS/2.0);
        ItemRenderer.renderItemIn2D(Tessellator.instance, 0, 0, 1, 1, 16, 16, ITEM_TICKNESS);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
