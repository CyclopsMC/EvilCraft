package evilcraft;

import java.io.File;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import evilcraft.api.Debug;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.helpers.LoggerHelper;
import evilcraft.api.item.grenades.GrenadeTypeLoader;
import evilcraft.commands.CommandEvilCraft;
import evilcraft.gui.GuiHandler;
import evilcraft.gui.client.GuiMainMenuEvilifier;
import evilcraft.modcompat.ModCompatLoader;
import evilcraft.proxies.CommonProxy;
import evilcraft.worldgen.DarkTempleGenerator;
import evilcraft.worldgen.EvilDungeonGenerator;
import evilcraft.worldgen.EvilWorldGenerator;

/**
 * The main mod class of EvilCraft.
 * @author rubensworks
 *
 */
@Mod(modid = Reference.MOD_ID,
    name = Reference.MOD_NAME,
    useMetadata = true,
    version = Reference.MOD_VERSION,
    dependencies = Reference.MOD_DEPENDENCIES,
    guiFactory = "evilcraft.api.config.gui.ExtendedConfigGuiFactory"
    )
public class EvilCraft {
    
    /**
     * The proxy of this mod, depending on 'side' a different proxy will be inside this field.
     * @see cpw.mods.fml.common.SidedProxy
     */
    @SidedProxy(clientSide = "evilcraft.proxies.ClientProxy", serverSide = "evilcraft.proxies.CommonProxy")
    public static CommonProxy proxy;
    
    /**
     * The unique instance of this mod.
     */
    @Instance(value = Reference.MOD_ID)
    public static EvilCraft _instance;
    
    /**
     * Root evilcraft config folder.
     */
    public static File CONFIG_FOLDER = null;
    
    /**
     * Unique instance of the FMLEventChannel that is used to send EvilCraft messages between
     * clients and server
     */
    public static FMLEventChannel channel;
    
    private static Set<IInitListener> initListeners = Sets.newHashSet();
    static {
    	addInitListeners(new ModCompatLoader());
    	addInitListeners(new GrenadeTypeLoader());
    }
    
    /**
     * The pre-initialization, will register required configs.
     * @param event The Forge event required for this.
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LoggerHelper.init();
        LoggerHelper.log(Level.INFO, "preInit()");
        
        // Determine evilcraft config folder.
        String rootFolderName = event.getModConfigurationDirectory()
                + "/" + Reference.MOD_ID;
        CONFIG_FOLDER = new File(rootFolderName);
        if(!CONFIG_FOLDER.exists()) {
        	CONFIG_FOLDER.mkdir();
        }
        
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
        
        // Register events
        proxy.registerEventHooks();
        
        // Call init listeners
        callInitStepListeners(IInitListener.Step.PREINIT);
        
        // Start fetching the version info
        VersionStats.load();
    }
    
    /**
     * Register the config dependent things like world generation and proxy handlers.
     * @param event The Forge event required for this.
     */
    @EventHandler
    public void init(FMLInitializationEvent event) {
        LoggerHelper.log(Level.INFO, "init()");
        
        // Register world generation
        GameRegistry.registerWorldGenerator(new EvilWorldGenerator(), 5);
        GameRegistry.registerWorldGenerator(new EvilDungeonGenerator(), 2);
        GameRegistry.registerWorldGenerator(new DarkTempleGenerator(), 1);
        
        // Gui Handlers
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        
        // Add custom panorama's
        if(event.getSide() == Side.CLIENT) {
            GuiMainMenuEvilifier.evilifyMainMenu();
        }
        
        // Register proxy related things.
        proxy.registerRenderers();
        proxy.registerKeyBindings();
        proxy.registerPacketHandlers();
        proxy.registerTickHandlers();
        
        // Register recipes
        Recipes.registerRecipes(CONFIG_FOLDER);
        
        // Register achievements
        Achievements.registerAchievements();
        
        // Call init listeners
        callInitStepListeners(IInitListener.Step.INIT);
    }
    
    /**
     * Register the event hooks.
     * @param event The Forge event required for this.
     */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LoggerHelper.log(Level.INFO, "postInit()");
        
        // Call init listeners
        callInitStepListeners(IInitListener.Step.POSTINIT);
    }
    
    /**
     * Register the things that are related to server starting, like commands.
     * @param event The Forge event required for this.
     */
    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandEvilCraft());
    }
    
    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public static void log(String message) {
        log(message, Level.INFO);
    }
    
    /**
     * Log a new message of the given level for this mod.
     * @param message The message to show.
     * @param level The level in which the message must be shown.
     */
    public static void log(String message, Level level) {
        LoggerHelper.log(level, message);
    }
    
    /**
     * Register a new init listener.
     * @param initListener The init listener.
     */
    public static void addInitListeners(IInitListener initListener) {
    	synchronized(initListeners) {
    		initListeners.add(initListener);
    	}
    }
    
    /**
     * Get the init-listeners on a thread-safe way;
     * @return A copy of the init listeners list.
     */
    private static Set<IInitListener> getSafeInitListeners() {
    	Set<IInitListener> clonedInitListeners;
        synchronized(initListeners) {
        	clonedInitListeners = Sets.newHashSet(initListeners);
        }
        return clonedInitListeners;
    }
    
    private static void callInitStepListeners(IInitListener.Step step) {
    	for(IInitListener initListener : getSafeInitListeners()) {
        	initListener.onInit(step);
        }
    }
    
}
