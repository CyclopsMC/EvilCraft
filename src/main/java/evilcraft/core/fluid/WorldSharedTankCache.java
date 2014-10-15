package evilcraft.core.fluid;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.network.PacketHandler;
import evilcraft.network.packet.UpdateWorldSharedTankClientCachePacket;

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
	private Set<UpdateWorldSharedTankClientCachePacket> packetBuffer = Sets.newHashSet();
	private int tick = 0;
	
	private WorldSharedTankCache() {
		
	}
	
	/**
	 * Reset the cache, packet buffer and tick offset.
	 */
	public void reset() {
		tankCache = Maps.newHashMap();
		packetBuffer = Sets.newHashSet();
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
	
	/**
	 * Get a tank contents.
	 * @param tankID The tank.
	 * @return The contents.
	 */
	public FluidStack getTankContent(String tankID) {
		return tankCache.get(getMapID(tankID));
	}
	
	protected static boolean shouldRefreshFluid(FluidStack old, FluidStack newF) {
    	return (old == null && newF != null) || (old != null && !old.equals(newF));
    }
	
	/**
	 * Set the tank contents.
	 * @param tankID The id of the tank.
	 * @param fluidStack The tank contents.
	 */
	public void setTankContent(String tankID, FluidStack fluidStack) {
		boolean shouldRefresh = shouldRefreshFluid(tankCache.get(tankID), fluidStack);
		if(fluidStack == null) {
			tankCache.remove(getMapID(tankID));
		} else if(shouldRefresh) {
			tankCache.put(getMapID(tankID), fluidStack);
		}
		if(!MinecraftHelpers.isClientSide() && shouldRefresh) {
			bufferPacket(new UpdateWorldSharedTankClientCachePacket(tankID, fluidStack));
		}
	}
	
	protected void bufferPacket(UpdateWorldSharedTankClientCachePacket packet) {
		if(packetBuffer.contains(packet)) {
			packetBuffer.remove(packet);
		}
		packetBuffer.add(packet);
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
    	if(event.phase == Phase.START && (event.type == Type.CLIENT || event.type == Type.SERVER)) {
	    	if(event.side == Side.SERVER && getTickOffset() % INTERPOLATION_TICK_OFFSET == 0) {
		    	Iterator<UpdateWorldSharedTankClientCachePacket> it = packetBuffer.iterator();
		    	while(it.hasNext()) {
		    		PacketHandler.sendToAll(it.next());
		    		it.remove();
		    	}
	    	}
	    	tick = (tick + 1) % INTERPOLATION_TICK_OFFSET;
    	}
    }
	
}
