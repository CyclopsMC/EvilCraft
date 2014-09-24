package evilcraft;

import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.block.BloodChestConfig;
import evilcraft.block.BloodInfuserConfig;
import evilcraft.block.BloodStainedBlockConfig;
import evilcraft.block.BloodyCobblestoneConfig;
import evilcraft.block.BoxOfEternalClosureConfig;
import evilcraft.block.DarkBlockConfig;
import evilcraft.block.DarkBloodBrickConfig;
import evilcraft.block.DarkBrickConfig;
import evilcraft.block.DarkOreConfig;
import evilcraft.block.DarkTankConfig;
import evilcraft.block.EnvironmentalAccumulatorConfig;
import evilcraft.block.EvilBlockConfig;
import evilcraft.block.ExcrementPileConfig;
import evilcraft.block.FluidBlockBloodConfig;
import evilcraft.block.FluidBlockPoisonConfig;
import evilcraft.block.HardenedBloodConfig;
import evilcraft.block.InvisibleRedstoneBlockConfig;
import evilcraft.block.LargeDoorConfig;
import evilcraft.block.LightningBombConfig;
import evilcraft.block.NetherfishSpawnConfig;
import evilcraft.block.ObscuredGlassConfig;
import evilcraft.block.PurifierConfig;
import evilcraft.block.SanguinaryPedestalConfig;
import evilcraft.block.SpikedPlateConfig;
import evilcraft.block.SpiritFurnaceConfig;
import evilcraft.block.SpiritReanimatorConfig;
import evilcraft.block.UndeadLeavesConfig;
import evilcraft.block.UndeadLogConfig;
import evilcraft.block.UndeadPlankConfig;
import evilcraft.block.UndeadSaplingConfig;
import evilcraft.core.config.ConfigHandler;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.degradation.effect.BiomeDegradationConfig;
import evilcraft.core.degradation.effect.KnockbackDistortDegradationConfig;
import evilcraft.core.degradation.effect.MobSpawnDegradationConfig;
import evilcraft.core.degradation.effect.NauseateDegradationConfig;
import evilcraft.core.degradation.effect.ParticleDegradationConfig;
import evilcraft.core.degradation.effect.SoundDegradationConfig;
import evilcraft.core.degradation.effect.TerraformDegradationConfig;
import evilcraft.enchantment.EnchantmentBreakingConfig;
import evilcraft.enchantment.EnchantmentLifeStealingConfig;
import evilcraft.enchantment.EnchantmentPoisonTipConfig;
import evilcraft.enchantment.EnchantmentUnusingConfig;
import evilcraft.entity.block.EntityLightningBombPrimedConfig;
import evilcraft.entity.effect.EntityAntiVengeanceBeamConfig;
import evilcraft.entity.effect.EntityNecromancersHeadConfig;
import evilcraft.entity.item.EntityBloodPearlConfig;
import evilcraft.entity.item.EntityBroomConfig;
import evilcraft.entity.item.EntityLightningGrenadeConfig;
import evilcraft.entity.item.EntityRedstoneGrenadeConfig;
import evilcraft.entity.item.EntityWeatherContainerConfig;
import evilcraft.entity.monster.NetherfishConfig;
import evilcraft.entity.monster.PoisonousLibelleConfig;
import evilcraft.entity.monster.VengeanceSpiritConfig;
import evilcraft.entity.monster.WerewolfConfig;
import evilcraft.entity.villager.WerewolfVillagerConfig;
import evilcraft.fluid.BloodConfig;
import evilcraft.fluid.PoisonConfig;
import evilcraft.item.BloodContainerConfig;
import evilcraft.item.BloodExtractorConfig;
import evilcraft.item.BloodInfusionCoreConfig;
import evilcraft.item.BloodPearlOfTeleportationConfig;
import evilcraft.item.BlookConfig;
import evilcraft.item.BroomConfig;
import evilcraft.item.BucketBloodConfig;
import evilcraft.item.BucketPoisonConfig;
import evilcraft.item.BurningGemStoneConfig;
import evilcraft.item.ContainedFluxConfig;
import evilcraft.item.CreativeBloodDropConfig;
import evilcraft.item.DarkGemConfig;
import evilcraft.item.DarkGemCrushedConfig;
import evilcraft.item.DarkPowerGemConfig;
import evilcraft.item.DarkSpikeConfig;
import evilcraft.item.DarkStickConfig;
import evilcraft.item.ExaltedCrafterConfig;
import evilcraft.item.HardenedBloodShardConfig;
import evilcraft.item.InvertedPotentiaConfig;
import evilcraft.item.InvigoratingPendantConfig;
import evilcraft.item.KineticatorConfig;
import evilcraft.item.LargeDoorItemConfig;
import evilcraft.item.LightningGrenadeConfig;
import evilcraft.item.MaceOfDistortionConfig;
import evilcraft.item.NecromancerStaffConfig;
import evilcraft.item.PoisonSacConfig;
import evilcraft.item.PotentiaSphereConfig;
import evilcraft.item.RedstoneGrenadeConfig;
import evilcraft.item.ResurgenceEggConfig;
import evilcraft.item.VeinSwordConfig;
import evilcraft.item.VengeanceFocusConfig;
import evilcraft.item.VengeancePickaxeConfig;
import evilcraft.item.VengeanceRingConfig;
import evilcraft.item.WeatherContainerConfig;
import evilcraft.item.WerewolfBoneConfig;
import evilcraft.item.WerewolfFleshConfig;
import evilcraft.item.WerewolfFurConfig;
import evilcraft.world.biome.BiomeDegradedConfig;

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
        OreDictionary.registerOre(Reference.DICT_BLOCKGLASS, new ItemStack(Blocks.glass));
        OreDictionary.registerOre(Reference.DICT_MATERIALPOISONOUS, new ItemStack(Items.poisonous_potato));
        OreDictionary.registerOre(Reference.DICT_MATERIALBONE, new ItemStack(Items.bone));
        OreDictionary.registerOre(Reference.DICT_ITEMSKULL, new ItemStack(Items.skull, 1, OreDictionary.WILDCARD_VALUE));
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
        configs.add(new DarkTankConfig());
        configs.add(new SanguinaryPedestalConfig());
        configs.add(new SpikedPlateConfig());
        configs.add(new SpiritReanimatorConfig());
        
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
        configs.add(new CreativeBloodDropConfig());
        configs.add(new DarkSpikeConfig());
        configs.add(new ExaltedCrafterConfig());
        configs.add(new NecromancerStaffConfig());
        configs.add(new InvigoratingPendantConfig());
        configs.add(new ResurgenceEggConfig());
        
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
        configs.add(new EntityNecromancersHeadConfig());
        
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
     * A safe way to check if a {@link IConfigurable} is enabled. @see ExtendedConfig#isEnabled()
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
