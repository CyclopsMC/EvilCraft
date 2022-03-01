package org.cyclops.evilcraft;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.registries.ObjectHolder;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.block.BlockColossalBloodChest;
import org.cyclops.evilcraft.block.BlockDarkBloodBrick;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.block.BlockReinforcedUndeadPlank;
import org.cyclops.evilcraft.block.BlockSpiritFurnace;
import org.cyclops.evilcraft.blockentity.*;
import org.cyclops.evilcraft.client.particle.ParticleBlurTargettedData;
import org.cyclops.evilcraft.client.particle.ParticleBlurTargettedEntityData;
import org.cyclops.evilcraft.client.particle.ParticleBubbleExtendedData;
import org.cyclops.evilcraft.client.particle.ParticleColoredSmokeData;
import org.cyclops.evilcraft.client.particle.ParticleDarkSmokeData;
import org.cyclops.evilcraft.client.particle.ParticleDistortData;
import org.cyclops.evilcraft.client.particle.ParticleExplosionExtendedData;
import org.cyclops.evilcraft.client.particle.ParticleFartData;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodExtractorCombination;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodInfuser;
import org.cyclops.evilcraft.core.recipe.type.RecipeBroomPartCombination;
import org.cyclops.evilcraft.core.recipe.type.RecipeDeadBush;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulatorBiomeExtract;
import org.cyclops.evilcraft.core.recipe.type.RecipeFluidContainerCombination;
import org.cyclops.evilcraft.entity.block.EntityLightningBombPrimed;
import org.cyclops.evilcraft.entity.effect.EntityAttackVengeanceBeam;
import org.cyclops.evilcraft.entity.effect.EntityNecromancersHead;
import org.cyclops.evilcraft.entity.item.EntityBiomeExtract;
import org.cyclops.evilcraft.entity.item.EntityBroom;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStick;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerable;
import org.cyclops.evilcraft.entity.item.EntityItemUndespawnable;
import org.cyclops.evilcraft.entity.item.EntityLightningGrenade;
import org.cyclops.evilcraft.entity.item.EntityRedstoneGrenade;
import org.cyclops.evilcraft.entity.item.EntityWeatherContainer;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombie;
import org.cyclops.evilcraft.entity.monster.EntityNetherfish;
import org.cyclops.evilcraft.entity.monster.EntityPoisonousLibelle;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.EntityWerewolf;
import org.cyclops.evilcraft.inventory.container.ContainerBloodChest;
import org.cyclops.evilcraft.inventory.container.ContainerBloodInfuser;
import org.cyclops.evilcraft.inventory.container.ContainerColossalBloodChest;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafter;
import org.cyclops.evilcraft.inventory.container.ContainerOriginsOfDarkness;
import org.cyclops.evilcraft.inventory.container.ContainerPrimedPendant;
import org.cyclops.evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritFurnace;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritReanimator;
import org.cyclops.evilcraft.item.ItemBiomeExtract;
import org.cyclops.evilcraft.item.ItemMaceOfDistortion;
import org.cyclops.evilcraft.item.ItemVeinSword;
import org.cyclops.evilcraft.item.ItemVengeancePickaxe;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    @ObjectHolder("evilcraft:blood_pearl_of_teleportation")
    public static final Item ITEM_BLOOD_PEARL_OF_TELEPORTATION = null;
    @ObjectHolder("evilcraft:blood_infusion_core")
    public static final Item ITEM_BLOOD_INFUSION_CORE = null;
    @ObjectHolder("evilcraft:broom")
    public static final Item ITEM_BROOM = null;
    @ObjectHolder("evilcraft:broom_part")
    public static final Item ITEM_BROOM_PART = null;
    @ObjectHolder("evilcraft:bucket_blood")
    public static final Item ITEM_BUCKET_BLOOD = null;
    @ObjectHolder("evilcraft:bucket_poison")
    public static final Item ITEM_BUCKET_POISON = null;
    @ObjectHolder("evilcraft:blook")
    public static final Item ITEM_BLOOK = null;
    @ObjectHolder("evilcraft:dark_gem")
    public static final Item ITEM_DARK_GEM = null;
    @ObjectHolder("evilcraft:dark_gem_crushed")
    public static final Item ITEM_DARK_GEM_CRUSHED = null;
    @ObjectHolder("evilcraft:dark_power_gem")
    public static final Item ITEM_DARK_POWER_GEM = null;
    @ObjectHolder("evilcraft:dark_tank")
    public static final Item ITEM_DARK_TANK = null;
    @ObjectHolder("evilcraft:dark_spike")
    public static final Item ITEM_DARK_SPIKE = null;
    @ObjectHolder("evilcraft:biome_extract")
    public static final ItemBiomeExtract ITEM_BIOME_EXTRACT = null;
    @ObjectHolder("evilcraft:bowl_of_promises_empty")
    public static final Item ITEM_BOWL_OF_PROMISES_EMPTY = null;
    @ObjectHolder("evilcraft:box_of_eternal_closure")
    public static final Item ITEM_BOX_OF_ETERNAL_CLOSURE = null;
    @ObjectHolder("evilcraft:blood_extractor")
    public static final Item ITEM_BLOOD_EXTRACTOR = null;
    @ObjectHolder("evilcraft:burning_gem_stone")
    public static final Item ITEM_BURNING_GEM_STONE = null;
    @ObjectHolder("evilcraft:entangled_chalice")
    public static final Item ITEM_ENTANGLED_CHALICE = null;
    @ObjectHolder("evilcraft:exalted_crafter")
    public static final Item ITEM_EXALTED_CRAFTER = null;
    @ObjectHolder("evilcraft:exalted_crafter_wooden")
    public static final Item ITEM_EXALTED_CRAFTER_WOODEN = null;
    @ObjectHolder("evilcraft:exalted_crafter_empowered")
    public static final Item ITEM_EXALTED_CRAFTER_EMPOWERED = null;
    @ObjectHolder("evilcraft:exalted_crafter_wooden_empowered")
    public static final Item ITEM_EXALTED_CRAFTER_WOODEN_EMPOWERED = null;
    @ObjectHolder("evilcraft:garmonbozia")
    public static final Item ITEM_GARMONBOZIA = null;
    @ObjectHolder("evilcraft:flesh_werewolf")
    public static final Item ITEM_FLESH_WEREWOLF = null;
    @ObjectHolder("evilcraft:flesh_werewolf")
    public static final Item ITEM_FLESH_HUMANOID = null;
    @ObjectHolder("evilcraft:hardened_blood_shard")
    public static final Item ITEM_HARDENED_BLOOD_SHARD = null;
    @ObjectHolder("evilcraft:mace_of_distortion")
    public static final ItemMaceOfDistortion ITEM_MACE_OF_DISTORTION = null;
    @ObjectHolder("evilcraft:promise_tier_1")
    public static final Item ITEM_PROMISE_TIER_1 = null;
    @ObjectHolder("evilcraft:promise_tier_2")
    public static final Item ITEM_PROMISE_TIER_2 = null;
    @ObjectHolder("evilcraft:promise_tier_3")
    public static final Item ITEM_PROMISE_TIER_3 = null;
    @ObjectHolder("evilcraft:promise_speed_0")
    public static final Item ITEM_PROMISE_SPEED = null;
    @ObjectHolder("evilcraft:promise_efficiency_0")
    public static final Item ITEM_PROMISE_EFFICIENCY = null;
    @ObjectHolder("evilcraft:inverted_potentia")
    public static final Item ITEM_INVERTED_POTENTIA = null;
    @ObjectHolder("evilcraft:inverted_potentia_empowered")
    public static final Item ITEM_INVERTED_POTENTIA_EMPOWERED = null;
    @ObjectHolder("evilcraft:lightning_grenade")
    public static final Item ITEM_LIGHTNING_GRENADE = null;
    @ObjectHolder("evilcraft:origins_of_darkness")
    public static final Item ITEM_ORIGINS_OF_DARKNESS = null;
    @ObjectHolder("evilcraft:poison_sac")
    public static final Item ITEM_POISON_SAC = null;
    @ObjectHolder("evilcraft:redstone_grenade")
    public static final Item ITEM_REDSTONE_GRENADE = null;
    @ObjectHolder("evilcraft:vein_sword")
    public static final ItemVeinSword ITEM_VEIN_SWORD = null;
    @ObjectHolder("evilcraft:undead_sapling")
    public static final Item ITEM_UNDEAD_SAPLING = null;
    @ObjectHolder("evilcraft:vengeance_focus")
    public static final Item ITEM_VENGEANCE_FOCUS = null;
    @ObjectHolder("evilcraft:vengeance_pickaxe")
    public static final ItemVengeancePickaxe ITEM_VENGEANCE_PICKAXE = null;
    @ObjectHolder("evilcraft:weather_container")
    public static final Item ITEM_WEATHER_CONTAINER = null;
    @ObjectHolder("evilcraft:werewolf_bone")
    public static final Item ITEM_WEREWOLF_BONE = null;
    @ObjectHolder("evilcraft:werewolf_fur")
    public static final Item ITEM_WEREWOLF_FUR = null;

    @ObjectHolder("evilcraft:blood")
    public static final LiquidBlock BLOCK_BLOOD = null;
    @ObjectHolder("evilcraft:blood_chest")
    public static final Block BLOCK_BLOOD_CHEST = null;
    @ObjectHolder("evilcraft:blood_infuser")
    public static final Block BLOCK_BLOOD_INFUSER = null;
    @ObjectHolder("evilcraft:blood_stain")
    public static final Block BLOCK_BLOOD_STAIN = null;
    @ObjectHolder("evilcraft:bloody_cobblestone")
    public static final Block BLOCK_BLOODY_COBBLESTONE = null;
    @ObjectHolder("evilcraft:box_of_eternal_closure")
    public static final Block BLOCK_BOX_OF_ETERNAL_CLOSURE = null;
    @ObjectHolder("evilcraft:colossal_blood_chest")
    public static final BlockColossalBloodChest BLOCK_COLOSSAL_BLOOD_CHEST = null;
    @ObjectHolder("evilcraft:dark_blood_brick")
    public static final BlockDarkBloodBrick BLOCK_DARK_BLOOD_BRICK = null;
    @ObjectHolder("evilcraft:dark_brick")
    public static final Block BLOCK_DARK_BRICK = null;
    @ObjectHolder("evilcraft:dark_tank")
    public static final org.cyclops.evilcraft.block.BlockDarkTank BLOCK_DARK_TANK = null;
    @ObjectHolder("evilcraft:display_stand")
    public static final BlockDisplayStand BLOCK_DISPLAY_STAND = null;
    @ObjectHolder("evilcraft:entangled_chalice")
    public static final Block BLOCK_ENTANGLED_CHALICE = null;
    @ObjectHolder("evilcraft:environmental_accumulator")
    public static final Block BLOCK_ENVIRONMENTAL_ACCUMULATOR = null;
    @ObjectHolder("evilcraft:eternal_water")
    public static final Block BLOCK_ETERNAL_WATER = null;
    @ObjectHolder("evilcraft:gem_stone_torch")
    public static final Block BLOCK_GEM_STONE_TORCH = null;
    @ObjectHolder("evilcraft:gem_stone_torch_wall")
    public static final Block BLOCK_GEM_STONE_TORCH_WALL = null;
    @ObjectHolder("evilcraft:hardened_blood")
    public static final Block BLOCK_HARDENED_BLOOD = null;
    @ObjectHolder("evilcraft:invisible_redstone")
    public static final Block BLOCK_INVISIBLE_REDSTONE = null;
    @ObjectHolder("evilcraft:infested_nether_netherrack")
    public static final Block BLOCK_INFESTED_NETHER_NETHERRACK = null;
    @ObjectHolder("evilcraft:infested_nether_nether_bricks")
    public static final Block BLOCK_INFESTED_NETHER_NETHER_BRICK = null;
    @ObjectHolder("evilcraft:infested_nether_soul_sand")
    public static final Block BLOCK_INFESTED_NETHER_SOUL_SAND = null;
    @ObjectHolder("evilcraft:lightning_bomb")
    public static final Block BLOCK_LIGHTNING_BOMB_PRIMED = null;
    @ObjectHolder("evilcraft:poison")
    public static final LiquidBlock BLOCK_POISON = null;
    @ObjectHolder("evilcraft:purifier")
    public static final Block BLOCK_PURIFIER = null;
    @ObjectHolder("evilcraft:reinforced_undead_planks")
    public static final BlockReinforcedUndeadPlank BLOCK_REINFORCED_UNDEAD_PLANKS = null;
    @ObjectHolder("evilcraft:sanguinary_environmental_accumulator")
    public static final Block BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR = null;
    @ObjectHolder("evilcraft:sanguinary_pedestal_0")
    public static final Block BLOCK_SANGUINARY_PEDESTAL_0 = null;
    @ObjectHolder("evilcraft:sanguinary_pedestal_1")
    public static final Block BLOCK_SANGUINARY_PEDESTAL_1 = null;
    @ObjectHolder("evilcraft:spirit_furnace")
    public static final BlockSpiritFurnace BLOCK_SPIRIT_FURNACE = null;
    @ObjectHolder("evilcraft:spirit_portal")
    public static final Block BLOCK_SPIRIT_PORTAL = null;
    @ObjectHolder("evilcraft:spirit_reanimator")
    public static final Block BLOCK_SPIRIT_REANIMATOR = null;
    @ObjectHolder("evilcraft:undead_leaves")
    public static final Block BLOCK_UNDEAD_LEAVES = null;
    @ObjectHolder("evilcraft:undead_log")
    public static final Block BLOCK_UNDEAD_LOG = null;
    @ObjectHolder("evilcraft:undead_log_stripped")
    public static final Block BLOCK_UNDEAD_LOG_STRIPPED = null;
    @ObjectHolder("evilcraft:undead_wood")
    public static final Block BLOCK_UNDEAD_WOOD = null;
    @ObjectHolder("evilcraft:undead_wood_stripped")
    public static final Block BLOCK_UNDEAD_WOOD_STRIPPED = null;
    @ObjectHolder("evilcraft:undead_planks")
    public static final Block BLOCK_UNDEAD_PLANK = null;
    @ObjectHolder("evilcraft:undead_sapling")
    public static final Block BLOCK_UNDEAD_SAPLING = null;

    @ObjectHolder("evilcraft:blood_chest")
    public static final BlockEntityType<BlockEntityBloodChest> BLOCK_ENTITY_BLOOD_CHEST = null;
    @ObjectHolder("evilcraft:blood_infuser")
    public static final BlockEntityType<BlockEntityBloodInfuser> BLOCK_ENTITY_BLOOD_INFUSER = null;
    @ObjectHolder("evilcraft:blood_stain")
    public static final BlockEntityType<BlockEntityBloodStain> BLOCK_ENTITY_BLOOD_STAIN = null;
    @ObjectHolder("evilcraft:box_of_eternal_closure")
    public static final BlockEntityType<BlockEntityBoxOfEternalClosure> BLOCK_ENTITY_BOX_OF_ETERNAL_CLOSURE = null;
    @ObjectHolder("evilcraft:colossal_blood_chest")
    public static final BlockEntityType<BlockEntityColossalBloodChest> BLOCK_ENTITY_COLOSSAL_BLOOD_CHEST = null;
    @ObjectHolder("evilcraft:dark_tank")
    public static final BlockEntityType<BlockEntityDarkTank> BLOCK_ENTITY_DARK_TANK = null;
    @ObjectHolder("evilcraft:display_stand")
    public static final BlockEntityType<BlockEntityDisplayStand> BLOCK_ENTITY_DISPLAY_STAND = null;
    @ObjectHolder("evilcraft:entangled_chalice")
    public static final BlockEntityType<BlockEntityEntangledChalice> BLOCK_ENTITY_ENTANGLED_CHALICE = null;
    @ObjectHolder("evilcraft:environmental_accumulator")
    public static final BlockEntityType<BlockEntityEnvironmentalAccumulator> BLOCK_ENTITY_ENVIRONMENTAL_ACCUMULATOR = null;
    @ObjectHolder("evilcraft:eternal_water")
    public static final BlockEntityType<BlockEntityEternalWater> BLOCK_ENTITY_ETERNAL_WATER = null;
    @ObjectHolder("evilcraft:invisible_redstone")
    public static final BlockEntityType<BlockEntityInvisibleRedstone> BLOCK_ENTITY_INVISIBLE_REDSTONE = null;
    @ObjectHolder("evilcraft:purifier")
    public static final BlockEntityType<BlockEntityPurifier> BLOCK_ENTITY_PURIFIER = null;
    @ObjectHolder("evilcraft:sanguinary_environmental_accumulator")
    public static final BlockEntityType<BlockEntitySanguinaryEnvironmentalAccumulator> BLOCK_ENTITY_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR = null;
    @ObjectHolder("evilcraft:sanguinary_pedestal")
    public static final BlockEntityType<BlockEntitySanguinaryPedestal> BLOCK_ENTITY_SANGUINARY_PEDESTAL = null;
    @ObjectHolder("evilcraft:spirit_furnace")
    public static final BlockEntityType<BlockEntitySpiritFurnace> BLOCK_ENTITY_SPIRIT_FURNACE = null;
    @ObjectHolder("evilcraft:spirit_portal")
    public static final BlockEntityType<BlockEntitySpiritPortal> BLOCK_ENTITY_SPIRIT_PORTAL = null;
    @ObjectHolder("evilcraft:spirit_reanimator")
    public static final BlockEntityType<BlockEntitySpiritReanimator> BLOCK_ENTITY_SPIRIT_REANIMATOR = null;

    @ObjectHolder("evilcraft:blood")
    public static final FlowingFluid FLUID_BLOOD = null;
    @ObjectHolder("evilcraft:poison")
    public static final FlowingFluid FLUID_POISON = null;

    @ObjectHolder("evilcraft:paling")
    public static final MobEffect POTION_PALING = null;

    @ObjectHolder("evilcraft:vengeance")
    public static final Enchantment ENCHANTMENT_VENGEANCE = null;
    @ObjectHolder("evilcraft:life_stealing")
    public static final Enchantment ENCHANTMENT_LIFE_STEALING = null;

    @ObjectHolder("evilcraft:blood_chest")
    public static final MenuType<ContainerBloodChest> CONTAINER_BLOOD_CHEST = null;
    @ObjectHolder("evilcraft:blood_infuser")
    public static final MenuType<ContainerBloodInfuser> CONTAINER_BLOOD_INFUSER = null;
    @ObjectHolder("evilcraft:colossal_blood_chest")
    public static final MenuType<ContainerColossalBloodChest> CONTAINER_COLOSSAL_BLOOD_CHEST = null;
    @ObjectHolder("evilcraft:exalted_crafter")
    public static final MenuType<ContainerExaltedCrafter> CONTAINER_EXALTED_CRAFTER = null;
    @ObjectHolder("evilcraft:origins_of_darkness")
    public static final MenuType<ContainerOriginsOfDarkness> CONTAINER_ORIGINS_OF_DARKNESS = null;
    @ObjectHolder("evilcraft:primed_pendant")
    public static final MenuType<ContainerPrimedPendant> CONTAINER_PRIMED_PENDANT = null;
    @ObjectHolder("evilcraft:sanguinary_environmental_accumulator")
    public static final MenuType<ContainerSanguinaryEnvironmentalAccumulator> CONTAINER_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR = null;
    @ObjectHolder("evilcraft:spirit_furnace")
    public static final MenuType<ContainerSpiritFurnace> CONTAINER_SPIRIT_FURNACE = null;
    @ObjectHolder("evilcraft:spirit_reanimator")
    public static final MenuType<ContainerSpiritReanimator> CONTAINER_SPIRIT_REANIMATOR = null;

    // Recipe types are not Forge registries unfortunately...
    public static RecipeType<RecipeBloodInfuser> RECIPETYPE_BLOOD_INFUSER = null;
    public static RecipeType<RecipeEnvironmentalAccumulator> RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR = null;

    @ObjectHolder("evilcraft:blood_infuser")
    public static final RecipeSerializer<RecipeBloodInfuser> RECIPESERIALIZER_BLOOD_INFUSER = null;
    @ObjectHolder("evilcraft:environmental_accumulator")
    public static final RecipeSerializer<RecipeEnvironmentalAccumulator> RECIPESERIALIZER_ENVIRONMENTAL_ACCUMULATOR = null;
    @ObjectHolder("evilcraft:crafting_special_bloodextractor_combination")
    public static final RecipeSerializer<RecipeBloodExtractorCombination> RECIPESERIALIZER_BLOODEXTRACTOR_COMBINATION = null;
    @ObjectHolder("evilcraft:crafting_special_fluidcontainer_combination")
    public static final RecipeSerializer<RecipeFluidContainerCombination> RECIPESERIALIZER_FLUIDCONTAINER_COMBINATION = null;
    @ObjectHolder("evilcraft:crafting_special_dead_bush")
    public static final RecipeSerializer<RecipeDeadBush> RECIPESERIALIZER_DEAD_BUSH = null;
    @ObjectHolder("evilcraft:crafting_special_broom_part_combination")
    public static final RecipeSerializer<RecipeBroomPartCombination> RECIPESERIALIZER_BROOM_PART_COMBINATION = null;
    @ObjectHolder("evilcraft:environmental_accumulator_biome_extract")
    public static final RecipeSerializer<RecipeEnvironmentalAccumulatorBiomeExtract> RECIPESERIALIZER_BIOME_EXTRACT = null;

    @ObjectHolder("cyclopscore:blur")
    public static final ParticleType<ParticleBlurData> PARTICLE_BLUR = null;
    @ObjectHolder("evilcraft:blur_targetted")
    public static final ParticleType<ParticleBlurTargettedData> PARTICLE_BLUR_TARGETTED = null;
    @ObjectHolder("evilcraft:blur_targetted_entity")
    public static final ParticleType<ParticleBlurTargettedEntityData> PARTICLE_BLUR_TARGETTED_ENTITY = null;
    @ObjectHolder("evilcraft:blood_bubble")
    public static final SimpleParticleType PARTICLE_BLOOD_BUBBLE = null;
    @ObjectHolder("evilcraft:blood_splash")
    public static final SimpleParticleType PARTICLE_BLOOD_SPLASH = null;
    @ObjectHolder("evilcraft:bubble_extended")
    public static final ParticleType<ParticleBubbleExtendedData> PARTICLE_BUBBLE_EXTENDED = null;
    @ObjectHolder("evilcraft:colored_smoke")
    public static final ParticleType<ParticleColoredSmokeData> PARTICLE_COLORED_SMOKE = null;
    @ObjectHolder("evilcraft:dark_smoke")
    public static final ParticleType<ParticleDarkSmokeData> PARTICLE_DARK_SMOKE = null;
    @ObjectHolder("evilcraft:degrade")
    public static final SimpleParticleType PARTICLE_DEGRADE = null;
    @ObjectHolder("evilcraft:distort")
    public static final ParticleType<ParticleDistortData> PARTICLE_DISTORT = null;
    @ObjectHolder("evilcraft:explosion_extended")
    public static final ParticleType<ParticleExplosionExtendedData> PARTICLE_EXPLOSION_EXTENDED = null;
    @ObjectHolder("evilcraft:fart")
    public static final ParticleType<ParticleFartData> PARTICLE_FART = null;
    @ObjectHolder("evilcraft:magic_finish")
    public static final SimpleParticleType PARTICLE_MAGIC_FINISH = null;

    @ObjectHolder("evilcraft:controlled_zombie")
    public static final EntityType<? extends EntityControlledZombie> ENTITY_CONTROLLED_ZOMBIE = null;
    @ObjectHolder("evilcraft:netherfish")
    public static final EntityType<? extends EntityNetherfish> ENTITY_NETHERFISH = null;
    @ObjectHolder("evilcraft:poisonous_libelle")
    public static final EntityType<? extends EntityPoisonousLibelle> ENTITY_POISONOUS_LIBELLE = null;
    @ObjectHolder("evilcraft:vengeance_spirit")
    public static final EntityType<? extends EntityVengeanceSpirit> ENTITY_VENGEANCE_SPIRIT = null;
    @ObjectHolder("evilcraft:werewolf")
    public static final EntityType<? extends EntityWerewolf> ENTITY_WEREWOLF = null;
    @ObjectHolder("evilcraft:biome_extract")
    public static final EntityType<? extends EntityBiomeExtract> ENTITY_BIOME_EXTRACT = null;
    @ObjectHolder("evilcraft:blood_pearl")
    public static final EntityType<? extends EntityBiomeExtract> ENTITY_BLOOD_PEARL = null;
    @ObjectHolder("evilcraft:item_dark_stick")
    public static final EntityType<? extends EntityItemDarkStick> ENTITY_ITEM_DARK_STICK = null;
    @ObjectHolder("evilcraft:item_empowerable")
    public static final EntityType<? extends EntityItemEmpowerable> ENTITY_ITEM_EMPOWERABLE = null;
    @ObjectHolder("evilcraft:item_undespawnable")
    public static final EntityType<? extends EntityItemUndespawnable> ENTITY_ITEM_UNDESPAWNABLE = null;
    @ObjectHolder("evilcraft:lightning_grenade")
    public static final EntityType<? extends EntityLightningGrenade> ENTITY_LIGHTNING_GRENADE = null;
    @ObjectHolder("evilcraft:redstone_grenade")
    public static final EntityType<? extends EntityRedstoneGrenade> ENTITY_REDSTONE_GRENADE = null;
    @ObjectHolder("evilcraft:weather_container")
    public static final EntityType<? extends EntityWeatherContainer> ENTITY_WEATHER_CONTAINER = null;
    @ObjectHolder("evilcraft:anti_vengeance_beam")
    public static final EntityType<? extends EntityAttackVengeanceBeam> ENTITY_ANTI_VENGEANCE_BEAM = null;
    @ObjectHolder("evilcraft:attack_vengeance_beam")
    public static final EntityType<? extends EntityAttackVengeanceBeam> ENTITY_ATTACK_VENGEANCE_BEAM = null;
    @ObjectHolder("evilcraft:necromancers_head")
    public static final EntityType<? extends EntityNecromancersHead> ENTITY_NECROMANCER_HEAD = null;
    @ObjectHolder("evilcraft:lightning_bomb_primed")
    public static final EntityType<? extends EntityLightningBombPrimed> ENTITY_LIGHTNING_BOMB_PRIMED = null;
    @ObjectHolder("evilcraft:broom")
    public static final EntityType<? extends EntityBroom> ENTITY_BROOM = null;

    @ObjectHolder("evilcraft:werewolf")
    public static final VillagerProfession VILLAGER_PROFESSION_WEREWOLF = null;

    @ObjectHolder("evilcraft:degraded")
    public static final Biome BIOME_DEGRADED = null;

    @ObjectHolder("evilcraft:dark_temple")
    public static final StructureFeature<?> STRUCTURE_DARK_TEMPLE = null;
}
