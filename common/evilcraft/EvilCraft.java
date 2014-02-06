package evilcraft;
import java.util.logging.Level;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import evilcraft.api.BucketHandler;
import evilcraft.api.Debug;
import evilcraft.api.LoggerHelper;
import evilcraft.api.config.ConfigHandler;
import evilcraft.commands.CommandEvilCraft;
import evilcraft.events.BonemealEventHook;
import evilcraft.events.LivingAttackEventHook;
import evilcraft.events.LivingDeathEventHook;
import evilcraft.events.PlaySoundAtEntityEventHook;
import evilcraft.events.PlayerInteractEventHook;
import evilcraft.events.TextureStitchEventHook;
import evilcraft.gui.client.GuiMainMenuEvilifier;
import evilcraft.proxies.CommonProxy;
import evilcraft.worldgen.DarkTempleGenerator;
import evilcraft.worldgen.EvilDungeonGenerator;
import evilcraft.worldgen.EvilWorldGenerator;

@Mod(modid = Reference.MOD_ID,
    name = Reference.MOD_NAME,
    useMetadata = true,
    version = Reference.MOD_VERSION,
    dependencies = Reference.MOD_DEPENDENCIES
    )
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
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
        Configs.getInstance().registerVanillaDictionary();
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
        
        // Add custom panorama's
        if(event.getSide() == Side.CLIENT) {
            GuiMainMenuEvilifier.evilifyMainMenu();
        }

        proxy.registerKeyBindings();
        proxy.registerPacketHandlers();
        proxy.registerTickHandlers();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        LoggerHelper.log(Level.INFO, "init()");
        
        GameRegistry.registerWorldGenerator(new EvilWorldGenerator());
        GameRegistry.registerWorldGenerator(new EvilDungeonGenerator());
        GameRegistry.registerWorldGenerator(new DarkTempleGenerator());
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        proxy.registerRenderers();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LoggerHelper.log(Level.INFO, "postInit()");
        
        Recipes.registerRecipes();
        
        MinecraftForge.EVENT_BUS.register(BucketHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(new LivingDeathEventHook());
        MinecraftForge.EVENT_BUS.register(new TextureStitchEventHook());
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHook());
        MinecraftForge.EVENT_BUS.register(new LivingAttackEventHook());
        MinecraftForge.EVENT_BUS.register(new PlaySoundAtEntityEventHook());
        MinecraftForge.EVENT_BUS.register(new BonemealEventHook());
    }
    
    @EventHandler
    public void onLivingAttack(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandEvilCraft());
    }
    
    public static void log(String message) {
        log(message, Level.INFO);
    }
    
    public static void log(String message, Level level) {
        LoggerHelper.log(level, message);
    }
}
