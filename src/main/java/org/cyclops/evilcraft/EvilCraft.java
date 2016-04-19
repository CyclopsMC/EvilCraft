package org.cyclops.evilcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigReference;
import org.cyclops.cyclopscore.infobook.IInfoBookRegistry;
import org.cyclops.cyclopscore.infobook.InfoBookRegistry;
import org.cyclops.cyclopscore.init.ItemCreativeTab;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.item.BucketRegistry;
import org.cyclops.cyclopscore.item.IBucketRegistry;
import org.cyclops.cyclopscore.modcompat.ICapabilityCompat;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.persist.world.GlobalCounters;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.cyclopscore.recipe.custom.SuperRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.api.ISuperRecipeRegistry;
import org.cyclops.cyclopscore.world.gen.IRetroGenRegistry;
import org.cyclops.cyclopscore.world.gen.RetroGenRegistry;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;
import org.cyclops.evilcraft.api.degradation.IDegradationRegistry;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierActionRegistry;
import org.cyclops.evilcraft.block.EnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.client.gui.container.GuiMainMenuEvilifier;
import org.cyclops.evilcraft.core.broom.BroomPartRegistry;
import org.cyclops.evilcraft.core.degradation.DegradationRegistry;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.infobook.OriginsOfDarknessBook;
import org.cyclops.evilcraft.item.DarkGemConfig;
import org.cyclops.evilcraft.modcompat.baubles.BaublesModCompat;
import org.cyclops.evilcraft.modcompat.bloodmagic.BloodMagicModCompat;
import org.cyclops.evilcraft.modcompat.capabilities.WorkerEnvirAccTileCompat;
import org.cyclops.evilcraft.modcompat.capabilities.WorkerWorkingTileCompat;
import org.cyclops.evilcraft.modcompat.jei.JEIModCompat;
import org.cyclops.evilcraft.modcompat.tconstruct.TConstructModCompat;
import org.cyclops.evilcraft.modcompat.waila.WailaModCompat;
import org.cyclops.evilcraft.tileentity.TileEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.BloodChestRepairActionRegistry;
import org.cyclops.evilcraft.tileentity.tickaction.purifier.PurifierActionRegistry;
import org.cyclops.evilcraft.world.gen.DarkTempleGenerator;
import org.cyclops.evilcraft.world.gen.EvilDungeonGenerator;
import org.cyclops.evilcraft.world.gen.OreGenerator;
import org.cyclops.evilcraft.world.gen.nbt.DarkTempleData;

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
        guiFactory = "org.cyclops.evilcraft.GuiConfigOverview$ExtendedConfigGuiFactory"
)
public class EvilCraft extends ModBaseVersionable {
    
    /**
     * The proxy of this mod, depending on 'side' a different proxy will be inside this field.
     * @see net.minecraftforge.fml.common.SidedProxy
     */
    @SidedProxy(clientSide = "org.cyclops.evilcraft.proxy.ClientProxy", serverSide = "org.cyclops.evilcraft.proxy.CommonProxy")
    public static ICommonProxy proxy;
    
    /**
     * The unique instance of this mod.
     */
    @Instance(value = Reference.MOD_ID)
    public static EvilCraft _instance;

    public static GlobalCounters globalCounters = null;
    public static DarkTempleData darkTempleData = null;

    public EvilCraft() {
        super(Reference.MOD_ID, Reference.MOD_NAME, Reference.MOD_VERSION);

        // Register world storages
        registerWorldStorage(new WorldSharedTank.TankData(this));
        registerWorldStorage(globalCounters = new GlobalCounters(this));
        registerWorldStorage(darkTempleData = new DarkTempleData(this));
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        // Mod compats
        modCompatLoader.addModCompat(new BaublesModCompat());
        modCompatLoader.addModCompat(new WailaModCompat());
        modCompatLoader.addModCompat(new JEIModCompat());
        modCompatLoader.addModCompat(new BloodMagicModCompat());
        modCompatLoader.addModCompat(new TConstructModCompat());

        // Capabilities
        ICapabilityCompat.ICapabilityReference<IWorker> workerReference = new ICapabilityCompat.ICapabilityReference<IWorker>() {
            @Override
            public Capability<IWorker> getCapability() {
                return Capabilities.WORKER;
            }
        };
        modCompatLoader.addCapabilityCompat(TickingTankInventoryTileEntity.class, workerReference, new WorkerWorkingTileCompat());
        modCompatLoader.addCapabilityCompat(TileEnvironmentalAccumulator.class, workerReference, new WorkerEnvirAccTileCompat());
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
        getRegistryManager().addRegistry(IInfoBookRegistry.class, new InfoBookRegistry());
        getRegistryManager().addRegistry(IPurifierActionRegistry.class, new PurifierActionRegistry());

        super.preInit(event);
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
        if(Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            GameRegistry.registerWorldGenerator(new DarkTempleGenerator(), 1);
        }
        
        // Add custom panorama's
        if(event.getSide() == Side.CLIENT) {
            GuiMainMenuEvilifier.evilifyMainMenu();
        }
        
        // Register achievements
        Achievements.registerAchievements();

        // Initialize info book
        getRegistryManager().getRegistry(IInfoBookRegistry.class).registerInfoBook(
                OriginsOfDarknessBook.getInstance(), "/assets/" + Reference.MOD_ID + "/info/book.xml");
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
    }

    /**
     * Register the things that are related to server stopping, like persistent storage.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void onServerStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);
    }

    @Override
    public CreativeTabs constructDefaultCreativeTab() {
        return new ItemCreativeTab(this, new ItemConfigReference(DarkGemConfig.class));
    }

    @Override
    public void onGeneralConfigsRegister(ConfigHandler configHandler) {
        configHandler.add(new GeneralConfig());
        Configs.registerVanillaDictionary();
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
