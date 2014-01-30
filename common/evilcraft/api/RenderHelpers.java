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
    private static Map<ForgeDirection, String> METHODS_RENDERFACE_OBFUSICATED = new HashMap<ForgeDirection, String>();
    static {
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.DOWN, "func_78613_a");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.UP, "func_78617_b");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.NORTH, "func_78622_d");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.EAST, "func_78605_f");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.SOUTH, "func_78611_c");
        METHODS_RENDERFACE_OBFUSICATED.put(ForgeDirection.WEST, "func_78573_e");
    }
    private static Map<ForgeDirection, String> FIELDS_UVROTATE_OBFUSICATED = new HashMap<ForgeDirection, String>();
    static { // Note: the fields from the RenderBlock are INCORRECT! Very good read: http://greyminecraftcoder.blogspot.be/2013/07/rendering-non-standard-blocks.html
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.DOWN, "field_78675_l");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.UP, "field_78681_k");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.NORTH, "field_78662_g");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.EAST, "field_78685_i");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.SOUTH, "field_78683_h");
        FIELDS_UVROTATE_OBFUSICATED.put(ForgeDirection.WEST, "field_78679_j");
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
            String methodName = Helpers.isObfusicated()?METHODS_RENDERFACE_OBFUSICATED.get(renderDirection):METHODS_RENDERFACE.get(renderDirection);
            Method method = renderer.getClass().getMethod(methodName, Block.class, double.class, double.class, double.class, Icon.class);
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
