package evilcraft;

import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.biomes.BiomeDegradedConfig;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.blocks.BloodStainedBlockConfig;
import evilcraft.blocks.BloodyCobblestoneConfig;
import evilcraft.blocks.BoxOfEternalClosureConfig;
import evilcraft.blocks.DarkBlockConfig;
import evilcraft.blocks.DarkBloodBrickConfig;
import evilcraft.blocks.DarkBrickConfig;
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
import evilcraft.blocks.SpiritFurnaceConfig;
import evilcraft.blocks.UndeadLeavesConfig;
import evilcraft.blocks.UndeadLogConfig;
import evilcraft.blocks.UndeadPlankConfig;
import evilcraft.blocks.UndeadSaplingConfig;
import evilcraft.core.config.ConfigHandler;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.configurable.Configurable;
import evilcraft.core.degradation.effects.BiomeDegradationConfig;
import evilcraft.core.degradation.effects.KnockbackDistortDegradationConfig;
import evilcraft.core.degradation.effects.MobSpawnDegradationConfig;
import evilcraft.core.degradation.effects.NauseateDegradationConfig;
import evilcraft.core.degradation.effects.ParticleDegradationConfig;
import evilcraft.core.degradation.effects.SoundDegradationConfig;
import evilcraft.core.degradation.effects.TerraformDegradationConfig;
import evilcraft.enchantment.EnchantmentBreakingConfig;
import evilcraft.enchantment.EnchantmentLifeStealingConfig;
import evilcraft.enchantment.EnchantmentPoisonTipConfig;
import evilcraft.enchantment.EnchantmentUnusingConfig;
import evilcraft.entities.block.EntityLightningBombPrimedConfig;
import evilcraft.entities.effect.EntityAntiVengeanceBeamConfig;
import evilcraft.entities.item.EntityBloodPearlConfig;
import evilcraft.entities.item.EntityBroomConfig;
import evilcraft.entities.item.EntityLightningGrenadeConfig;
import evilcraft.entities.item.EntityRedstoneGrenadeConfig;
import evilcraft.entities.item.EntityWeatherContainerConfig;
import evilcraft.entities.monster.NetherfishConfig;
import evilcraft.entities.monster.PoisonousLibelleConfig;
import evilcraft.entities.monster.VengeanceSpiritConfig;
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
import evilcraft.items.BurningGemStoneConfig;
import evilcraft.items.ContainedFluxConfig;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.DarkGemCrushedConfig;
import evilcraft.items.DarkPowerGemConfig;
import evilcraft.items.DarkStickConfig;
import evilcraft.items.HardenedBloodShardConfig;
import evilcraft.items.InvertedPotentiaConfig;
import evilcraft.items.KineticatorConfig;
import evilcraft.items.LargeDoorItemConfig;
import evilcraft.items.LightningGrenadeConfig;
import evilcraft.items.MaceOfDistortionConfig;
import evilcraft.items.PoisonSacConfig;
import evilcraft.items.PotentiaSphereConfig;
import evilcraft.items.RedstoneGrenadeConfig;
import evilcraft.items.VeinSwordConfig;
import evilcraft.items.VengeanceFocusConfig;
import evilcraft.items.VengeancePickaxeConfig;
import evilcraft.items.VengeanceRingConfig;
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
        OreDictionary.registerOre(Reference.DICT_MATERIALGLASS, new ItemStack(Blocks.glass));
        OreDictionary.registerOre(Reference.DICT_MATERIALPOISONOUS, new ItemStack(Items.poisonous_potato));
        OreDictionary.registerOre(Reference.DICT_MATERIALBONE, new ItemStack(Items.bone));
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
        configs.add(new DarkBrickConfig());
        configs.add(new DarkBloodBrickConfig());
        configs.add(new SpiritFurnaceConfig());
        
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
        configs.add(new PotentiaSphereConfig());
        configs.add(new InvertedPotentiaConfig());
        configs.add(new MaceOfDistortionConfig());
        configs.add(new KineticatorConfig());
        configs.add(new VengeanceRingConfig());
        configs.add(new VengeanceFocusConfig());
        configs.add(new VengeancePickaxeConfig());
        configs.add(new BurningGemStoneConfig());
        configs.add(new DarkGemCrushedConfig());
        configs.add(new VeinSwordConfig());
        
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
        configs.add(new VengeanceSpiritConfig());
        configs.add(new BoxOfEternalClosureConfig()); // This is a block, but depends on vengeance spirit.
        // Villager
        configs.add(new WerewolfVillagerConfig());
        // Other
        configs.add(new EntityAntiVengeanceBeamConfig());
        
        // Enchantments
        configs.add(new EnchantmentUnusingConfig());
        configs.add(new EnchantmentBreakingConfig());
        configs.add(new EnchantmentLifeStealingConfig());
        configs.add(new EnchantmentPoisonTipConfig());
        
        // Biomes
        configs.add(new BiomeDegradedConfig());
        
        // Degradation Effects
        configs.add(new BiomeDegradationConfig());
        configs.add(new KnockbackDistortDegradationConfig());
        configs.add(new MobSpawnDegradationConfig());
        configs.add(new NauseateDegradationConfig());
        configs.add(new ParticleDegradationConfig());
        configs.add(new SoundDegradationConfig());
        configs.add(new TerraformDegradationConfig());
    }

    /**
     * A safe way to check if a {@link Configurable} is enabled. @see ExtendedConfig#isEnabled()
     * @param config The config to check.
     * @return If the given config is enabled.
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEnabled(Class<? extends ExtendedConfig> config) {
        try {
            return ((ExtendedConfig)config.getField("_instance").get(null)).isEnabled();
        } catch (NullPointerException e1) {
            return false;
        } catch (IllegalArgumentException e2) {
            return false;
        } catch (IllegalAccessException e3) {
            return false;
        } catch (NoSuchFieldException e3) {
            return false;
        } catch (SecurityException e4) {
            return false;
        }
    }
    
}
