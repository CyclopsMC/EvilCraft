package evilcraft.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import evilcraft.entities.tileentities.ExtendedTileEntity;

public class Helpers {
    public static final int MINECRAFT_DAY = 24000;
    public static List<ForgeDirection> DIRECTIONS = new LinkedList<ForgeDirection>();
    static {
        DIRECTIONS.add(ForgeDirection.DOWN);
        DIRECTIONS.add(ForgeDirection.UP);
        DIRECTIONS.add(ForgeDirection.SOUTH);
        DIRECTIONS.add(ForgeDirection.NORTH);
        DIRECTIONS.add(ForgeDirection.WEST);
        DIRECTIONS.add(ForgeDirection.EAST);
    }
    private static Map<ForgeDirection, String> METHODS_RENDERFACE = new HashMap<ForgeDirection, String>();
    static {
        METHODS_RENDERFACE.put(ForgeDirection.DOWN, "renderFaceYNeg");
        METHODS_RENDERFACE.put(ForgeDirection.UP, "renderFaceYPos");
        METHODS_RENDERFACE.put(ForgeDirection.SOUTH, "renderFaceZNeg");
        METHODS_RENDERFACE.put(ForgeDirection.NORTH, "renderFaceZPos");
        METHODS_RENDERFACE.put(ForgeDirection.WEST, "renderFaceXNeg");
        METHODS_RENDERFACE.put(ForgeDirection.EAST, "renderFaceXPos");
    }
    
    /**
     * Check if it's day in this world
     * @param world
     * @return
     */
    public static boolean isDay(World world) {
        return world.getWorldTime() % MINECRAFT_DAY < MINECRAFT_DAY/2;
    }
    
    /**
     * Set the world time to day or night.
     * @param world the world to manipulate time in.
     * @param toDay if true, set to day, otherwise to night.
     */
    public static void setDay(World world, boolean toDay) {
        int currentTime = (int) world.getWorldTime();
        int newTime = currentTime - (currentTime % (MINECRAFT_DAY / 2)) + MINECRAFT_DAY / 2;
        world.setWorldTime(newTime);
    }
    
    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * @param world
     * @param entityID
     * @param x
     * @param y
     * @param z
     * @return the entity that was spawned
     */
    public static Entity spawnCreature(World world, int entityID, double x, double y, double z)
    {
        if (!EntityList.entityEggs.containsKey(Integer.valueOf(entityID))) {
            return null;
        } else {
            Entity entity = EntityList.createEntityByID(entityID, world);

            if (entity != null && entity.isEntityAlive()) {
                EntityLiving entityliving = (EntityLiving)entity;
                entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                entityliving.onSpawnWithEgg((EntityLivingData)null);
                world.spawnEntityInWorld(entity);
                entityliving.playLivingSound();
            }

            return entity;
        }
    }
    
    /**
     * Convert r, g and b colors to an integer representation.
     * @param r red
     * @param g green
     * @param b blue
     * @return integer representation of the color.
     */
    public static int RGBToInt(int r, int g, int b) {
        return (int)r << 16 | (int)g << 8 | (int)b;
    }
    
    /**
     * This method should be called when a BlockContainer is destroyed
     * @param world world
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public static void preDestroyBlock(World world, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof IInventory && !world.isRemote) {
            //if (!(tile instanceof IDropControlInventory) || ((IDropControlInventory) tile).doDrop()) {
                dropItems(world, (IInventory) tile, x, y, z);
                clearInventory((IInventory) tile);
            //}
        }

        if (tile instanceof ExtendedTileEntity) {
            ((ExtendedTileEntity) tile).destroy();
        }
    }
    
    /**
     * Drop an ItemStack into the world
     * @param world the world
     * @param stack ItemStack to drop
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public static void dropItems(World world, ItemStack stack, int x, int y, int z) {
        if (stack.stackSize > 0) {
            float offsetMultiply = 0.7F;
            double offsetX = (world.rand.nextFloat() * offsetMultiply) + (1.0F - offsetMultiply) * 0.5D;
            double offsetY = (world.rand.nextFloat() * offsetMultiply) + (1.0F - offsetMultiply) * 0.5D;
            double offsetZ = (world.rand.nextFloat() * offsetMultiply) + (1.0F - offsetMultiply) * 0.5D;
            EntityItem entityitem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ, stack);
            entityitem.delayBeforeCanPickup = 10;
    
            world.spawnEntityInWorld(entityitem);
        }
    }

    /**
     * Drop an ItemStack into the world
     * @param world the world
     * @param inventory inventory with ItemStacks
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public static void dropItems(World world, IInventory inventory, int x, int y, int z) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.stackSize > 0)
                dropItems(world, inventory.getStackInSlot(i).copy(), x, y, z);
        }
    }
    
    /**
     * Erase a complete inventory
     * @param inventory inventory to clear
     */
    public static void clearInventory(IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            inventory.setInventorySlotContents(i, null);
        }
    }
    
    /**
     * Checks if an itemStack has a certain enchantment.
     * @param itemStack The itemStack to check.
     * @param enchantID The Enchantment to compare.
     * @return The id of the enchantment in the enchantmentlist or -1 if it does not appy.
     */
    public static int doesEnchantApply(ItemStack itemStack, int enchantID) {
        if(itemStack != null) {
            NBTTagList enchantmentList = itemStack.getEnchantmentTagList();
            if(enchantmentList != null) {
                for(int i = 0; i < enchantmentList.tagCount(); i++) {
                    if (((NBTTagCompound)enchantmentList.tagAt(i)).getShort("id") == enchantID) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * Iterate over the coordinates for the sides of the given block coordinates.
     * @param xc x coordinate of the center block.
     * @param yc y coordinate of the center block.
     * @param zc z coordinate of the center block.
     * @return
     */
    public static Iterator<Coordinate> getSideIterator(int xc, int yc, int zc) {
        List<Coordinate> coordinates = new LinkedList<Coordinate>();
        for(int x = xc - 1; x <= xc + 1; x++) {
            for(int y = yc - 1; y <= yc + 1; y++) {
                for(int z = zc - 1; z <= zc + 1; z++) {
                    // Only allow side that are directly next to the center block (no diagonal blocks)
                    if(Math.abs(x - xc) + Math.abs(y - yc) + Math.abs(z - zc) == 1) {
                        coordinates.add(new Coordinate(x, y, z));
                    }
                }
            }
        }
        return coordinates.iterator();
    }
    
    public static Iterator<ForgeDirection> getDirectionIterator() {
        return DIRECTIONS.iterator();
    }

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
    
}
