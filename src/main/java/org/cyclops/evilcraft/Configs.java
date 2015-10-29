package org.cyclops.evilcraft;

import org.cyclops.evilcraft.block.ColossalBloodChestConfig;
import org.cyclops.evilcraft.block.ReinforcedUndeadPlankConfig;
import org.cyclops.evilcraft.block.SanguinaryEnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.entity.monster.*;
import org.cyclops.evilcraft.item.CondensedBloodConfig;
import org.cyclops.evilcraft.item.RejuvenatedFleshConfig;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.block.*;
import org.cyclops.evilcraft.core.degradation.effect.*;
import org.cyclops.evilcraft.enchantment.EnchantmentBreakingConfig;
import org.cyclops.evilcraft.enchantment.EnchantmentLifeStealingConfig;
import org.cyclops.evilcraft.enchantment.EnchantmentPoisonTipConfig;
import org.cyclops.evilcraft.enchantment.EnchantmentUnusingConfig;
import org.cyclops.evilcraft.entity.block.EntityLightningBombPrimedConfig;
import org.cyclops.evilcraft.entity.effect.EntityAntiVengeanceBeamConfig;
import org.cyclops.evilcraft.entity.effect.EntityNecromancersHeadConfig;
import org.cyclops.evilcraft.entity.item.*;
import org.cyclops.evilcraft.entity.villager.WerewolfVillagerConfig;
import org.cyclops.evilcraft.fluid.BloodConfig;
import org.cyclops.evilcraft.fluid.PoisonConfig;
import org.cyclops.evilcraft.item.*;
import org.cyclops.evilcraft.potion.PotionPalingConfig;
import org.cyclops.evilcraft.world.biome.BiomeDegradedConfig;

/**
 * This class holds a set of all the configs that need to be registered.
 * @author rubensworks
 *
 */
public class Configs {

    public static void registerBlocks(ConfigHandler configs) {

        // Potion Effects
        configs.add(new PotionPalingConfig());

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
        configs.add(new EntangledChaliceConfig());
        configs.add(new DarkPowerGemBlockConfig());
        configs.add(new GemStoneTorchConfig());
        configs.add(new EternalWaterBlockConfig());
        configs.add(new BloodWaxedCoalBlockConfig());
        configs.add(new SpiritPortalConfig());
        configs.add(new ColossalBloodChestConfig());
        configs.add(new ReinforcedUndeadPlankConfig());
        configs.add(new SanguinaryEnvironmentalAccumulatorConfig());
        
        // Items
        configs.add(new WerewolfBoneConfig());
        configs.add(new WerewolfFleshConfig());
        configs.add(new LightningGrenadeConfig());
        configs.add(new RedstoneGrenadeConfig());
        configs.add(new BucketBloodConfig());
        configs.add(new BloodExtractorConfig());
        configs.add(new DarkGemConfig());
        configs.add(new DarkStickConfig());
        configs.add(new WeatherContainerConfig());        
        configs.add(new BloodPearlOfTeleportationConfig());
        configs.add(new BroomConfig());
        configs.add(new HardenedBloodShardConfig());
        configs.add(new DarkPowerGemConfig());
        configs.add(new BloodInfusionCoreConfig());
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
        configs.add(new CorruptedTearConfig());
        configs.add(new PromiseConfig());
        configs.add(new PromiseAcceptorConfig());
        configs.add(new BowlOfPromisesConfig());
        configs.add(new DullDustConfig());
        configs.add(new BloodWaxedCoalConfig());
        configs.add(new EnderTearConfig());
        configs.add(new BloodPotashConfig());
        configs.add(new BucketEternalWaterConfig());
        configs.add(new BloodOrbConfig());
        configs.add(new SceptreOfThunderConfig());
        configs.add(new OriginsOfDarknessConfig());
        configs.add(new DarkenedAppleConfig());
        configs.add(new EffortlessRingConfig());
        configs.add(new CondensedBloodConfig());
        configs.add(new RejuvenatedFleshConfig());
        configs.add(new PrimedPendantConfig());
        configs.add(new GoldenStringConfig());
        configs.add(new BiomeExtractConfig());
        configs.add(new EnvironmentalAccumulationCoreConfig());
        configs.add(new MaceOfDestructionConfig());
        configs.add(new GarmonboziaConfig());
        configs.add(new PoisonBottleConfig());
        
        // Entities
        // Item
        configs.add(new EntityLightningGrenadeConfig());
        configs.add(new EntityRedstoneGrenadeConfig());
        configs.add(new EntityBloodPearlConfig());
        configs.add(new EntityBroomConfig());
        configs.add(new EntityWeatherContainerConfig());
        configs.add(new EntityItemEmpowerableConfig());
        configs.add(new EntityItemUndespawnableConfig());
        configs.add(new EntityItemDarkStickConfig());
        configs.add(new EntityBiomeExtractConfig());
        // Block
        configs.add(new EntityLightningBombPrimedConfig());
        // Monster
        configs.add(new WerewolfConfig());
        configs.add(new NetherfishConfig());
        configs.add(new PoisonousLibelleConfig());
        configs.add(new VengeanceSpiritConfig());
        configs.add(new BoxOfEternalClosureConfig()); // This is a block, but depends on vengeance spirit.
        configs.add(new ControlledZombieConfig());
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
        configs.add(new PalingDegradationConfig());
    }

    /**
     * Register ore dictionary keys for vanilla items/blocks.
     */
    public static void registerVanillaDictionary() {
        OreDictionary.registerOre(Reference.DICT_BLOCKGLASS, new ItemStack(Blocks.glass));
        OreDictionary.registerOre(Reference.DICT_MATERIALPOISONOUS, new ItemStack(Items.poisonous_potato));
        OreDictionary.registerOre(Reference.DICT_MATERIALBONE, new ItemStack(Items.bone));
        OreDictionary.registerOre(Reference.DICT_ITEMSKULL, new ItemStack(Items.skull, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre(Reference.DICT_FLESH, new ItemStack(Items.rotten_flesh, 1, OreDictionary.WILDCARD_VALUE));
    }

    /**
     * A safe way to check if a {@link org.cyclops.cyclopscore.config.configurable.IConfigurable} is enabled. @see ExtendedConfig#isEnabled()
     * @param config The config to check.
     * @return If the given config is enabled.
     */
    public static boolean isEnabled(Class<? extends ExtendedConfig> config) {
        return ConfigHandler.isEnabled(config);
    }
    
}
