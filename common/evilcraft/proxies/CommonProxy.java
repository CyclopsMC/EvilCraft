package evilcraft.proxies;

import cpw.mods.fml.common.network.NetworkRegistry;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.network.FartPacketHandler;

public class CommonProxy {
    
    public void registerRenderers() {
        // Nothing here as the server doesn't render graphics!
    }
    
    public void registerKeyBindings() {
    }
 
    public void registerPacketHandlers() {
        NetworkRegistry.instance().registerChannel(new FartPacketHandler(), Reference.MOD_CHANNEL);
        EvilCraft.log("Registered remote key handlers");
    }
    
    public void registerTickHandlers() {
        
    }
}
