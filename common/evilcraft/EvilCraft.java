package evilcraft;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.blocks.DarkBlock;
import evilcraft.blocks.DarkBlockConfig;
import evilcraft.blocks.DarkOreConfig;
import evilcraft.blocks.EvilBlockConfig;
import evilcraft.blocks.LiquidBlockBloodConfig;
import evilcraft.entities.item.EntityLightningGrenadeConfig;
import evilcraft.entities.monster.WerewolfConfig;
import evilcraft.items.BloodExtractorConfig;
import evilcraft.items.BucketBloodConfig;
import evilcraft.items.DarkGem;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.LightningGrenadeConfig;
import evilcraft.items.WerewolfBoneConfig;
import evilcraft.items.WerewolfFleshConfig;
import evilcraft.liquids.BloodConfig;
import evilcraft.proxies.CommonProxy;
import evilcraft.worldgen.EvilWorldGenerator;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class EvilCraft {
    
    @SidedProxy(clientSide = "evilcraft.proxies.ClientProxy", serverSide = "evilcraft.proxies.CommonProxy")
    public static CommonProxy proxy;
    
    public static EvilCraft _instance;
    
    private static Set<ExtendedConfig> configs = ConfigHandler.getInstance(); // Order is necessary for some registrations
    static {
        // Fluids
        configs.add(new BloodConfig());
        
        // Blocks
        configs.add(new EvilBlockConfig());
        configs.add(new LiquidBlockBloodConfig());
        configs.add(new DarkOreConfig());
        configs.add(new DarkBlockConfig());
        
        // Items
        configs.add(new WerewolfBoneConfig());
        configs.add(new WerewolfFleshConfig());
        configs.add(new LightningGrenadeConfig());
        configs.add(new BucketBloodConfig());
        configs.add(new BloodExtractorConfig());
        configs.add(new DarkGemConfig());
        
        // Entities
        //Item
        configs.add(new EntityLightningGrenadeConfig());
        // Monster
        configs.add(new WerewolfConfig()); // http://www.minecraftwiki.net/wiki/Resource_pack#pack.mcmeta
    }
    public static Map<Class<? extends Entity>, Render> renderers = new HashMap<Class<? extends Entity>, Render>();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLLog.log(Level.INFO, ""+Reference.MOD_NAME+" preInit()");
        
        // Save this instance, so we can use it later
        this._instance = this;
        
        // Run the ConfigHandler to make/read the config and fill in the game registry
        ConfigHandler.getInstance().handle(event);
    }
    
    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        FMLLog.log(Level.INFO, ""+Reference.MOD_NAME+" init()");
        
        GameRegistry.registerWorldGenerator(new EvilWorldGenerator());
    }
    
    @EventHandler
    public void postInit(FMLPreInitializationEvent event) {
        FMLLog.log(Level.INFO, ""+Reference.MOD_NAME+" postInit()");
        
        /*log("Bone: "+WerewolfBoneConfig._instance.ID);
        log("Flesh: "+WerewolfFleshConfig._instance.ID);
        log("EvilBlock: "+EvilBlockConfig._instance.ID);*/
        
        proxy.registerRenderers();
        Recipes.registerRecipes();
        
    }
    
    public static void log(String message) {
        log(message, Level.INFO);
    }
    
    public static void log(String message, Level level) {
        FMLLog.log(level, "["+Reference.MOD_NAME+"] " + message);
    }
}
