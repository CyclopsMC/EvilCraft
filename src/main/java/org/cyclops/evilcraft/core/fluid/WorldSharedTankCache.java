package org.cyclops.evilcraft.core.fluid;

import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.network.packet.UpdateWorldSharedTankClientCachePacket;

import java.util.Iterator;
import java.util.Map;

/**
 * The cache for the shared tank contents.
 * @author rubensworks
 */
public class WorldSharedTankCache {

    /**
     * The amount of ticks inbetween a packet update.
     */
    public static final int INTERPOLATION_TICK_OFFSET = 10;

    private static WorldSharedTankCache _instance = null;

    private Map<String, UpdateWorldSharedTankClientCachePacket> packetBuffer = Maps.newHashMap();
    private int tick = 0;

    private WorldSharedTankCache() {

    }

    /**
     * Reset the cache, packet buffer and tick offset.
     */
    public void reset() {
        packetBuffer = Maps.newHashMap();
        tick = 0;
    }

    /**
     * Get the unique instance.
     * @return The unique instance.
     */
    public static WorldSharedTankCache getInstance() {
        if(_instance == null) {
            _instance = new WorldSharedTankCache();
        }
        return _instance;
    }

    protected String getMapID(String tankID) {
        return tankID + (IModHelpers.get().getMinecraftHelpers().isClientSide() ? "C" : "S");
    }

    protected String removeMapID(String mapID) {
        return mapID.substring(0, mapID.length() - 1);
    }

    /**
     * Get a tank contents.
     * @param tankID The tank.
     * @return The contents.
     */
    public synchronized FluidStack getTankContent(String tankID) {
        return EvilCraft.sharedTanks.get().getFluid(getMapID(tankID)).copy();
    }

    protected static boolean shouldRefreshFluid(FluidStack old, FluidStack newF) {
        return old.getAmount() != newF.getAmount();
    }

    /**
     * Set the tank contents.
     * @param tankID The id of the tank.
     * @param fluidStack The tank contents.
     */
    public synchronized void setTankContent(String tankID, FluidStack fluidStack) {
        String key = getMapID(tankID);
        WorldStorageSharedTank sharedTanks = EvilCraft.sharedTanks.get();
        boolean shouldRefresh = shouldRefreshFluid(sharedTanks.getFluid(key), fluidStack);
        sharedTanks.setFluid(key, fluidStack.copy());
        if(!IModHelpers.get().getMinecraftHelpers().isClientSide() && shouldRefresh) {
            bufferPacket(tankID, new UpdateWorldSharedTankClientCachePacket(tankID, fluidStack));
        }
    }

    protected void bufferPacket(String tankID, UpdateWorldSharedTankClientCachePacket packet) {
        packetBuffer.put(tankID, packet);
    }

    /**
     * Get the ticks since last packet flush.
     * @return The tick offset.
     */
    public int getTickOffset() {
        return this.tick;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onTickServer(ServerTickEvent.Pre event) {
        this.onTick(event);
    }

    /**
     * When a tick event is received.
     * @param event The received event.
     */
    public void onTick(ServerTickEvent.Pre event) {
        tick++;
        if(getTickOffset() > INTERPOLATION_TICK_OFFSET) {
            Iterator<Map.Entry<String, UpdateWorldSharedTankClientCachePacket>> it = packetBuffer.entrySet().iterator();
            while(it.hasNext()) {
                try {
                    EvilCraft._instance.getPacketHandler().sendToAll(it.next().getValue());
                } catch (UnsupportedOperationException e) {
                    // Can occur during game testing
                    e.printStackTrace();
                }
                it.remove();
            }
            tick = 0;
        }
    }

    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if(!IModHelpers.get().getMinecraftHelpers().isClientSide()) {
            for(Map.Entry<String, FluidStack> entry: EvilCraft.sharedTanks.get().getEntries()) {
                EvilCraft._instance.getPacketHandler().sendToPlayer(
                        new UpdateWorldSharedTankClientCachePacket(removeMapID(entry.getKey()), entry.getValue()), (ServerPlayer) event.getEntity());
            }
        }
    }

}
