package evilcraft;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.client.GuiBloodInfuser;
import evilcraft.gui.container.ContainerBloodInfuser;

public class GuiHandler implements IGuiHandler {

    public static Map<Integer, Class<? extends Container>> CONTAINERS = new HashMap<Integer, Class<? extends Container>>();
    public static Map<Integer, Class<? extends GuiContainer>> GUIS = new HashMap<Integer, Class<? extends GuiContainer>>();

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        Class<? extends Container> containerClass = CONTAINERS.get(id);
        try {
            Constructor<? extends Container> containerConstructor = containerClass.getConstructor(InventoryPlayer.class, tileEntity.getClass());
            return containerConstructor.newInstance(player.inventory, tileEntity);
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

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        Class<? extends GuiContainer> guiClass = GUIS.get(id);
        try {
            Constructor<? extends GuiContainer> guiConstructor = guiClass.getConstructor(InventoryPlayer.class, tileEntity.getClass());
            return guiConstructor.newInstance(player.inventory, tileEntity);
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

}
