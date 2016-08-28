package org.cyclops.evilcraft.client.render.tileentity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.api.broom.IBroom;
import org.cyclops.evilcraft.block.DisplayStand;
import org.cyclops.evilcraft.tileentity.TileDisplayStand;
import org.lwjgl.util.vector.Vector3f;

import java.util.Map;

/**
 * Renderer for the item inside the {@link org.cyclops.evilcraft.block.DisplayStand}.
 * 
 * @author rubensworks
 *
 */
public class RenderTileEntityDisplayStand extends TileEntitySpecialRenderer<TileDisplayStand> {

    private static final Map<EnumFacing, Vector3f> ROTATIONS = ImmutableMap.<EnumFacing, Vector3f>builder()
            .put(EnumFacing.NORTH, new Vector3f(270, 0, 0))
            .put(EnumFacing.SOUTH, new Vector3f(90, 0, 0))
            .put(EnumFacing.WEST, new Vector3f(0, 90, 0))
            .put(EnumFacing.EAST, new Vector3f(0, 90, 0))
            .put(EnumFacing.UP, new Vector3f(180, 180, 0))
            .put(EnumFacing.DOWN, new Vector3f(0, 0, 0))
            .build();

	@Override
	public void renderTileEntityAt(TileDisplayStand tile, double x, double y, double z, float partialTickTime, int partialDamage) {
	    GlStateManager.pushMatrix();
	    float var10 = (float) (x - 0.5F);
        float var11 = (float) (y - 0.5F);
        float var12 = (float) (z - 0.5F);
        GlStateManager.translate(var10, var11, var12);
	    
        if(tile != null) {
            if(tile.getStackInSlot(0) != null) {
                IBlockState blockState = tile.getWorld().getBlockState(tile.getPos());
                renderItem(tile.getWorld(), tile.getStackInSlot(0),
                        blockState .getValue(DisplayStand.FACING),
                        blockState .getValue(DisplayStand.AXIS_X),
                        tile.getDirection() == EnumFacing.AxisDirection.POSITIVE);
            }
        }
        
        GlStateManager.popMatrix();
	}
	
	private void renderItem(World world, ItemStack itemStack, EnumFacing facing, boolean axisX, boolean positiveDirection) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(1F, 1F, 1F);
        if (itemStack.getItem() instanceof ItemBlock) {
            GlStateManager.scale(0.6F, 0.6F, 0.6F);
            GlStateManager.rotate(90F, 0, 1, 0);
        } else if(!(itemStack.getItem() instanceof IBroom)) {
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0F, 0.25F, 0F);
            GlStateManager.rotate(90F, 0, 1, 0);
        }

        Vector3f vec = ROTATIONS.get(facing);
        GlStateManager.rotate(vec.getX(), 1, 0, 0);
        GlStateManager.rotate(vec.getY(), 0, 1, 0);

        if (!axisX) {
            GlStateManager.rotate(90F, 0, 1, 0);
            if (!positiveDirection) {
                GlStateManager.rotate(180F, 0, 1, 0);
            }
        } else {
            if (positiveDirection) {
                GlStateManager.rotate(180F, 0, 1, 0);
            }
        }
        
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        RenderHelpers.renderItem(itemStack);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

}
