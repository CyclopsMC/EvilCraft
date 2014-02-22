package evilcraft.api;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
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
    private static Map<ForgeDirection, String> METHODS_RENDERFACE_OBFUSICATED = new HashMap<ForgeDirection, String>();
    static {
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.DOWN, "func_147768_a");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.UP, "func_147806_b");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.NORTH, "func_147734_d");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.EAST, "func_147764_f");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.SOUTH, "func_147761_c");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.WEST, "func_147798_e");
    }
    private static Map<ForgeDirection, String> FIELDS_UVROTATE_OBFUSICATED = new HashMap<ForgeDirection, String>();
    static { // Note: the fields from the RenderBlock are INCORRECT! Very good read: http://greyminecraftcoder.blogspot.be/2013/07/rendering-non-standard-blocks.html
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.DOWN, "field_147865_v");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.UP, "field_147867_u");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.NORTH, "field_147875_q");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.EAST, "field_147871_s");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.SOUTH, "field_147873_r");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.WEST, "field_147869_t");
    }
    private static int[] ROTATE_UV_ROTATE = {0, 1, 3, 2}; // N, E, S, W -> N, E, W, S
    
    /**
     * An icon that contains to texture, useful for when you want to render nothing.
     */
    public static IIcon EMPTYICON;

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
            IIcon blockIconFromSideAndMetadata) {
        try {
            String methodName = Helpers.isObfusicated()?METHODS_RENDERFACE_OBFUSICATED.get(renderDirection):METHODS_RENDERFACE.get(renderDirection);
            Method method = renderer.getClass().getMethod(methodName, Block.class, double.class, double.class, double.class, IIcon.class);
            method.invoke(renderer, block, x, y, z, blockIconFromSideAndMetadata);
        } catch (NoSuchMethodException e1) {
            // Impossible to go wrong, unless this code changes or those of Minecraft...
            e1.printStackTrace();
        } catch (SecurityException e2) {
        	e2.printStackTrace();
        } catch (IllegalAccessException e3) {
        	e3.printStackTrace();
        } catch (IllegalArgumentException e4) {
        	e4.printStackTrace();
        } catch (InvocationTargetException e5) {
        	e5.printStackTrace();
        }
    }
    
    /**
     * Set the correct rotation of the given renderer given a {@link ForgeDirection}.
     * It will use reflection to set the correct field in the {@link RenderBlocks}.
     * @param renderer The renderer to set the rotation at.
     * @param side The {@link ForgeDirection} to set a rotation for.
     * @param rotation The rotation to set.
     * @see RenderBlocks
     */
    public static void setRenderBlocksUVRotation(RenderBlocks renderer, ForgeDirection side, int rotation) {
        try {
            String fieldName = Helpers.isObfusicated()?FIELDS_UVROTATE_OBFUSICATED.get(side):FIELDS_UVROTATE.get(side);
            Field field = renderer.getClass().getField(fieldName);
            field.set(renderer, ROTATE_UV_ROTATE[rotation]);
        } catch (NoSuchFieldException e1) {
            // Impossible to go wrong, unless this code changes or those of Minecraft...
            e1.printStackTrace();
        } catch (SecurityException e2) {
        	e2.printStackTrace();
        } catch (IllegalAccessException e3) {
        	e3.printStackTrace();
        } catch (IllegalArgumentException e4) {
        	e4.printStackTrace();
        }
    }
}
