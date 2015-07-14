package evilcraft;

import evilcraft.api.broom.IBroomPartRegistry;
import evilcraft.api.degradation.IDegradationRegistry;
import evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import evilcraft.client.gui.container.GuiMainMenuEvilifier;
import evilcraft.core.broom.BroomPartRegistry;
import evilcraft.core.degradation.DegradationRegistry;
import evilcraft.event.ServerStatusEventHook;
import evilcraft.infobook.InfoBookRegistry;
import evilcraft.item.DarkGemConfig;
import evilcraft.modcompat.baubles.BaublesModCompat;
import evilcraft.modcompat.nei.NEIModCompat;
import evilcraft.modcompat.waila.WailaModCompat;
import evilcraft.tileentity.tickaction.bloodchest.BloodChestRepairActionRegistry;
import evilcraft.world.gen.DarkTempleGenerator;
import evilcraft.world.gen.EvilDungeonGenerator;
import evilcraft.world.gen.OreGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigReference;
import org.cyclops.cyclopscore.init.ItemCreativeTab;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.item.BucketRegistry;
import org.cyclops.cyclopscore.item.IBucketRegistry;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.cyclopscore.recipe.custom.SuperRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.api.ISuperRecipeRegistry;
import org.cyclops.cyclopscore.world.gen.IRetroGenRegistry;
import org.cyclops.cyclopscore.world.gen.RetroGenRegistry;

/**
 * The main mod class of EvilCraft.
 * @author rubensworks
 *
 */
@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        useMetadata = true,
        version = Reference.MOD_VERSION,
        dependencies = Reference.MOD_DEPENDENCIES,
        guiFactory = "evilcraft.GuiConfigOverview$ExtendedConfigGuiFactory"
)
public class EvilCraft extends ModBase {
    
    /**
     * The proxy of this mod, depending on 'side' a different proxy will be inside this field.
     * @see net.minecraftforge.fml.common.SidedProxy
     */
    @SidedProxy(clientSide = "evilcraft.proxy.ClientProxy", serverSide = "evilcraft.proxy.CommonProxy")
    public static ICommonProxy proxy;
    
    /**
     * The unique instance of this mod.
     */
    @Instance(value = Reference.MOD_ID)
    public static EvilCraft _instance;

    public EvilCraft() {
        super(Reference.MOD_ID, Reference.MOD_NAME);

    }

    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        modCompatLoader.addModCompat(new BaublesModCompat());
        modCompatLoader.addModCompat(new WailaModCompat());
        modCompatLoader.addModCompat(new NEIModCompat());
    }

    @Override
    protected RecipeHandler constructRecipeHandler() {
        return new ExtendedRecipeHandler(this,
                "shaped.xml",
                "shapeless.xml",
                "smelting.xml",
                "bloodinfuser.xml",
                "bloodinfuser_convenience.xml",
                "bloodinfuser_mods.xml",
                "environmentalaccumulator.xml"
        );
    }

    /**
     * The pre-initialization, will register required configs.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        getRegistryManager().addRegistry(IDegradationRegistry.class, new DegradationRegistry());
        getRegistryManager().addRegistry(ISuperRecipeRegistry.class, new SuperRecipeRegistry(this));
        getRegistryManager().addRegistry(IBloodChestRepairActionRegistry.class, new BloodChestRepairActionRegistry());
        getRegistryManager().addRegistry(IRetroGenRegistry.class, new RetroGenRegistry(this));
        getRegistryManager().addRegistry(IBucketRegistry.class, new BucketRegistry());
        getRegistryManager().addRegistry(IBroomPartRegistry.class, new BroomPartRegistry());

        super.preInit(event);

        // Start fetching the version info
        VersionStats.load();
    }
    
    /**
     * Register the config dependent things like world generation and proxy handlers.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
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
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
    
    /**
     * Register the things that are related to server starting, like commands.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);
    }

    /**
     * Register the things that are related to server starting.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void onServerStarted(FMLServerStartedEvent event) {
        super.onServerStarted(event);
        ServerStatusEventHook.getInstance().onStartedEvent(event);
    }

    /**
     * Register the things that are related to server stopping, like persistent storage.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
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
        EvilCraft._instance.getLoggerHelper().log(level, message);
    }
    
}
