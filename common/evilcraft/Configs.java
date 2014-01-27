package evilcraft;

import java.util.Set;

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
import evilcraft.blocks.HardenedBloodConfig;
import evilcraft.blocks.LargeDoorConfig;
import evilcraft.blocks.LightningBombConfig;
import evilcraft.blocks.NetherfishSpawnConfig;
import evilcraft.blocks.ObscuredGlassConfig;
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
import evilcraft.entities.monster.NetherfishConfig;
import evilcraft.entities.monster.WerewolfConfig;
import evilcraft.entities.villager.WerewolfVillagerConfig;
import evilcraft.fluids.BloodConfig;
import evilcraft.items.BloodContainerConfig;
import evilcraft.items.BloodExtractorConfig;
import evilcraft.items.BloodInfusionCoreConfig;
import evilcraft.items.BloodPearlOfTeleportationConfig;
import evilcraft.items.BroomConfig;
import evilcraft.items.BucketBloodConfig;
import evilcraft.items.ContainedFluxConfig;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.DarkPowerGemConfig;
import evilcraft.items.DarkStickConfig;
import evilcraft.items.HardenedBloodShardConfig;
import evilcraft.items.LargeDoorItemConfig;
import evilcraft.items.LightningGrenadeConfig;
import evilcraft.items.WeatherContainerConfig;
import evilcraft.items.WerewolfBoneConfig;
import evilcraft.items.WerewolfFleshConfig;
import evilcraft.items.WerewolfFurConfig;

public class Configs {
    
    private static Configs _instance;
    
    public Set<ExtendedConfig> configs = ConfigHandler.getInstance(); // Order is necessary for some registrations
    
    public static Configs getInstance() {
        if(_instance == null)
            _instance = new Configs();
        return _instance;
    }
    
    private Configs() {
        
    }
    
    public void registerGeneralConfigs() {
     // General
        configs.add(new GeneralConfig());
    }
    
    public void registerConfigs() {        
        // Fluids
        configs.add(new BloodConfig());
        
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
        
        // Items
        configs.add(new WerewolfBoneConfig());
        configs.add(new WerewolfFleshConfig());
        configs.add(new LightningGrenadeConfig());
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
        configs.add(new WerewolfFurConfig());
        
        // Entities
        // Item
        configs.add(new EntityLightningGrenadeConfig());
        configs.add(new EntityBloodPearlConfig());
        // Block
        configs.add(new EntityLightningBombPrimedConfig());
        // Monster
        configs.add(new WerewolfConfig()); // http://www.minecraftwiki.net/wiki/Resource_pack#pack.mcmeta
        configs.add(new NetherfishConfig());
        // Villager
        configs.add(new WerewolfVillagerConfig());
        // Other
        configs.add(new EntityBroomConfig());
        
        // Enchantments
        configs.add(new EnchantmentUnusingConfig());
        configs.add(new EnchantmentBreakingConfig());
        configs.add(new EnchantmentLifeStealingConfig());
        configs.add(new EnchantmentPoisonTipConfig());
    }
    
}
