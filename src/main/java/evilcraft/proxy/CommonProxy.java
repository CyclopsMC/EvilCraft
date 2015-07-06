package evilcraft.proxy;

import evilcraft.EvilCraft;
import evilcraft.core.BucketHandler;
import evilcraft.core.fluid.WorldSharedTankCache;
import evilcraft.core.world.gen.RetroGenRegistry;
import evilcraft.event.*;
import evilcraft.network.PacketHandler;
import evilcraft.network.packet.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {
	
	protected static final String DEFAULT_RESOURCELOCATION_MOD = "minecraft";

    @Override
    protected ModBase getMod() {
        return EvilCraft._instance;
    }

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
    	PacketHandler.register(FartPacket.class);
    	PacketHandler.register(RingOfFirePacket.class);
    	PacketHandler.register(DetectionListenerPacket.class);
    	PacketHandler.register(SoundPacket.class);
    	PacketHandler.register(SanguinaryPedestalBlockReplacePacket.class);
    	PacketHandler.register(ExaltedCrafterButtonPacket.class);
    	PacketHandler.register(ExaltedCrafterOpenPacket.class);
    	PacketHandler.register(UpdateWorldSharedTankClientCachePacket.class);
    	
        EvilCraft.clog("Registered packet handler.");
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
    	MinecraftForge.EVENT_BUS.register(RetroGenRegistry.getInstance());
        MinecraftForge.EVENT_BUS.register(new LivingDeathEventHook());
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHook());
        MinecraftForge.EVENT_BUS.register(new LivingAttackEventHook());
        MinecraftForge.EVENT_BUS.register(new BonemealEventHook());
        MinecraftForge.EVENT_BUS.register(new EntityStruckByLightningEventHook());
        MinecraftForge.EVENT_BUS.register(new LivingUpdateEventHook());
        MinecraftForge.EVENT_BUS.register(new LivingDropsEventHook());
        MinecraftForge.EVENT_BUS.register(new LivingSpawnEventHook());
        MinecraftForge.EVENT_BUS.register(new FillBucketEventHook());
        MinecraftForge.EVENT_BUS.register(new EntityItemPickupEventHook());
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHook());
        
        FMLCommonHandler.instance().bus().register(new ConfigChangedEventHook());
        FMLCommonHandler.instance().bus().register(new PlayerRingOfFire());
        FMLCommonHandler.instance().bus().register(new ItemCraftedEventHook());
        FMLCommonHandler.instance().bus().register(WorldSharedTankCache.getInstance());
    }

}
