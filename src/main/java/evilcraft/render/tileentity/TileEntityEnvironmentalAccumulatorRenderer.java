package evilcraft.render.tileentity;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.core.recipes.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipes.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.core.recipes.IRecipe;
import evilcraft.entities.tileentities.EvilCraftBeaconTileEntity;
import evilcraft.entities.tileentities.TileEnvironmentalAccumulator;

/**
 * Renderer for the {@link EnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class TileEntityEnvironmentalAccumulatorRenderer extends TileEntityBeaconRenderer {
    
    // Speed at which the item should spin in the animation
    private static final int ITEM_SPIN_SPEED = 3;
    
    @Override
    public void renderBeacon(EvilCraftBeaconTileEntity tileentity, double x, double y, double z, float partialTickTime) {
        super.renderBeacon(tileentity, x, y, z, partialTickTime);
        
        TileEnvironmentalAccumulator tile = (TileEnvironmentalAccumulator)tileentity;
        if(tile.getHealth() != tile.getMaxHealth())
            BossStatus.setBossStatus(tile, false);
        
        // Render the an item moving up if we're currently processing one
        if (tile.getMovingItemY() != -1.0f) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x - 0.5f, (float)y - 0.5f + tile.getMovingItemY(), (float)z - 0.5f);
            
            renderProcessingItem(tile.getRecipe(), tile.getWorld(), partialTickTime);
            
            GL11.glPopMatrix();
        }
    }
    
    private void renderProcessingItem(
            IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe,
            World world, float partialTickTime) {

        if (recipe == null)
            return;
        
        ItemStack stack = recipe.getInput().getItemStack();
        if (stack == null)
            return;
        
        // Calculate angle for the spinning item
        double totalTickTime = world.getTotalWorldTime() + partialTickTime;
        double angle = ITEM_SPIN_SPEED * (totalTickTime % 360);
        
        // Draw the actual item at the origin
        if (stack.getItem() instanceof ItemBlock) {
            GL11.glTranslatef(1F, 0.675F, 1F);
            GL11.glRotated(angle, 0, 1, 0);
        } else {
            GL11.glTranslatef(1F, 1F, 1F);
            GL11.glRotated(angle, 0, 1, 0);
        }
        
        RenderItem.renderInFrame = true;
        EntityItem entity = new EntityItem(world, 0, 0, 0, stack);
        entity.hoverStart = 0.0F;
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        RenderItem.renderInFrame = false;
    }
}
