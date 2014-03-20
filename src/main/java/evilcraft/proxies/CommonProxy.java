package evilcraft.proxies;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.NetworkRegistry;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.api.BucketHandler;
import evilcraft.events.BonemealEventHook;
import evilcraft.events.ItemCraftedEventHook;
import evilcraft.events.LivingAttackEventHook;
import evilcraft.events.LivingDeathEventHook;
import evilcraft.events.PlayerInteractEventHook;
import evilcraft.network.FartPacketHandler;

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
    	// Create and save a new network wrapper
    	EvilCraft.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(Reference.MOD_CHANNEL);
    	
    	// Registration of packet handlers
    	EvilCraft.channel.register(new FartPacketHandler());
    	
        EvilCraft.log("Registered remote key handlers");
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
    }
}
