package evilcraft.client.gui;

import com.google.common.collect.Maps;
import evilcraft.core.inventory.IGuiContainerProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * The handler class that will map Containers to GUI's.
 * @author rubensworks
 *
 */
public class GuiHandler implements IGuiHandler {

    private static Map<Integer, Class<? extends Container>> CONTAINERS = Maps.newHashMap();
    private static Map<Integer, Class<? extends GuiScreen>> GUIS = Maps.newHashMap();
    private static Map<Integer, GuiType> TYPES = Maps.newHashMap();
    
    // Two variables to avoid collisions in singleplayer worlds
    private static int TEMP_ITEM_GUI_INDEX_OVERRIDE_CLIENT = -1;
    private static int TEMP_ITEM_GUI_INDEX_OVERRIDE_SERVER = -1;
    
    /**
     * Register a new GUI.
     * @param guiProvider A provider of GUI data.
     * @param type The GUI type.
     */
    public static void registerGUI(IGuiContainerProvider guiProvider, GuiType type) {
    	CONTAINERS.put(guiProvider.getGuiID(), guiProvider.getContainer());
    	if(MinecraftHelpers.isClientSide()) {
    		GUIS.put(guiProvider.getGuiID(), guiProvider.getGUI());
    	}
    	TYPES.put(guiProvider.getGuiID(), type);
    }
    
    /**
     * Set a temporary index in the player inventory for showing gui's for items.
     * This temporary index will be removed once a gui/container is opened once.
     * @param index The index.
     */
    public static void setTemporaryItemIndex(int index) {
    	if(MinecraftHelpers.isClientSide()) {
    		TEMP_ITEM_GUI_INDEX_OVERRIDE_CLIENT = index;
    	} else {
    		TEMP_ITEM_GUI_INDEX_OVERRIDE_SERVER = index;
    	}
    }
    
    /**
     * Clear the temporary item index for item containers.
     */
    private static void clearTemporaryItemIndex() {
    	setTemporaryItemIndex(-1);
    }
    
    private static int getItemIndex(EntityPlayer player) throws IllegalArgumentException {
    	int index = MinecraftHelpers.isClientSide() ? TEMP_ITEM_GUI_INDEX_OVERRIDE_CLIENT : TEMP_ITEM_GUI_INDEX_OVERRIDE_SERVER;
    	if(index == -1) {
    		index = player.inventory.currentItem;
    	}
    	clearTemporaryItemIndex();
        if(index == -1) {
            throw new IllegalArgumentException("Invalid GUI item.");
        }
    	return index;
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	try {
    		Class<? extends Container> containerClass = CONTAINERS.get(id);
            if(containerClass == null) {
                return null; // Possible with client-only GUI's like books.
            }
    		if(TYPES.get(id) == GuiType.BLOCK) {
    			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
    			Constructor<? extends Container> containerConstructor = containerClass.getConstructor(InventoryPlayer.class, tileEntity.getClass());
    			return containerConstructor.newInstance(player.inventory, tileEntity);
    		} else {
    			Constructor<? extends Container> containerConstructor = containerClass.getConstructor(EntityPlayer.class, int.class);
    			return containerConstructor.newInstance(player, getItemIndex(player));
    		}
    	} catch (NoSuchMethodException e) {
    		e.printStackTrace();
    	} catch (SecurityException e) {
    		e.printStackTrace();
    	} catch (InstantiationException e) {
    		e.printStackTrace();
    	} catch (IllegalAccessException e) {
    		e.printStackTrace();
    	} catch (IllegalArgumentException e) {
    		e.printStackTrace();
    	} catch (InvocationTargetException e) {
    		e.printStackTrace();
    	}
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	try {
    		Class<? extends GuiScreen> guiClass = GUIS.get(id);
    		if(TYPES.get(id) == GuiType.BLOCK) {
    			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
	            Constructor<? extends GuiScreen> guiConstructor = guiClass.getConstructor(InventoryPlayer.class, tileEntity.getClass());
	            return guiConstructor.newInstance(player.inventory, tileEntity);
    		} else {
	            Constructor<? extends GuiScreen> guiConstructor = guiClass.getConstructor(EntityPlayer.class, int.class);
	            return guiConstructor.newInstance(player, getItemIndex(player));
    		}
    	} catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Types of GUIs.
     * @author rubensworks
     */
    public static enum GuiType {
    	
    	/**
    	 * A blockState with a tile entity.
    	 */
    	BLOCK,
    	/**
    	 * An item.
    	 */
    	ITEM;
    	
    }

}
