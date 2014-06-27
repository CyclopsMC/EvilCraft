package evilcraft.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;
import evilcraft.api.item.TileEntityNBTStorage;

/**
 * A collection of helper methods and fields.
 * @author rubensworks
 *
 */
public class Helpers {
    
    private static final Random random = new Random();
    
    /**
     * The length of one Minecraft day.
     */
    public static final int MINECRAFT_DAY = 24000;
    /**
     * The amount of steps there are in a comparator.
     */
    public static final int COMPARATOR_MULTIPLIER = 15;
    
    /**
     * A list of all the {@link ForgeDirection}.
     */
    public static List<ForgeDirection> DIRECTIONS = Arrays.asList(ForgeDirection.VALID_DIRECTIONS);
    /**
     * A list of all the {@link DirectionCorner}
     */
    public static List<DirectionCorner> DIRECTIONS_CORNERS = Arrays.asList(DirectionCorner.VALID_DIRECTIONS);
    /**
     * The facing directions of an entity, used in {@link Helpers#getEntityFacingDirection(EntityLivingBase)}.
     */
    public static final ForgeDirection[] ENTITYFACING =
        {ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST};
    /**
     * A double array that contains the visual side. The first argument should be the rotation of
     * the block and the second argument is the side for which the texture is called.
     */
    public static ForgeDirection[][] TEXTURESIDE_ORIENTATION = {
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST}, // DOWN
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST}, // UP
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST}, // NORTH
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.WEST}, // SOUTH
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH}, // WEST
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.NORTH}, // EAST
    };
    /**
     * The types of NBT Tags, used for the second parameter in
     * {@link NBTTagCompound#getTagList(String, int)}.
     * @author rubensworks
     *
     */
    @SuppressWarnings("javadoc")
    public enum NBTTag_Types {
		NBTTagEnd, NBTTagByte, NBTTagShort, 
		NBTTagInt, NBTTagLong, NBTTagFloat, 
		NBTTagDouble, NBTTagByteArray, NBTTagString,
		NBTTagList, NBTTagCompound, NBTTagIntArray
	}
    
    private static boolean ISOBFUSICATED_CHECKED = false;
    private static boolean ISOBFUSICATED;
    
    private static Map<IDType, Integer> ID_COUNTER = new HashMap<IDType, Integer>();
    
    /**
     * A list of all the {@link ChestGenHooks}.
     * @see ChestGenHooks
     */
    public static List<String> CHESTGENCATEGORIES = new LinkedList<String>();
    static {
        CHESTGENCATEGORIES.add(ChestGenHooks.BONUS_CHEST);
        CHESTGENCATEGORIES.add(ChestGenHooks.DUNGEON_CHEST);
        CHESTGENCATEGORIES.add(ChestGenHooks.MINESHAFT_CORRIDOR);
        CHESTGENCATEGORIES.add(ChestGenHooks.PYRAMID_DESERT_CHEST);
        CHESTGENCATEGORIES.add(ChestGenHooks.PYRAMID_JUNGLE_CHEST);
        CHESTGENCATEGORIES.add(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER);
        CHESTGENCATEGORIES.add(ChestGenHooks.STRONGHOLD_CORRIDOR);
        CHESTGENCATEGORIES.add(ChestGenHooks.STRONGHOLD_CROSSING);
        CHESTGENCATEGORIES.add(ChestGenHooks.STRONGHOLD_LIBRARY);
        CHESTGENCATEGORIES.add(ChestGenHooks.VILLAGE_BLACKSMITH);
    }
    
    /**
     * Check if it's day in this world.
     * @param world
     * @return If it is day in the world, checked with the world time.
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
     * @param world The world.
     * @param entityID The ID of the entity.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return the entity that was spawned.
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
                entityliving.onSpawnWithEgg((IEntityLivingData)null);
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
     * @param saveNBT If the NBT data should be saved to the dropped item.
     */
    public static void preDestroyBlock(World world, int x, int y, int z, boolean saveNBT) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof IInventory && !world.isRemote) {
            dropItems(world, (IInventory) tile, x, y, z);
            clearInventory((IInventory) tile);
        }
        
        if (tile instanceof EvilCraftTileEntity && saveNBT) {
            // Cache 
            EvilCraftTileEntity ecTile = ((EvilCraftTileEntity) tile);
            TileEntityNBTStorage.TAG = ecTile.getNBTTagCompound();
            
            ecTile.destroy();
        } else {
            TileEntityNBTStorage.TAG = null;
        }
    }
    
    /**
     * This method should be called after a BlockContainer is destroyed
     * @param world world
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public static void postDestroyBlock(World world, int x, int y, int z) {
        // Does nothing for now.
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
     * @return The id of the enchantment in the enchantmentlist or -1 if it does not apply.
     */
    public static int doesEnchantApply(ItemStack itemStack, int enchantID) {
        if(itemStack != null) {
            NBTTagList enchantmentList = itemStack.getEnchantmentTagList();
            if(enchantmentList != null) {
                for(int i = 0; i < enchantmentList.tagCount(); i++) {
                    if (((NBTTagCompound)enchantmentList.getCompoundTagAt(i)).getShort("id") == enchantID) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the level of an enchantment given an itemStack and the list id
     * of the enchantment in the enchantmentlist (see doesEnchantApply() to get
     * the id in the enchantmentlist)
     * @param itemStack The itemStack which contains the enchanted item
     * @param enchantmentListID The id of the enchantment in the enchantment list
     * @return The level of the enchantment on the given item
     */
    public static int getEnchantmentLevel(ItemStack itemStack, int enchantmentListID) {
        NBTTagList enchlist = itemStack.getEnchantmentTagList();
        return ((NBTTagCompound)enchlist.getCompoundTagAt(enchantmentListID)).getShort("lvl");
    }
    
    /**
     * Returns the id of an enchantment given an itemStack and the list id
     * of the enchantment in the enchantmentlist (see doesEnchantApply() to get
     * the id in the enchantmentlist)
     * @param itemStack The itemStack which contains the enchanted item
     * @param enchantmentListID The id of the enchantment in the enchantment list
     * @return The id of the enchantment on the given item
     */
    public static int getEnchantmentID(ItemStack itemStack, int enchantmentListID) {
        NBTTagList enchlist = itemStack.getEnchantmentTagList();
        return ((NBTTagCompound)enchlist.getCompoundTagAt(enchantmentListID)).getShort("id");
    }
    
    /**
     * Sets the level of an enchantment given an itemStack and the id
     * of the enchantment in the enchantmentlist (see doesEnchantApply() to get
     * the id in the enchantmentlist)
     * Will clear the enchantment if the new level <= 0
     * @param itemStack The itemStack which contains the enchanted item
     * @param enchantmentListID The id of the enchantment in the enchantment list
     * @param level The new level of the enchantment on the given item
     */
    public static void setEnchantmentLevel(ItemStack itemStack, int enchantmentListID, int level) {
        NBTTagList enchlist = itemStack.getEnchantmentTagList();
        if(level <= 0) {
            enchlist.removeTag(enchantmentListID);
            if(enchlist.tagCount() == 0) {
                itemStack.stackTagCompound.removeTag("ench");
            }
        } else {
            ((NBTTagCompound)enchlist.getCompoundTagAt(enchantmentListID)).setShort("lvl", (short) level);
        }
    }
    
    /**
     * Iterate over the coordinates for the sides of the given block coordinates.
     * @param xc x coordinate of the center block.
     * @param yc y coordinate of the center block.
     * @param zc z coordinate of the center block.
     * @return Iterator for coordinates of sides.
     * @see Coordinate
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
    
    /**
     * Get an iterator for all the {@link ForgeDirection}.
     * @return The {@link ForgeDirection} iterator
     * @see Helpers#DIRECTIONS
     * @see ForgeDirection
     */
    public static Iterator<ForgeDirection> getDirectionIterator() {
        return DIRECTIONS.iterator();
    }
    
    /**
     * Check if a command sender is an operator.
     * @param sender The command sender.
     * @return If this sender is an OP.
     */
    public static boolean isOp(ICommandSender sender) {
    	int op_level = 4;
    	EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager()
    			.func_152612_a(sender.getCommandSenderName());
    	if (player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile())) {
            UserListOpsEntry userlistopsentry = (UserListOpsEntry) player.mcServer
            		.getConfigurationManager().func_152603_m()
            		.func_152683_b(player.getGameProfile());
            return userlistopsentry != null ? userlistopsentry.func_152644_a() >= op_level
            		: player.mcServer.getOpPermissionLevel() >= op_level;
        }
    	return false;
    }
    
    /**
     * Safe parsing of a string to it's real object type.
     * The real object type is determined by checking the class of the oldValue.
     * @param newValue The value to parse
     * @param oldValue The old value that has a certain type.
     * @return The parsed newValue.
     */
    public static Object tryParse(String newValue, Object oldValue) {
        Object newValueParsed = null;
        try {
            if(oldValue instanceof Integer) {
                newValueParsed = Integer.parseInt(newValue);
            } else if(oldValue instanceof Boolean) {
                newValueParsed = Boolean.parseBoolean(newValue);
            } else if(oldValue instanceof Double) {
                newValueParsed = Double.parseDouble(newValue);
            } else if(oldValue instanceof String) {
                newValueParsed = newValue;
            }
        } catch (Exception e) {}
        return newValueParsed;
    }
    
    /**
     * Get the ForgeDirection the entity is facing, only vertical directions.
     * @param entity The entity that is facing a direction.
     * @return The {@link ForgeDirection} the entity is facing.
     */
    public static ForgeDirection getEntityFacingDirection(EntityLivingBase entity) {
        int facingDirection = MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;
        return ENTITYFACING[facingDirection];
    }
    
    /**
     * Get the {@link ForgeDirection} from the sign of an X offset.
     * @param xSign X offset from somewhere.
     * @return The {@link ForgeDirection} for the offset.
     */
    public static ForgeDirection getForgeDirectionFromXSign(int xSign) {
    	return xSign > 0 ? ForgeDirection.EAST : ForgeDirection.WEST;
    }
    
    /**
     * Get the {@link ForgeDirection} from the sign of an Z offset.
     * @param zSign Z offset from somewhere.
     * @return The {@link ForgeDirection} for the offset.
     */
    public static ForgeDirection getForgeDirectionFromZSing(int zSign) {
    	return zSign > 0 ? ForgeDirection.SOUTH : ForgeDirection.NORTH;
    }
    
    /**
     * Check if Minecraft is currently running in an obfusicated environment.
     * @return If we run obfusicated.
     */
    public static boolean isObfusicated() {
        if(!ISOBFUSICATED_CHECKED) {
            ISOBFUSICATED_CHECKED = true;
            ISOBFUSICATED = false;
            try {
                Minecraft.getMinecraft().getClass().getField("currentScreen");
            } catch (NoSuchFieldException e) {
                ISOBFUSICATED = true;
            } catch (SecurityException e) {}
        }
        return ISOBFUSICATED;
    }
    
    /**
     * Check if the given player inventory is full.
     * @param player The player.
     * @return If the player does not have a free spot in it's inventory.
     */
    public static boolean isPlayerInventoryFull(EntityPlayer player) {
        return player.inventory.getFirstEmptyStack() == -1;
    }
    
    /**
     * Check if this code is ran on client side.
     * @return If we are at client side.
     */
    public static boolean isClientSide() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }

    /**
     * This should by called when custom entities collide. It will call the
     * correct method in {@link Block#onEntityCollidedWithBlock(World, int, int, int, Entity)}.
     * @param world The world
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param entity The entity that collides.
     */
    public static void onEntityCollided(World world, int x, int y, int z, Entity entity) {
        Block block = world.getBlock(x, y, z);
        if(block != null)
            block.onEntityCollidedWithBlock(world, x, y, z, entity);
    }
    
    /**
     * Normalize an angle around 180 degrees so that this value doesn't become too large/small.
     * @param angle The angle to normalize.
     * @return The normalized angle.
     */
    public static float normalizeAngle_180(float angle) {
        angle %= 360;
        
        while (angle <= -180)
            angle += 360;
        
        while (angle > 180)
            angle -= 360;
        
        return angle;
    }
    
    /**
     * Get localized info for the given block for display in tooltips.
     * @param block The block.
     * @return Localized info.
     */
    public static String getLocalizedInfo(Block block) {
    	return IInformationProvider.INFO_PREFIX + StatCollector.translateToLocal(block.getUnlocalizedName() + ".info");
    }
    
    /**
     * Get localized info for the given item for display in tooltips.
     * @param item The item.
     * @return Localized info.
     */
    public static String getLocalizedInfo(Item item) {
    	return getLocalizedInfo(item, "");
    }
    
    /**
     * Get localized info for the given item for display in tooltips.
     * @param item The item.
     * @param suffix The suffix to add to the unlocalized name.
     * @return Localized info.
     */
    public static String getLocalizedInfo(Item item, String suffix) {
    	return IInformationProvider.INFO_PREFIX + StatCollector.translateToLocal(item.getUnlocalizedName() + ".info" + suffix);
    }
    
    /**
     * Creates a {@link TargetPoint} for the dimension and position of the given {@link Entity}
     * and a given range.
     * 
     * @param entity Entity who's dimension and position will be used to create the {@link TargetPoint}. 
     * @param range The range of the {@link TargetPoint}.
     * @return A {@link TargetPoint} with the position and dimension of the entity and the given range.
     */
    public static TargetPoint createTargetPointFromEntityPosition(Entity entity, int range) {
    	return new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, range);
    }
    
	/**
     * Get a new ID for the given type.
     * @param type Type for a {@link Configurable}.
     * @return The incremented ID.
     */
    public static int getNewId(IDType type) {
    	Integer ID = ID_COUNTER.get(type);
    	if(ID == null) ID = new Integer(0);
    	ID_COUNTER.put(type, ID + 1);
    	return ID;
    }
    
    /**
     * Type of ID's to use in {@link Helpers#getNewId(IDType)}
     * @author rubensworks
     *
     */
    public enum IDType {
    	/**
    	 * Entity ID.
    	 */
    	ENTITY,
    	/**
    	 * GUI ID.
    	 */
    	GUI;
    }
    
    /**
     * Get the list of entities within a certain area.
     * @param world The world to look in.
     * @param x The center X coordinate.
     * @param y The center Y coordinate.
     * @param z The center Z coordinate.
     * @param area The radius of the area.
     * @return The list of entities in that area.
     */
    public static List<Entity> getEntitiesInArea(World world, int x, int y, int z, int area) {
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(area, area, area);
        @SuppressWarnings("unchecked")
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
        return entities;
    }
    
    /**
     * Get a random point inside a sphere in an efficient way.
     * @param center The center coordinates of the sphere.
     * @param radius The radius of the sphere.
     * @return The coordinates of the random point.
     */
    public static Coordinate getRandomPointInSphere(Coordinate center, int radius) {
        Coordinate randomPoint = null;
        while(randomPoint == null) {
            int x = center.x - radius + random.nextInt(2 * radius);
            int y = center.y - radius + random.nextInt(2 * radius);
            int z = center.z - radius + random.nextInt(2 * radius);
            int dx = center.x - x;
            int dy = center.y - y;
            int dz = center.z - z;
            int distance = (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
            if(distance <= radius) {
                randomPoint = new Coordinate(x, y, z);
            }
        }
        return randomPoint;
    }
}
