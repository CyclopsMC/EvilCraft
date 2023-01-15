package org.cyclops.evilcraft.core.fluid;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.LogicalSide;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.network.packet.UpdateWorldSharedTankClientCachePacket;

import javax.annotation.Nullable;
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

    private Map<String, FluidStack> tankCache = Maps.newHashMap();
    private Map<String, UpdateWorldSharedTankClientCachePacket> packetBuffer = Maps.newHashMap();
    private int tick = 0;

    private WorldSharedTankCache() {

    }

    /**
     * Reset the cache, packet buffer and tick offset.
     */
    public void reset() {
        tankCache = Maps.newHashMap();
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
        return tankID + (MinecraftHelpers.isClientSide() ? "C" : "S");
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
        FluidStack stack = tankCache.get(getMapID(tankID));
        return (stack == null) ? FluidStack.EMPTY : stack.copy();
    }

    protected static boolean shouldRefreshFluid(@Nullable FluidStack old, FluidStack newF) {
        return (old == null ? 0 : old.getAmount()) != newF.getAmount();
    }

    /**
     * Set the tank contents.
     * @param tankID The id of the tank.
     * @param fluidStack The tank contents.
     */
    public synchronized void setTankContent(String tankID, FluidStack fluidStack) {
        String key = getMapID(tankID);
        boolean shouldRefresh = shouldRefreshFluid(tankCache.get(key), fluidStack);
        if(fluidStack.isEmpty()) {
            tankCache.remove(key);
        } else if(shouldRefresh) {
            tankCache.put(key, fluidStack.copy());
        }
        if(!MinecraftHelpers.isClientSide() && shouldRefresh) {
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

    /**
     * When a tick event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onTick(TickEvent event) {
        if(event.phase == TickEvent.Phase.START && (event.type == TickEvent.Type.CLIENT || event.type == TickEvent.Type.SERVER)) {
            tick++;
            if(event.side == LogicalSide.SERVER && getTickOffset() > INTERPOLATION_TICK_OFFSET) {
                Iterator<Map.Entry<String, UpdateWorldSharedTankClientCachePacket>> it = packetBuffer.entrySet().iterator();
                while(it.hasNext()) {
                    EvilCraft._instance.getPacketHandler().sendToAll(it.next().getValue());
                    it.remove();
                }
                tick = 0;
            }
        }
    }

    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if(!MinecraftHelpers.isClientSide()) {
            for(Map.Entry<String, FluidStack> entry: tankCache.entrySet()) {
                EvilCraft._instance.getPacketHandler().sendToPlayer(
                        new UpdateWorldSharedTankClientCachePacket(removeMapID(entry.getKey()), entry.getValue()), (ServerPlayer) event.getEntity());
            }
        }
    }

    /**
     * Read the cache.
     * @param tag The tag to read from.
     */
    public void readFromNBT(CompoundTag tag) {
        if(tag != null) {
            ListTag list = tag.getList("tankCache", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag subTag = list.getCompound(i);
                setTankContent(subTag.getString("key"),
                        FluidStack.loadFluidStackFromNBT(subTag.getCompound("value")));
            }
        }
    }

    /**
     * Write the cache.
     * @param tag The tag to write to.
     */
    public void writeToNBT(CompoundTag tag) {
        ListTag list = new ListTag();
        for(Map.Entry<String, FluidStack> entry : tankCache.entrySet()) {
            CompoundTag subTag = new CompoundTag();
            subTag.putString("key", removeMapID(entry.getKey()));
            CompoundTag fluidTag = new CompoundTag();
            entry.getValue().writeToNBT(fluidTag);
            subTag.put("value", fluidTag);
            list.add(subTag);
        }
        tag.put("tankCache", list);
    }

}
