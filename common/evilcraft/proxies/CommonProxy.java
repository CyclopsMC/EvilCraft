package evilcraft.proxies;

import cpw.mods.fml.common.network.NetworkRegistry;
import evilcraft.EvilCraft;
import evilcraft.Reference;
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
        NetworkRegistry.INSTANCE.registerChannel(new FartPacketHandler(), Reference.MOD_CHANNEL);
        EvilCraft.log("Registered remote key handlers");
    }
    
    /**
     * Register tick handlers.
     */
    public void registerTickHandlers() {
        
    }
}
