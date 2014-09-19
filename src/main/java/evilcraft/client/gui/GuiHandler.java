package evilcraft.client.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.inventory.IGuiContainerProvider;

/**
 * The handler class that will map Containers to GUI's.
 * @author rubensworks
 *
 */
public class GuiHandler implements IGuiHandler {

    private static Map<Integer, Class<? extends Container>> CONTAINERS = Maps.newHashMap();
    private static Map<Integer, Class<? extends GuiContainer>> GUIS = Maps.newHashMap();
    private static Map<Integer, GuiType> TYPES = Maps.newHashMap();
    
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

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	try {
    		Class<? extends Container> containerClass = CONTAINERS.get(id);
    		if(TYPES.get(id) == GuiType.BLOCK) {
    			TileEntity tileEntity = world.getTileEntity(x, y, z);
    			Constructor<? extends Container> containerConstructor = containerClass.getConstructor(InventoryPlayer.class, tileEntity.getClass());
    			return containerConstructor.newInstance(player.inventory, tileEntity);
    		} else {
    			Constructor<? extends Container> containerConstructor = containerClass.getConstructor(EntityPlayer.class);
    			return containerConstructor.newInstance(player);
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
    		Class<? extends GuiContainer> guiClass = GUIS.get(id);
    		if(TYPES.get(id) == GuiType.BLOCK) {
    			TileEntity tileEntity = world.getTileEntity(x, y, z);
	            Constructor<? extends GuiContainer> guiConstructor = guiClass.getConstructor(InventoryPlayer.class, tileEntity.getClass());
	            return guiConstructor.newInstance(player.inventory, tileEntity);
    		} else {
	            Constructor<? extends GuiContainer> guiConstructor = guiClass.getConstructor(EntityPlayer.class);
	            return guiConstructor.newInstance(player);
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
    	 * A block with a tile entity.
    	 */
    	BLOCK,
    	/**
    	 * An item.
    	 */
    	ITEM;
    	
    }

}
