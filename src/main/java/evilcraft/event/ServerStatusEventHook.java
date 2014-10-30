package evilcraft.event;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import evilcraft.core.GlobalCounter;
import net.minecraft.server.MinecraftServer;

/**
 * Event hook for server starting and stopping events.
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
    }

    /**
     * When a server is stopping.
     * @param event The received event.
     */
    public void onStoppingEvent(FMLServerStoppingEvent event) {
        saveCounters();
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
        GlobalCounter.getInstance().readFromNBT(getCounterData().tag);
    }

    private void saveCounters() {
        GlobalCounter.GlobalCounterData data = getCounterData();
        GlobalCounter.getInstance().writeToNBT(data.tag);
        data.setDirty(true);
    }
    
}
