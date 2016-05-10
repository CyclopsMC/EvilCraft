package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.tileentity.EvilCraftBeaconTileEntity;
import org.cyclops.evilcraft.tileentity.TileEnvironmentalAccumulator;


/**
 * Renderer for the {@link EnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class RenderTileEntityEnvironmentalAccumulator extends RenderTileEntityBeacon {
    
    // Speed at which the item should spin in the animation
    private static final int ITEM_SPIN_SPEED = 3;
    
    @Override
    public void renderBeacon(EvilCraftBeaconTileEntity tileentity, double x, double y, double z, float partialTickTime, int partialDamage) {
        TileEnvironmentalAccumulator tile = (TileEnvironmentalAccumulator)tileentity;
        if(tile.getHealth() != tile.getMaxHealth())
            //tile.getBossInfo().addPlayer(Minecraft.getMinecraft().thePlayer); // TODO
        
        // Render the an item moving up if we're currently processing one
        if (tile.getMovingItemY() != -1.0f) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x - 0.5f, (float) y - 0.5f + tile.getMovingItemY(), (float) z - 0.5f);
            
            renderProcessingItem(tile.getRecipe(), tile.getDegradationWorld(), partialTickTime);
            
            GlStateManager.popMatrix();
        }

        super.renderBeacon(tileentity, x, y, z, partialTickTime, partialDamage);
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
        float angle = (float) (ITEM_SPIN_SPEED * (totalTickTime % 360));
        
        // Draw the actual item at the origin
        if (stack.getItem() instanceof ItemBlock) {
            GlStateManager.translate(1F, 0.675F, 1F);
            GlStateManager.rotate(angle, 0, 1, 0);

        } else {
            GlStateManager.translate(1F, 1F, 1F);
            GlStateManager.rotate(angle, 0, 1, 0);
        }
        GlStateManager.scale(0.5, 0.5, 0.5);

        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        RenderHelpers.renderItem(stack);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.enableLighting();
    }
}
