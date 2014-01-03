package evilcraft.api;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

public class RenderHelpers {
    
    private static Map<ForgeDirection, String> METHODS_RENDERFACE = new HashMap<ForgeDirection, String>();
    static {
        METHODS_RENDERFACE.put(ForgeDirection.DOWN, "renderFaceYNeg");
        METHODS_RENDERFACE.put(ForgeDirection.UP, "renderFaceYPos");
        METHODS_RENDERFACE.put(ForgeDirection.NORTH, "renderFaceZPos");
        METHODS_RENDERFACE.put(ForgeDirection.EAST, "renderFaceXPos");
        METHODS_RENDERFACE.put(ForgeDirection.SOUTH, "renderFaceZNeg");
        METHODS_RENDERFACE.put(ForgeDirection.WEST, "renderFaceXNeg");
    }
    private static Map<ForgeDirection, String> FIELDS_UVROTATE = new HashMap<ForgeDirection, String>();
    static { // Note: the fields from the RenderBlock are INCORRECT! Very good read: http://greyminecraftcoder.blogspot.be/2013/07/rendering-non-standard-blocks.html
        FIELDS_UVROTATE.put(ForgeDirection.DOWN, "uvRotateBottom");
        FIELDS_UVROTATE.put(ForgeDirection.UP, "uvRotateTop");
        FIELDS_UVROTATE.put(ForgeDirection.NORTH, "uvRotateEast");
        FIELDS_UVROTATE.put(ForgeDirection.EAST, "uvRotateSouth");
        FIELDS_UVROTATE.put(ForgeDirection.SOUTH, "uvRotateWest");
        FIELDS_UVROTATE.put(ForgeDirection.WEST, "uvRotateNorth");
    }
    private static int[] ROTATE_UV_ROTATE = {0, 1, 3, 2}; // N, E, S, W -> N, E, W, S
    
    public static Icon EMPTYICON;

    /**
     * Call the correct face renderer on the renderer depending on the given renderDirection.
     * @param renderDirection direction to call the renderer method for.
     * @param renderer Renderer to call the face renderer on.
     * @param block To be passed to renderer.
     * @param x To be passed to renderer.
     * @param y To be passed to renderer.
     * @param z To be passed to renderer.
     * @param blockIconFromSideAndMetadata To be passed to renderer.
     */
    public static void renderFaceDirection(ForgeDirection renderDirection,
            RenderBlocks renderer, Block block, double x, double y, double z,
            Icon blockIconFromSideAndMetadata) {
        try {
            Method method = renderer.getClass().getMethod(METHODS_RENDERFACE.get(renderDirection), Block.class, double.class, double.class, double.class, Icon.class);
            method.invoke(renderer, block, x, y, z, blockIconFromSideAndMetadata);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
            // Impossible to go wrong, unless this code changes or those of Minecraft...
            e1.printStackTrace();
        }
    }
    
    public static void setRenderBlocksUVRotation(RenderBlocks renderer, ForgeDirection side, int rotation) {
        try {
            Field field = renderer.getClass().getField(FIELDS_UVROTATE.get(side));
            field.set(renderer, ROTATE_UV_ROTATE[rotation]);
            /*if((side == ForgeDirection.EAST || side == ForgeDirection.NORTH)
                    && (rotation == 1 || rotation == 2)) {
                renderer.flipTexture = true;
            }
            if(side == ForgeDirection.DOWN) {
                renderer.flipTexture = true;
            }
            renderer.flipTexture = true;*/
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException e1) {
            // Impossible to go wrong, unless this code changes or those of Minecraft...
            e1.printStackTrace();
        }
    }
}
