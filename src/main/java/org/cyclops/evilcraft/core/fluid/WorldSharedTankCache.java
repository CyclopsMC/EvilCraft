package org.cyclops.evilcraft.core.fluid;

import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;
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
                EvilCraft._instance.getPacketHandler().sendToAll(it.next().getValue());
                it.remove();
            }
            tick = 0;
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
    public void readFromNBT(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        if(tag != null) {
            ListTag list = tag.getList("tankCache", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag subTag = list.getCompound(i);
                setTankContent(subTag.getString("key"),
                        FluidStack.CODEC.decode(holderLookupProvider.createSerializationContext(NbtOps.INSTANCE), subTag.getCompound("value")).getOrThrow().getFirst());
            }
        }
    }

    /**
     * Write the cache.
     * @param tag The tag to write to.
     */
    public void writeToNBT(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        ListTag list = new ListTag();
        for(Map.Entry<String, FluidStack> entry : tankCache.entrySet()) {
            CompoundTag subTag = new CompoundTag();
            subTag.putString("key", removeMapID(entry.getKey()));
            Tag fluidTag = FluidStack.CODEC.encodeStart(holderLookupProvider.createSerializationContext(NbtOps.INSTANCE), entry.getValue()).getOrThrow();
            subTag.put("value", fluidTag);
            list.add(subTag);
        }
        tag.put("tankCache", list);
    }

}
