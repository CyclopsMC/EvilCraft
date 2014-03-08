package evilcraft;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.blocks.BloodStainedBlockConfig;
import evilcraft.blocks.BloodyCobblestoneConfig;
import evilcraft.blocks.DarkBlockConfig;
import evilcraft.blocks.DarkOreConfig;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.blocks.EvilBlockConfig;
import evilcraft.blocks.ExcrementPileConfig;
import evilcraft.blocks.FluidBlockBloodConfig;
import evilcraft.blocks.FluidBlockPoisonConfig;
import evilcraft.blocks.HardenedBloodConfig;
import evilcraft.blocks.InvisibleRedstoneBlockConfig;
import evilcraft.blocks.LargeDoorConfig;
import evilcraft.blocks.LightningBombConfig;
import evilcraft.blocks.NetherfishSpawnConfig;
import evilcraft.blocks.ObscuredGlassConfig;
import evilcraft.blocks.PurifierConfig;
import evilcraft.blocks.UndeadLeavesConfig;
import evilcraft.blocks.UndeadLogConfig;
import evilcraft.blocks.UndeadPlankConfig;
import evilcraft.blocks.UndeadSaplingConfig;
import evilcraft.enchantment.EnchantmentBreakingConfig;
import evilcraft.enchantment.EnchantmentLifeStealingConfig;
import evilcraft.enchantment.EnchantmentPoisonTipConfig;
import evilcraft.enchantment.EnchantmentUnusingConfig;
import evilcraft.entities.block.EntityLightningBombPrimedConfig;
import evilcraft.entities.item.EntityBloodPearlConfig;
import evilcraft.entities.item.EntityBroomConfig;
import evilcraft.entities.item.EntityLightningGrenadeConfig;
import evilcraft.entities.item.EntityRedstoneGrenadeConfig;
import evilcraft.entities.item.EntityWeatherContainerConfig;
import evilcraft.entities.monster.NetherfishConfig;
import evilcraft.entities.monster.PoisonousLibelleConfig;
import evilcraft.entities.monster.WerewolfConfig;
import evilcraft.entities.villager.WerewolfVillagerConfig;
import evilcraft.fluids.BloodConfig;
import evilcraft.fluids.PoisonConfig;
import evilcraft.items.BloodContainerConfig;
import evilcraft.items.BloodExtractorConfig;
import evilcraft.items.BloodInfusionCoreConfig;
import evilcraft.items.BloodPearlOfTeleportationConfig;
import evilcraft.items.BlookConfig;
import evilcraft.items.BroomConfig;
import evilcraft.items.BucketBloodConfig;
import evilcraft.items.BucketPoisonConfig;
import evilcraft.items.ContainedFluxConfig;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.DarkPowerGemConfig;
import evilcraft.items.DarkStickConfig;
import evilcraft.items.HardenedBloodShardConfig;
import evilcraft.items.LargeDoorItemConfig;
import evilcraft.items.LightningGrenadeConfig;
import evilcraft.items.PoisonSacConfig;
import evilcraft.items.RedstoneGrenadeConfig;
import evilcraft.items.WeatherContainerConfig;
import evilcraft.items.WerewolfBoneConfig;
import evilcraft.items.WerewolfFleshConfig;
import evilcraft.items.WerewolfFurConfig;

/**
 * This class holds a set of all the configs that need to be registered.
 * @author rubensworks
 *
 */
public class Configs {
    
    private static Configs _instance;
    
    /**
     * The set of configs.
     */
    @SuppressWarnings("rawtypes")
    public Set<ExtendedConfig> configs = ConfigHandler.getInstance(); // Order is necessary for some registrations
    
    /**
     * Get the unique instance.
     * @return Unique instance.
     */
    public static Configs getInstance() {
        if(_instance == null)
            _instance = new Configs();
        return _instance;
    }
    
    private Configs() {
        
    }
    
    /**
     * Register the general configs. They won't be checked with the config debugger.
     */
    public void registerGeneralConfigs() {
        // General
        configs.add(new GeneralConfig());
    }
    
    /**
     * Register ore dictionary keys for vanilla items/blocks.
     */
    public void registerVanillaDictionary() {
        OreDictionary.registerOre(Reference.DICT_MATERIALGLASS, new ItemStack(Block.glass));
        OreDictionary.registerOre(Reference.DICT_MATERIALPOISONOUS, new ItemStack(Item.poisonousPotato));
        OreDictionary.registerOre(Reference.DICT_MATERIALBONE, new ItemStack(Item.bone));
    }
    
    /**
     * Register all the configs.
     */
    public void registerConfigs() {        
        // Fluids
        configs.add(new BloodConfig());
        configs.add(new PoisonConfig());
        
        // Blocks
        configs.add(new EvilBlockConfig());
        configs.add(new FluidBlockBloodConfig());
        configs.add(new DarkOreConfig());
        configs.add(new DarkBlockConfig());
        configs.add(new BloodStainedBlockConfig());
        configs.add(new LightningBombConfig());
        configs.add(new LargeDoorConfig());
        configs.add(new ContainedFluxConfig());
        configs.add(new BloodInfuserConfig());
        configs.add(new BloodyCobblestoneConfig());
        configs.add(new NetherfishSpawnConfig());
        configs.add(new ExcrementPileConfig());
        configs.add(new HardenedBloodConfig());
        configs.add(new ObscuredGlassConfig());
        configs.add(new BloodChestConfig());
        configs.add(new EnvironmentalAccumulatorConfig());
        configs.add(new UndeadLeavesConfig());
        configs.add(new UndeadLogConfig());
        configs.add(new UndeadSaplingConfig());
        configs.add(new UndeadPlankConfig());
        configs.add(new FluidBlockPoisonConfig());
        configs.add(new InvisibleRedstoneBlockConfig());
        configs.add(new PurifierConfig());
        
        // Items
        configs.add(new WerewolfBoneConfig());
        configs.add(new WerewolfFleshConfig());
        configs.add(new LightningGrenadeConfig());
        configs.add(new RedstoneGrenadeConfig());
        configs.add(new BucketBloodConfig());
        configs.add(new BloodExtractorConfig());
        configs.add(new DarkGemConfig());
        configs.add(new DarkStickConfig());
        configs.add(new LargeDoorItemConfig());
        configs.add(new WeatherContainerConfig());        
        configs.add(new BloodPearlOfTeleportationConfig());
        configs.add(new BroomConfig());
        configs.add(new HardenedBloodShardConfig());
        configs.add(new DarkPowerGemConfig());
        configs.add(new BloodInfusionCoreConfig());
        configs.add(new BloodContainerConfig());
        configs.add(new PoisonSacConfig());
        configs.add(new BucketPoisonConfig());
        configs.add(new WerewolfFurConfig());
        configs.add(new BlookConfig());
        
        // Entities
        // Item
        configs.add(new EntityLightningGrenadeConfig());
        configs.add(new EntityRedstoneGrenadeConfig());
        configs.add(new EntityBloodPearlConfig());
        configs.add(new EntityBroomConfig());
        configs.add(new EntityWeatherContainerConfig());
        // Block
        configs.add(new EntityLightningBombPrimedConfig());
        // Monster
        configs.add(new WerewolfConfig());
        configs.add(new NetherfishConfig());
        configs.add(new PoisonousLibelleConfig());
        // Villager
        configs.add(new WerewolfVillagerConfig());
        // Other
        
        // Enchantments
        configs.add(new EnchantmentUnusingConfig());
        configs.add(new EnchantmentBreakingConfig());
        configs.add(new EnchantmentLifeStealingConfig());
        configs.add(new EnchantmentPoisonTipConfig());
    }
    
}
