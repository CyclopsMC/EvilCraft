package evilcraft.proxies;

import net.minecraftforge.common.MinecraftForge;
import evilcraft.EvilCraft;
import evilcraft.api.BucketHandler;
import evilcraft.events.BonemealEventHook;
import evilcraft.events.EntityStruckByLightningEventHook;
import evilcraft.events.ItemCraftedEventHook;
import evilcraft.events.LivingAttackEventHook;
import evilcraft.events.LivingDeathEventHook;
import evilcraft.events.PlayerInteractEventHook;
import evilcraft.network.FartPacket;
import evilcraft.network.PacketHandler;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy {
    
    /**
     * Register renderers.
     */
    public void registerRenderers() {
        // Nothing here as the server doesn't render graphics!
    }
    
    /**
     * Register key bindings.
     */
    public void registerKeyBindings() {
    }
 
    /**
     * Register packet handlers.
     */
    public void registerPacketHandlers() {
    	PacketHandler.init();
    	
    	// Register packets.
    	// TODO: this can be cleaned up.
    	PacketHandler.register(0, FartPacket.class);
    	
        EvilCraft.log("Registered packet handler.");
    }
    
    /**
     * Register tick handlers.
     */
    public void registerTickHandlers() {
        
    }
    
    /**
     * Register the event hooks
     */
    public void registerEventHooks() {
    	MinecraftForge.EVENT_BUS.register(BucketHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(new LivingDeathEventHook());
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHook());
        MinecraftForge.EVENT_BUS.register(new LivingAttackEventHook());
        MinecraftForge.EVENT_BUS.register(new BonemealEventHook());
        MinecraftForge.EVENT_BUS.register(new ItemCraftedEventHook());
        MinecraftForge.EVENT_BUS.register(new EntityStruckByLightningEventHook());
    }
}
