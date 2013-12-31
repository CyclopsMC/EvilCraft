package evilcraft;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.api.BucketHandler;
import evilcraft.api.Debug;
import evilcraft.api.LoggerHelper;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.render.MultiPassBlockRenderer;
import evilcraft.events.LivingAttackEventHook;
import evilcraft.events.LivingDeathEventHook;
import evilcraft.events.PlaySoundAtEntityEventHook;
import evilcraft.events.PlayerInteractEventHook;
import evilcraft.events.TextureStitchEventHook;
import evilcraft.proxies.CommonProxy;
import evilcraft.worldgen.EvilDungeonGenerator;
import evilcraft.worldgen.EvilWorldGenerator;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class EvilCraft {
    
    @SidedProxy(clientSide = "evilcraft.proxies.ClientProxy", serverSide = "evilcraft.proxies.CommonProxy")
    public static CommonProxy proxy;
    
    public static EvilCraft _instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LoggerHelper.init();
        LoggerHelper.log(Level.INFO, "preInit()");
        
        // Save this instance, so we can use it later
        this._instance = this;
        
        // Register configs and start with loading the general configs
        Configs.getInstance().registerGeneralConfigs();
        ConfigHandler.getInstance().handle(event);
        Configs.getInstance().registerConfigs();
        
        // Run debugging tools
        if(GeneralConfig.debug) {
            Debug.checkPreConfigurables(Configs.getInstance().configs);
        }
        
        // Load the rest of the configs and run the ConfigHandler to make/read the config and fill in the game registry
        ConfigHandler.getInstance().handle(event);   
        
        // Run debugging tools (after registering Configurables)
        if(GeneralConfig.debug) {
            Debug.checkPostConfigurables();
        }
        
        // Add death messages
        DeathMessages.register();
    }
    
    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        LoggerHelper.log(Level.INFO, "init()");
        
        GameRegistry.registerWorldGenerator(new EvilWorldGenerator());
        GameRegistry.registerWorldGenerator(new EvilDungeonGenerator());
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        proxy.registerRenderers();
    }
    
    @EventHandler
    public void postInit(FMLPreInitializationEvent event) {
        LoggerHelper.log(Level.INFO, "postInit()");
        
        Recipes.registerRecipes();
        
        MinecraftForge.EVENT_BUS.register(BucketHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(new LivingDeathEventHook());
        MinecraftForge.EVENT_BUS.register(new TextureStitchEventHook());
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHook());
        MinecraftForge.EVENT_BUS.register(new LivingAttackEventHook());
        MinecraftForge.EVENT_BUS.register(new PlaySoundAtEntityEventHook());
    }
    
    public static void log(String message) {
        log(message, Level.INFO);
    }
    
    public static void log(String message, Level level) {
        LoggerHelper.log(level, message);
    }
}
