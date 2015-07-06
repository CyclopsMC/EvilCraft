package evilcraft;

import com.google.common.collect.Sets;
import evilcraft.client.gui.container.GuiMainMenuEvilifier;
import evilcraft.command.CommandEvilCraft;
import evilcraft.core.helper.LoggerHelper;
import evilcraft.event.ServerStatusEventHook;
import evilcraft.infobook.InfoBookRegistry;
import evilcraft.item.DarkGemConfig;
import evilcraft.proxy.CommonProxy;
import evilcraft.world.gen.DarkTempleGenerator;
import evilcraft.world.gen.EvilDungeonGenerator;
import evilcraft.world.gen.OreGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigReference;
import org.cyclops.cyclopscore.init.ItemCreativeTab;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

import java.io.File;
import java.util.Set;

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
    guiFactory = "evilcraft.core.client.gui.config.ExtendedConfigGuiFactory"
    )
public class EvilCraft extends ModBase {
    
    /**
     * The proxy of this mod, depending on 'side' a different proxy will be inside this field.
     * @see net.minecraftforge.fml.common.SidedProxy
     */
    @SidedProxy(clientSide = "evilcraft.proxy.ClientProxy", serverSide = "evilcraft.proxy.CommonProxy")
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
    
    private static final Set<IInitListener> initListeners = Sets.newHashSet();
    static {
        // TODO: when modcompats are restored
    	//addInitListeners(new ModCompatLoader());
    }

    public EvilCraft() {
        super(Reference.MOD_ID, Reference.MOD_NAME);
    }
    
    /**
     * The pre-initialization, will register required configs.
     * @param event The Forge event required for this.
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        // Start fetching the version info
        VersionStats.load();
    }
    
    /**
     * Register the config dependent things like world generation and proxy handlers.
     * @param event The Forge event required for this.
     */
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
        
        // Register world generation
        GameRegistry.registerWorldGenerator(new OreGenerator(), 5);
        GameRegistry.registerWorldGenerator(new EvilDungeonGenerator(), 2);
        GameRegistry.registerWorldGenerator(new DarkTempleGenerator(), 1);
        
        // Add custom panorama's
        if(event.getSide() == Side.CLIENT) {
            GuiMainMenuEvilifier.evilifyMainMenu();
        }
        
        // Register proxy related things.
        proxy.registerRenderers();
        proxy.registerKeyBindings();
        proxy.registerPacketHandlers();
        proxy.registerTickHandlers();
        
        // Register achievements
        Achievements.registerAchievements();

        // Initialize info book
        InfoBookRegistry.getInstance();
    }
    
    /**
     * Register the event hooks.
     * @param event The Forge event required for this.
     */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
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
     * Register the things that are related to server starting.
     * @param event The Forge event required for this.
     */
    @EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        super.onServerStarted(event);
        ServerStatusEventHook.getInstance().onStartedEvent(event);
    }

    /**
     * Register the things that are related to server stopping, like persistent storage.
     * @param event The Forge event required for this.
     */
    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);
        ServerStatusEventHook.getInstance().onStoppingEvent(event);
    }

    @Override
    public CreativeTabs constructDefaultCreativeTab() {
        return new ItemCreativeTab(this, new ItemConfigReference(DarkGemConfig.class));
    }

    @Override
    public void onGeneralConfigsRegister(ConfigHandler configHandler) {
        configHandler.add(new GeneralConfig());
    }

    @Override
    public void onMainConfigsRegister(ConfigHandler configHandler) {
        Configs.registerBlocks(configHandler);
    }

    @Override
    public ICommonProxy getProxy() {
        return proxy;
    }

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public static void clog(String message) {
        clog(message, Level.INFO);
    }
    
    /**
     * Log a new message of the given level for this mod.
     * @param message The message to show.
     * @param level The level in which the message must be shown.
     */
    public static void clog(String message, Level level) {
        LoggerHelper.log(level, message);
    }
    
}
