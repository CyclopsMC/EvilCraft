package evilcraft.event;

import evilcraft.core.fluid.WorldSharedTank;
import evilcraft.core.fluid.WorldSharedTankCache;
import evilcraft.core.world.GlobalCounter;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

/**
 * Event hook for server starting and stopping events.
 * TODO: this can be abstracted quite a lot...
 * @author rubensworks
 *
 */
public class ServerStatusEventHook {

    private static ServerStatusEventHook _instance = null;

    private ServerStatusEventHook() {

    }

    /**
     * @return The unique instance.
     */
    public static ServerStatusEventHook getInstance() {
        if(_instance == null) {
            _instance = new ServerStatusEventHook();
        }
        return _instance;
    }

    /**
     * When a server is started.
     * @param event The received event.
     */
    public void onStartedEvent(FMLServerStartedEvent event) {
        loadCounters();
        loadTankData();
    }

    /**
     * When a server is stopping.
     * @param event The received event.
     */
    public void onStoppingEvent(FMLServerStoppingEvent event) {
        saveCounters();
        saveTankData();
    }

    private GlobalCounter.GlobalCounterData getCounterData() {
        GlobalCounter.GlobalCounterData data = (GlobalCounter.GlobalCounterData) MinecraftServer.getServer().worldServers[0]
                .loadItemData(GlobalCounter.GlobalCounterData.class, GlobalCounter.GlobalCounterData.KEY);
        if(data == null) {
            data = new GlobalCounter.GlobalCounterData(GlobalCounter.GlobalCounterData.KEY);
            MinecraftServer.getServer().worldServers[0].setItemData(GlobalCounter.GlobalCounterData.KEY, data);
        }
        return data;
    }
    
    private void loadCounters() {
        GlobalCounter.getInstance().reset();
        GlobalCounter.getInstance().readFromNBT(getCounterData().tag);
    }

    private void saveCounters() {
        GlobalCounter.GlobalCounterData data = getCounterData();
        GlobalCounter.getInstance().writeToNBT(data.tag);
        data.setDirty(true);
    }

    private WorldSharedTank.TankData getTankData() {
        WorldSharedTank.TankData data = (WorldSharedTank.TankData) MinecraftServer.getServer().worldServers[0]
                .loadItemData(WorldSharedTank.TankData.class, WorldSharedTank.TankData.KEY);
        if(data == null) {
            data = new WorldSharedTank.TankData(WorldSharedTank.TankData.KEY);
            MinecraftServer.getServer().worldServers[0].setItemData(WorldSharedTank.TankData.KEY, data);
        }
        return data;
    }

    private void loadTankData() {
        WorldSharedTankCache.getInstance().reset();
        WorldSharedTankCache.getInstance().readFromNBT(getTankData().getTankTag());
    }

    private void saveTankData() {
        WorldSharedTank.TankData data = getTankData();
        WorldSharedTankCache.getInstance().writeToNBT(data.getTankTag());
        data.setDirty(true);
    }
    
}
