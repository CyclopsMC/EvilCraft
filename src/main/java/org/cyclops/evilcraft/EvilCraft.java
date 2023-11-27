package org.cyclops.evilcraft;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.infobook.IInfoBookRegistry;
import org.cyclops.cyclopscore.infobook.InfoBookRegistry;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.persist.world.GlobalCounters;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroomModifierRegistry;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;
import org.cyclops.evilcraft.api.degradation.IDegradationRegistry;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierActionRegistry;
import org.cyclops.evilcraft.block.*;
import org.cyclops.evilcraft.blockentity.*;
import org.cyclops.evilcraft.blockentity.tickaction.bloodchest.BloodChestRepairActionRegistry;
import org.cyclops.evilcraft.blockentity.tickaction.purifier.PurifierActionRegistry;
import org.cyclops.evilcraft.client.particle.*;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.broom.BroomModifierRegistry;
import org.cyclops.evilcraft.core.broom.BroomPartRegistry;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.cyclops.evilcraft.core.degradation.DegradationRegistry;
import org.cyclops.evilcraft.core.degradation.effect.BiomeDegradationConfig;
import org.cyclops.evilcraft.core.degradation.effect.KnockbackDistortDegradationConfig;
import org.cyclops.evilcraft.core.degradation.effect.MobSpawnDegradationConfig;
import org.cyclops.evilcraft.core.degradation.effect.NauseateDegradationConfig;
import org.cyclops.evilcraft.core.degradation.effect.PalingDegradationConfig;
import org.cyclops.evilcraft.core.degradation.effect.ParticleDegradationConfig;
import org.cyclops.evilcraft.core.degradation.effect.SoundDegradationConfig;
import org.cyclops.evilcraft.core.degradation.effect.TerraformDegradationConfig;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.recipe.type.*;
import org.cyclops.evilcraft.enchantment.EnchantmentBreakingConfig;
import org.cyclops.evilcraft.enchantment.EnchantmentLifeStealingConfig;
import org.cyclops.evilcraft.enchantment.EnchantmentPoisonTipConfig;
import org.cyclops.evilcraft.enchantment.EnchantmentUnusingConfig;
import org.cyclops.evilcraft.enchantment.EnchantmentVengeanceConfig;
import org.cyclops.evilcraft.entity.block.EntityLightningBombPrimedConfig;
import org.cyclops.evilcraft.entity.effect.EntityAntiVengeanceBeamConfig;
import org.cyclops.evilcraft.entity.effect.EntityAttackVengeanceBeamConfig;
import org.cyclops.evilcraft.entity.effect.EntityNecromancersHeadConfig;
import org.cyclops.evilcraft.entity.item.EntityBiomeExtractConfig;
import org.cyclops.evilcraft.entity.item.EntityBloodPearlConfig;
import org.cyclops.evilcraft.entity.item.EntityBroomConfig;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStickConfig;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerableConfig;
import org.cyclops.evilcraft.entity.item.EntityItemUndespawnableConfig;
import org.cyclops.evilcraft.entity.item.EntityLightningGrenadeConfig;
import org.cyclops.evilcraft.entity.item.EntityRedstoneGrenadeConfig;
import org.cyclops.evilcraft.entity.item.EntityWeatherContainerConfig;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombieConfig;
import org.cyclops.evilcraft.entity.monster.EntityNetherfishConfig;
import org.cyclops.evilcraft.entity.monster.EntityPoisonousLibelleConfig;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritConfig;
import org.cyclops.evilcraft.entity.monster.EntityWerewolfConfig;
import org.cyclops.evilcraft.entity.villager.VillagerProfessionWerewolfConfig;
import org.cyclops.evilcraft.fluid.BloodConfig;
import org.cyclops.evilcraft.fluid.PoisonConfig;
import org.cyclops.evilcraft.infobook.OriginsOfDarknessBook;
import org.cyclops.evilcraft.inventory.container.ContainerBloodChestConfig;
import org.cyclops.evilcraft.inventory.container.ContainerBloodInfuserConfig;
import org.cyclops.evilcraft.inventory.container.ContainerColossalBloodChestConfig;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafterConfig;
import org.cyclops.evilcraft.inventory.container.ContainerOriginsOfDarknessConfig;
import org.cyclops.evilcraft.inventory.container.ContainerPrimedPendantConfig;
import org.cyclops.evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritFurnaceConfig;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritReanimatorConfig;
import org.cyclops.evilcraft.item.*;
import org.cyclops.evilcraft.loot.functions.LootFunctions;
import org.cyclops.evilcraft.loot.modifier.LootModifierInjectBoxOfEternalClosureConfig;
import org.cyclops.evilcraft.metadata.RegistryExportables;
import org.cyclops.evilcraft.modcompat.baubles.BaublesModCompat;
import org.cyclops.evilcraft.potion.PotionPalingConfig;
import org.cyclops.evilcraft.proxy.ClientProxy;
import org.cyclops.evilcraft.proxy.CommonProxy;
import org.cyclops.evilcraft.world.gen.feature.WorldFeatureEvilDungeonConfig;
import org.cyclops.evilcraft.world.gen.structure.WorldStructureDarkTempleConfig;

/**
 * The main mod class of EvilCraft.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class EvilCraft extends ModBaseVersionable<EvilCraft> {

    /**
     * The unique instance of this mod.
     */
    public static EvilCraft _instance;

    public static GlobalCounters globalCounters = null;

    public EvilCraft() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);

        // Register world storages
        registerWorldStorage(new WorldSharedTank.TankData(this));
        registerWorldStorage(globalCounters = new GlobalCounters(this));

        // Create registries
        getRegistryManager().addRegistry(IDegradationRegistry.class, new DegradationRegistry());
        getRegistryManager().addRegistry(IBloodChestRepairActionRegistry.class, new BloodChestRepairActionRegistry());
        getRegistryManager().addRegistry(IBroomPartRegistry.class, new BroomPartRegistry());
        getRegistryManager().addRegistry(IBroomModifierRegistry.class, new BroomModifierRegistry());
        getRegistryManager().addRegistry(IInfoBookRegistry.class, new InfoBookRegistry());
        getRegistryManager().addRegistry(IPurifierActionRegistry.class, new PurifierActionRegistry());

        // Register sounds
        if (MinecraftHelpers.isClientSide()) {
            FMLJavaModLoadingContext.get().getModEventBus().register(EvilCraftSoundEvents.class);
        }

        BroomParts.init();
        BroomModifiers.init();
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        // Mod compats
        modCompatLoader.addModCompat(new BaublesModCompat());
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        super.setup(event);

        Advancements.load();
        LootFunctions.load();
        RegistryExportables.load();

        // Initialize info book
        getRegistryManager().getRegistry(IInfoBookRegistry.class).registerInfoBook(
                OriginsOfDarknessBook.getInstance(), "/data/" + Reference.MOD_ID + "/info/book.xml");
    }

    @Override
    protected CreativeModeTab.Builder constructDefaultCreativeModeTab(CreativeModeTab.Builder builder) {
        return super.constructDefaultCreativeModeTab(builder)
                .icon(() -> new ItemStack(RegistryEntries.ITEM_DARK_GEM));
    }

    @Override
    protected void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig());

        // Particles
        configHandler.addConfigurable(new ParticleBubbleExtendedConfig());
        configHandler.addConfigurable(new ParticleBloodBubbleConfig());
        configHandler.addConfigurable(new ParticleBloodSplashConfig());
        configHandler.addConfigurable(new ParticleColoredSmokeConfig());
        configHandler.addConfigurable(new ParticleDarkSmokeConfig());
        configHandler.addConfigurable(new ParticleDegradeConfig());
        configHandler.addConfigurable(new ParticleDistortConfig());
        configHandler.addConfigurable(new ParticleExplosionExtendedConfig());
        configHandler.addConfigurable(new ParticleFartConfig());
        configHandler.addConfigurable(new ParticleMagicFinishConfig());
        configHandler.addConfigurable(new ParticleBlurTargettedConfig());
        configHandler.addConfigurable(new ParticleBlurTargettedEntityConfig());

        // Potion Effects
        configHandler.addConfigurable(new PotionPalingConfig());

        // Fluids
        configHandler.addConfigurable(new BloodConfig());
        configHandler.addConfigurable(new PoisonConfig());

        // Blocks
        configHandler.addConfigurable(new BlockFluidBloodConfig());
        configHandler.addConfigurable(new BlockDarkOreConfig(false));
        configHandler.addConfigurable(new BlockDarkOreConfig(true));
        configHandler.addConfigurable(new BlockDarkConfig());
        configHandler.addConfigurable(new org.cyclops.evilcraft.block.BlockBloodStainConfig());
        configHandler.addConfigurable(new BlockLightningBombConfig());
        //configHandler.addConfigurable(new ContainedFluxConfig());
        configHandler.addConfigurable(new org.cyclops.evilcraft.block.BlockBloodInfuserConfig());
        configHandler.addConfigurable(new BlockBloodyCobblestoneConfig());
        for (BlockInfestedNether.Type type : BlockInfestedNether.Type.values()) {
            configHandler.addConfigurable(new BlockInfestedNetherConfig(type));
        }
        configHandler.addConfigurable(new BlockHardenedBloodConfig());
        configHandler.addConfigurable(new BlockObscuredGlassConfig());
        configHandler.addConfigurable(new org.cyclops.evilcraft.block.BlockBloodChestConfig());
        configHandler.addConfigurable(new BlockEnvironmentalAccumulatorConfig());
        configHandler.addConfigurable(new BlockUndeadLeavesConfig());
        configHandler.addConfigurable(new BlockUndeadLogConfig());
        configHandler.addConfigurable(new BlockUndeadLogStrippedConfig());
        configHandler.addConfigurable(new BlockUndeadWoodConfig());
        configHandler.addConfigurable(new BlockUndeadWoodStrippedConfig());
        configHandler.addConfigurable(new BlockUndeadSlabConfig());
        configHandler.addConfigurable(new BlockUndeadFenceConfig());
        configHandler.addConfigurable(new BlockUndeadFenceGateConfig());
        configHandler.addConfigurable(new BlockUndeadSaplingConfig());
        configHandler.addConfigurable(new BlockUndeadPlankConfig());
        configHandler.addConfigurable(new BlockFluidPoisonConfig());
        configHandler.addConfigurable(new BlockInvisibleRedstoneConfig());
        configHandler.addConfigurable(new BlockPurifierConfig());
        configHandler.addConfigurable(new BlockDarkBrickConfig());
        configHandler.addConfigurable(new BlockDarkBloodBrickConfig());
        configHandler.addConfigurable(new BlockSpiritFurnaceConfig());
        configHandler.addConfigurable(new BlockDarkTankConfig());
        configHandler.addConfigurable(new BlockSanguinaryPedestalConfig(0));
        configHandler.addConfigurable(new BlockSanguinaryPedestalConfig(1));
        configHandler.addConfigurable(new BlockSpikedPlateConfig());
        configHandler.addConfigurable(new BlockSpiritReanimatorConfig());
        configHandler.addConfigurable(new BlockEntangledChaliceConfig());
        configHandler.addConfigurable(new BlockDarkPowerGemConfig());
        configHandler.addConfigurable(new BlockGemStoneTorchConfig());
        configHandler.addConfigurable(new BlockGemStoneTorchWallConfig());
        configHandler.addConfigurable(new BlockEternalWaterConfig());
        configHandler.addConfigurable(new BlockBloodWaxedCoalConfig());
        configHandler.addConfigurable(new BlockSpiritPortalConfig());
        configHandler.addConfigurable(new org.cyclops.evilcraft.block.BlockColossalBloodChestConfig());
        configHandler.addConfigurable(new BlockReinforcedUndeadPlankConfig());
        configHandler.addConfigurable(new BlockSanguinaryEnvironmentalAccumulatorConfig());
        configHandler.addConfigurable(new BlockDisplayStandConfig());
        configHandler.addConfigurable(new BlockUndeadPlankStairsConfig());
        configHandler.addConfigurable(new BlockReinforcedUndeadPlankStairsConfig());
        configHandler.addConfigurable(new BlockDarkBrickStairsConfig());
        configHandler.addConfigurable(new BlockDarkBloodBrickStairsConfig());

        // Items
        configHandler.addConfigurable(new ItemBucketBloodConfig());
        configHandler.addConfigurable(new ItemBucketPoisonConfig());
        configHandler.addConfigurable(new ItemWerewolfBoneConfig());
        configHandler.addConfigurable(new ItemWerewolfFleshConfig(false));
        configHandler.addConfigurable(new ItemWerewolfFleshConfig(true));
        configHandler.addConfigurable(new ItemLightningGrenadeConfig());
        configHandler.addConfigurable(new ItemRedstoneGrenadeConfig());
        configHandler.addConfigurable(new ItemBloodExtractorConfig());
        configHandler.addConfigurable(new ItemDarkGemConfig());
        configHandler.addConfigurable(new ItemDarkStickConfig());
        configHandler.addConfigurable(new ItemWeatherContainerConfig());
        configHandler.addConfigurable(new ItemBloodPearlOfTeleportationConfig());
        configHandler.addConfigurable(new ItemBroomConfig());
        configHandler.addConfigurable(new ItemHardenedBloodShardConfig());
        configHandler.addConfigurable(new ItemDarkPowerGemConfig());
        configHandler.addConfigurable(new ItemBloodInfusionCoreConfig());
        configHandler.addConfigurable(new ItemPoisonSacConfig());
        configHandler.addConfigurable(new ItemWerewolfFurConfig());
        configHandler.addConfigurable(new ItemBlookConfig());
        configHandler.addConfigurable(new ItemPotentiaSphereConfig());
        configHandler.addConfigurable(new ItemInvertedPotentiaConfig(false));
        configHandler.addConfigurable(new ItemInvertedPotentiaConfig(true));
        configHandler.addConfigurable(new ItemMaceOfDistortionConfig());
        configHandler.addConfigurable(new ItemKineticatorConfig(false));
        configHandler.addConfigurable(new ItemKineticatorConfig(true));
        configHandler.addConfigurable(new ItemVengeanceRingConfig());
        configHandler.addConfigurable(new ItemVengeanceFocusConfig());
        configHandler.addConfigurable(new ItemVengeancePickaxeConfig());
        configHandler.addConfigurable(new ItemBurningGemStoneConfig());
        configHandler.addConfigurable(new ItemDarkGemCrushedConfig());
        configHandler.addConfigurable(new ItemVeinSwordConfig());
        configHandler.addConfigurable(new ItemCreativeBloodDropConfig());
        configHandler.addConfigurable(new ItemDarkSpikeConfig());
        configHandler.addConfigurable(new ItemExaltedCrafterConfig(false, false));
        configHandler.addConfigurable(new ItemExaltedCrafterConfig(true, false));
        configHandler.addConfigurable(new ItemExaltedCrafterConfig(false, true));
        configHandler.addConfigurable(new ItemExaltedCrafterConfig(true, true));
        configHandler.addConfigurable(new ItemNecromancerStaffConfig());
        configHandler.addConfigurable(new ItemInvigoratingPendantConfig());
        configHandler.addConfigurable(new ItemCorruptedTearConfig());
        for (Upgrades.Upgrade upgrade : Upgrades.getUpgrades()) {
            configHandler.addConfigurable(new ItemPromiseConfig(upgrade));
        }
        for (ItemPromiseAcceptor.Type type : ItemPromiseAcceptor.Type.values()) {
            configHandler.addConfigurable(new ItemPromiseAcceptorConfig(type));
        }
        for (ItemBowlOfPromises.Type type : ItemBowlOfPromises.Type.values()) {
            configHandler.addConfigurable(new ItemBowlOfPromisesConfig(type));
        }
        configHandler.addConfigurable(new ItemDullDustConfig());
        configHandler.addConfigurable(new ItemBloodWaxedCoalConfig());
        configHandler.addConfigurable(new ItemEnderTearConfig());
        configHandler.addConfigurable(new ItemBloodPotashConfig());
        configHandler.addConfigurable(new ItemBucketEternalWaterConfig());
        configHandler.addConfigurable(new ItemBloodOrbConfig(false));
        configHandler.addConfigurable(new ItemBloodOrbConfig(true));
        configHandler.addConfigurable(new ItemSceptreOfThunderConfig());
        configHandler.addConfigurable(new ItemOriginsOfDarknessConfig());
        configHandler.addConfigurable(new ItemDarkenedAppleConfig());
        configHandler.addConfigurable(new ItemEffortlessRingConfig());
        configHandler.addConfigurable(new ItemCondensedBloodConfig());
        configHandler.addConfigurable(new ItemRejuvenatedFleshConfig());
        configHandler.addConfigurable(new ItemPrimedPendantConfig());
        configHandler.addConfigurable(new ItemGoldenStringConfig());
        configHandler.addConfigurable(new ItemBiomeExtractConfig());
        configHandler.addConfigurable(new ItemEnvironmentalAccumulationCoreConfig());
        configHandler.addConfigurable(new ItemMaceOfDestructionConfig());
        configHandler.addConfigurable(new ItemGarmonboziaConfig());
        configHandler.addConfigurable(new ItemPoisonBottleConfig());
        configHandler.addConfigurable(new ItemBroomPartConfig());
        configHandler.addConfigurable(new ItemVengeanceEssenceConfig());
        configHandler.addConfigurable(new ItemVengeanceEssenceMaterializedConfig());
        configHandler.addConfigurable(new ItemPiercingVengeanceFocusConfig());
        configHandler.addConfigurable(new ItemSpikeyClawsConfig());
        configHandler.addConfigurable(new ItemSpectralGlassesConfig());

        // Tile Entities
        configHandler.addConfigurable(new BlockEntityBloodChestConfig());
        configHandler.addConfigurable(new BlockEntityBloodInfuserConfig());
        configHandler.addConfigurable(new BlockEntityBloodStainConfig());
        configHandler.addConfigurable(new BlockEntityBoxOfEternalClosureConfig());
        configHandler.addConfigurable(new BlockEntityColossalBloodChestConfig());
        configHandler.addConfigurable(new BlockEntityDarkTankConfig());
        configHandler.addConfigurable(new BlockEntityDisplayStandConfig());
        configHandler.addConfigurable(new BlockEntityEntangledChaliceConfig());
        configHandler.addConfigurable(new BlockEntityEnvironmentalAccumulatorConfig());
        configHandler.addConfigurable(new BlockEntityEternalWaterConfig());
        configHandler.addConfigurable(new BlockEntityInvisibleRedstoneConfig());
        configHandler.addConfigurable(new BlockEntityPurifierConfig());
        configHandler.addConfigurable(new BlockEntitySanguinaryEnvironmentalAccumulatorConfig());
        configHandler.addConfigurable(new BlockEntitySanguinaryPedestalConfig());
        configHandler.addConfigurable(new BlockEntitySpiritFurnaceConfig());
        configHandler.addConfigurable(new BlockEntitySpiritPortalConfig());
        configHandler.addConfigurable(new BlockEntitySpiritReanimatorConfig());

        // Entities
        // Item
        configHandler.addConfigurable(new EntityLightningGrenadeConfig());
        configHandler.addConfigurable(new EntityRedstoneGrenadeConfig());
        configHandler.addConfigurable(new EntityBloodPearlConfig());
        configHandler.addConfigurable(new EntityBroomConfig());
        configHandler.addConfigurable(new EntityWeatherContainerConfig());
        configHandler.addConfigurable(new EntityItemEmpowerableConfig());
        configHandler.addConfigurable(new EntityItemUndespawnableConfig());
        configHandler.addConfigurable(new EntityItemDarkStickConfig());
        configHandler.addConfigurable(new EntityBiomeExtractConfig());
        // Block
        configHandler.addConfigurable(new EntityLightningBombPrimedConfig());
        // Monster
        configHandler.addConfigurable(new EntityWerewolfConfig());
        configHandler.addConfigurable(new EntityNetherfishConfig());
        configHandler.addConfigurable(new EntityPoisonousLibelleConfig());
        configHandler.addConfigurable(new EntityVengeanceSpiritConfig());
        configHandler.addConfigurable(new org.cyclops.evilcraft.block.BlockBoxOfEternalClosureConfig()); // This is a block, but depends on vengeance spirit.
        configHandler.addConfigurable(new EntityControlledZombieConfig());
        // Villager
        configHandler.addConfigurable(new VillagerProfessionWerewolfConfig());
        // Other
        configHandler.addConfigurable(new EntityAntiVengeanceBeamConfig());
        configHandler.addConfigurable(new EntityNecromancersHeadConfig());
        configHandler.addConfigurable(new EntityAttackVengeanceBeamConfig());

        // Enchantments
        configHandler.addConfigurable(new EnchantmentUnusingConfig());
        configHandler.addConfigurable(new EnchantmentBreakingConfig());
        configHandler.addConfigurable(new EnchantmentLifeStealingConfig());
        configHandler.addConfigurable(new EnchantmentPoisonTipConfig());
        configHandler.addConfigurable(new EnchantmentVengeanceConfig());

        // Degradation Effects
        configHandler.addConfigurable(new BiomeDegradationConfig());
        configHandler.addConfigurable(new KnockbackDistortDegradationConfig());
        configHandler.addConfigurable(new MobSpawnDegradationConfig());
        configHandler.addConfigurable(new NauseateDegradationConfig());
        configHandler.addConfigurable(new ParticleDegradationConfig());
        configHandler.addConfigurable(new SoundDegradationConfig());
        configHandler.addConfigurable(new TerraformDegradationConfig());
        configHandler.addConfigurable(new PalingDegradationConfig());

        // GUIs
        configHandler.addConfigurable(new ContainerBloodChestConfig());
        configHandler.addConfigurable(new ContainerBloodInfuserConfig());
        configHandler.addConfigurable(new ContainerColossalBloodChestConfig());
        configHandler.addConfigurable(new ContainerExaltedCrafterConfig());
        configHandler.addConfigurable(new ContainerOriginsOfDarknessConfig());
        configHandler.addConfigurable(new ContainerPrimedPendantConfig());
        configHandler.addConfigurable(new ContainerSanguinaryEnvironmentalAccumulatorConfig());
        configHandler.addConfigurable(new ContainerSpiritFurnaceConfig());
        configHandler.addConfigurable(new ContainerSpiritReanimatorConfig());

        // Recipe types
        configHandler.addConfigurable(new RecipeTypeBloodInfuserConfig());
        configHandler.addConfigurable(new RecipeTypeEnvironmentalAccumulatorConfig());

        // Recipes
        configHandler.addConfigurable(new RecipeSerializerBloodInfuserConfig());
        configHandler.addConfigurable(new RecipeSerializerEnvironmentalAccumulatorConfig());
        configHandler.addConfigurable(new RecipeSerializerBloodExtractorCombinationConfig());
        configHandler.addConfigurable(new RecipeSerializerFluidContainerCombinationConfig());
        configHandler.addConfigurable(new RecipeSerializerCraftingShapedCustomOutputDisplayStandConfig());
        configHandler.addConfigurable(new RecipeSerializerDeadBushConfig());
        configHandler.addConfigurable(new RecipeSerializerBroomPartCombinationConfig());
        configHandler.addConfigurable(new RecipeSerializerCraftingShapedCustomOutputVengeancePickaxeConfig());
        configHandler.addConfigurable(new RecipeSerializerCraftingShapedCustomOutputBoxOfEternalClosureConfig());
        configHandler.addConfigurable(new RecipeSerializerCraftingShapedCustomOutputVeinSwordConfig());
        configHandler.addConfigurable(new RecipeSerializerCraftingShapedCustomOutputDarkTankLargeConfig());
        configHandler.addConfigurable(new RecipeSerializerCraftingShapedCustomOutputEntangledChaliceNewConfig());
        configHandler.addConfigurable(new RecipeSerializerCraftingShapedCustomOutputEntangledChaliceCopyConfig());
        configHandler.addConfigurable(new RecipeSerializerEnvironmentalAccumulatorBiomeExtractConfig());

        // Features
        configHandler.addConfigurable(new WorldFeatureEvilDungeonConfig());
        configHandler.addConfigurable(new WorldStructureDarkTempleConfig());

        // Loot modifiers
        configHandler.addConfigurable(new LootModifierInjectBoxOfEternalClosureConfig());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected IClientProxy constructClientProxy() {
        return new ClientProxy();
    }

    @Override
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxy();
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
