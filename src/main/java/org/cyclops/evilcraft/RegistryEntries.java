package org.cyclops.evilcraft;

import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.advancement.criterion.BoxOfEternalClosureCaptureTrigger;
import org.cyclops.evilcraft.advancement.criterion.DistortTrigger;
import org.cyclops.evilcraft.advancement.criterion.FartTrigger;
import org.cyclops.evilcraft.advancement.criterion.NecromanceTrigger;
import org.cyclops.evilcraft.block.*;
import org.cyclops.evilcraft.blockentity.*;
import org.cyclops.evilcraft.client.particle.*;
import org.cyclops.evilcraft.component.DataComponentBiomeConfig;
import org.cyclops.evilcraft.core.broom.BroomModifiersContents;
import org.cyclops.evilcraft.core.broom.BroomPartsContents;
import org.cyclops.evilcraft.core.recipe.type.*;
import org.cyclops.evilcraft.entity.block.EntityLightningBombPrimed;
import org.cyclops.evilcraft.entity.effect.EntityAttackVengeanceBeam;
import org.cyclops.evilcraft.entity.effect.EntityNecromancersHead;
import org.cyclops.evilcraft.entity.item.*;
import org.cyclops.evilcraft.entity.monster.*;
import org.cyclops.evilcraft.inventory.container.*;
import org.cyclops.evilcraft.item.*;
import org.cyclops.evilcraft.world.gen.structure.WorldStructureDarkTemple;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolder<Item, Item> ITEM_BLOOD_PEARL_OF_TELEPORTATION = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:blood_pearl_of_teleportation"));
    public static final DeferredHolder<Item, Item> ITEM_BLOOD_INFUSION_CORE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:blood_infusion_core"));
    public static final DeferredHolder<Item, Item> ITEM_BROOM = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:broom"));
    public static final DeferredHolder<Item, Item> ITEM_BROOM_PART = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:broom_part"));
    public static final DeferredHolder<Item, Item> ITEM_BUCKET_BLOOD = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:bucket_blood"));
    public static final DeferredHolder<Item, Item> ITEM_BUCKET_POISON = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:bucket_poison"));
    public static final DeferredHolder<Item, Item> ITEM_BLOOK = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:blook"));
    public static final DeferredHolder<Item, Item> ITEM_CONDENSED_BLOOD = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:condensed_blood"));
    public static final DeferredHolder<Item, Item> ITEM_DARK_GEM = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:dark_gem"));
    public static final DeferredHolder<Item, Item> ITEM_DARK_GEM_CRUSHED = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:dark_gem_crushed"));
    public static final DeferredHolder<Item, Item> ITEM_DARK_POWER_GEM = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:dark_power_gem"));
    public static final DeferredHolder<Item, Item> ITEM_DARK_TANK = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:dark_tank"));
    public static final DeferredHolder<Item, Item> ITEM_DARK_SPIKE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:dark_spike"));
    public static final DeferredHolder<Item, ItemBiomeExtract> ITEM_BIOME_EXTRACT = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:biome_extract"));
    public static final DeferredHolder<Item, Item> ITEM_BOWL_OF_PROMISES_EMPTY = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:bowl_of_promises_empty"));
    public static final DeferredHolder<Item, Item> ITEM_BOWL_OF_PROMISES_DUSTED = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:bowl_of_promises_dusted"));
    public static final DeferredHolder<Item, Item> ITEM_BOWL_OF_PROMISES_TIER0 = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:bowl_of_promises_tier0"));
    public static final DeferredHolder<Item, Item> ITEM_BOWL_OF_PROMISES_TIER1 = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:bowl_of_promises_tier1"));
    public static final DeferredHolder<Item, Item> ITEM_BOWL_OF_PROMISES_TIER2 = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:bowl_of_promises_tier2"));
    public static final DeferredHolder<Item, Item> ITEM_BOWL_OF_PROMISES_TIER3 = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:bowl_of_promises_tier3"));
    public static final DeferredHolder<Item, Item> ITEM_BOX_OF_ETERNAL_CLOSURE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:box_of_eternal_closure"));
    public static final DeferredHolder<Item, Item> ITEM_BLOOD_EXTRACTOR = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:blood_extractor"));
    public static final DeferredHolder<Item, Item> ITEM_BURNING_GEM_STONE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:burning_gem_stone"));
    public static final DeferredHolder<Item, Item> ITEM_ENTANGLED_CHALICE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:entangled_chalice"));
    public static final DeferredHolder<Item, Item> ITEM_EXALTED_CRAFTER = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:exalted_crafter"));
    public static final DeferredHolder<Item, Item> ITEM_EXALTED_CRAFTER_WOODEN = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:exalted_crafter_wooden"));
    public static final DeferredHolder<Item, Item> ITEM_EXALTED_CRAFTER_EMPOWERED = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:exalted_crafter_empowered"));
    public static final DeferredHolder<Item, Item> ITEM_EXALTED_CRAFTER_WOODEN_EMPOWERED = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:exalted_crafter_wooden_empowered"));
    public static final DeferredHolder<Item, Item> ITEM_GARMONBOZIA = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:garmonbozia"));
    public static final DeferredHolder<Item, Item> ITEM_FLESH_WEREWOLF = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:flesh_werewolf"));
    public static final DeferredHolder<Item, Item> ITEM_FLESH_HUMANOID = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:flesh_werewolf"));
    public static final DeferredHolder<Item, Item> ITEM_HARDENED_BLOOD_SHARD = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:hardened_blood_shard"));
    public static final DeferredHolder<Item, ItemMaceOfDistortion> ITEM_MACE_OF_DISTORTION = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:mace_of_distortion"));
    public static final DeferredHolder<Item, Item> ITEM_PROMISE_TIER_1 = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:promise_tier_1"));
    public static final DeferredHolder<Item, Item> ITEM_PROMISE_TIER_2 = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:promise_tier_2"));
    public static final DeferredHolder<Item, Item> ITEM_PROMISE_TIER_3 = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:promise_tier_3"));
    public static final DeferredHolder<Item, Item> ITEM_PROMISE_SPEED = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:promise_speed_0"));
    public static final DeferredHolder<Item, Item> ITEM_PROMISE_EFFICIENCY = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:promise_efficiency_0"));
    public static final DeferredHolder<Item, Item> ITEM_INVERTED_POTENTIA = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:inverted_potentia"));
    public static final DeferredHolder<Item, Item> ITEM_INVERTED_POTENTIA_EMPOWERED = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:inverted_potentia_empowered"));
    public static final DeferredHolder<Item, Item> ITEM_LIGHTNING_GRENADE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:lightning_grenade"));
    public static final DeferredHolder<Item, Item> ITEM_ORIGINS_OF_DARKNESS = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:origins_of_darkness"));
    public static final DeferredHolder<Item, Item> ITEM_POISON_SAC = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:poison_sac"));
    public static final DeferredHolder<Item, Item> ITEM_REDSTONE_GRENADE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:redstone_grenade"));
    public static final DeferredHolder<Item, ItemVeinSword> ITEM_VEIN_SWORD = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:vein_sword"));
    public static final DeferredHolder<Item, Item> ITEM_UNDEAD_SAPLING = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:undead_sapling"));
    public static final DeferredHolder<Item, Item> ITEM_VENGEANCE_FOCUS = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:vengeance_focus"));
    public static final DeferredHolder<Item, Item> ITEM_PIERCING_VENGEANCE_FOCUS = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:piercing_vengeance_focus"));
    public static final DeferredHolder<Item, ItemVengeancePickaxe> ITEM_VENGEANCE_PICKAXE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:vengeance_pickaxe"));
    public static final DeferredHolder<Item, Item> ITEM_VENGEANCE_ESSENCE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:vengeance_essence"));
    public static final DeferredHolder<Item, Item> ITEM_VENGEANCE_RING = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:vengeance_ring"));
    public static final DeferredHolder<Item, Item> ITEM_WEATHER_CONTAINER = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:weather_container"));
    public static final DeferredHolder<Item, Item> ITEM_ETERNAL_WATER = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:bucket_eternal_water"));
    public static final DeferredHolder<Item, Item> ITEM_CREATIVE_BLOOD_DROP = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:creative_blood_drop"));
    public static final DeferredHolder<Item, Item> ITEM_DISPLAY_STAND = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:display_stand"));

    public static final DeferredHolder<Item, Item> ITEM_WEREWOLF_BONE = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:werewolf_bone"));
    public static final DeferredHolder<Item, Item> ITEM_WEREWOLF_FUR = DeferredHolder.create(Registries.ITEM, Identifier.parse("evilcraft:werewolf_fur"));

    public static final DeferredHolder<Block, LiquidBlock> BLOCK_BLOOD = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:blood"));
    public static final DeferredHolder<Block, Block> BLOCK_BLOOD_CHEST = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:blood_chest"));
    public static final DeferredHolder<Block, Block> BLOCK_BLOOD_INFUSER = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:blood_infuser"));
    public static final DeferredHolder<Block, Block> BLOCK_BLOOD_STAIN = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:blood_stain"));
    public static final DeferredHolder<Block, Block> BLOCK_BLOODY_COBBLESTONE = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:bloody_cobblestone"));
    public static final DeferredHolder<Block, Block> BLOCK_BOX_OF_ETERNAL_CLOSURE = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:box_of_eternal_closure"));
    public static final DeferredHolder<Block, BlockColossalBloodChest> BLOCK_COLOSSAL_BLOOD_CHEST = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:colossal_blood_chest"));
    public static final DeferredHolder<Block, BlockDarkBloodBrick> BLOCK_DARK_BLOOD_BRICK = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:dark_blood_brick"));
    public static final DeferredHolder<Block, Block> BLOCK_DARK_BRICK = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:dark_brick"));
    public static final DeferredHolder<Block, BlockDarkTank> BLOCK_DARK_TANK = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:dark_tank"));
    public static final DeferredHolder<Block, BlockDisplayStand> BLOCK_DISPLAY_STAND = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:display_stand"));
    public static final DeferredHolder<Block, Block> BLOCK_ENTANGLED_CHALICE = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:entangled_chalice"));
    public static final DeferredHolder<Block, Block> BLOCK_ENVIRONMENTAL_ACCUMULATOR = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:environmental_accumulator"));
    public static final DeferredHolder<Block, Block> BLOCK_ETERNAL_WATER = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:eternal_water"));
    public static final DeferredHolder<Block, Block> BLOCK_GEM_STONE_TORCH = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:gem_stone_torch"));
    public static final DeferredHolder<Block, Block> BLOCK_GEM_STONE_TORCH_WALL = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:gem_stone_torch_wall"));
    public static final DeferredHolder<Block, Block> BLOCK_HARDENED_BLOOD = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:hardened_blood"));
    public static final DeferredHolder<Block, Block> BLOCK_INVISIBLE_REDSTONE = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:invisible_redstone"));
    public static final DeferredHolder<Block, Block> BLOCK_INFESTED_NETHER_NETHERRACK = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:infested_nether_netherrack"));
    public static final DeferredHolder<Block, Block> BLOCK_INFESTED_NETHER_NETHER_BRICK = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:infested_nether_nether_bricks"));
    public static final DeferredHolder<Block, Block> BLOCK_INFESTED_NETHER_SOUL_SAND = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:infested_nether_soul_sand"));
    public static final DeferredHolder<Block, Block> BLOCK_LIGHTNING_BOMB_PRIMED = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:lightning_bomb"));
    public static final DeferredHolder<Block, LiquidBlock> BLOCK_POISON = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:poison"));
    public static final DeferredHolder<Block, Block> BLOCK_PURIFIER = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:purifier"));
    public static final DeferredHolder<Block, BlockReinforcedUndeadPlank> BLOCK_REINFORCED_UNDEAD_PLANKS = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:reinforced_undead_planks"));
    public static final DeferredHolder<Block, Block> BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:sanguinary_environmental_accumulator"));
    public static final DeferredHolder<Block, Block> BLOCK_SANGUINARY_PEDESTAL_0 = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:sanguinary_pedestal_0"));
    public static final DeferredHolder<Block, Block> BLOCK_SANGUINARY_PEDESTAL_1 = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:sanguinary_pedestal_1"));
    public static final DeferredHolder<Block, BlockSpiritFurnace> BLOCK_SPIRIT_FURNACE = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:spirit_furnace"));
    public static final DeferredHolder<Block, Block> BLOCK_SPIRIT_PORTAL = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:spirit_portal"));
    public static final DeferredHolder<Block, Block> BLOCK_SPIRIT_REANIMATOR = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:spirit_reanimator"));
    public static final DeferredHolder<Block, Block> BLOCK_UNDEAD_LEAVES = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:undead_leaves"));
    public static final DeferredHolder<Block, Block> BLOCK_UNDEAD_LOG = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:undead_log"));
    public static final DeferredHolder<Block, Block> BLOCK_UNDEAD_LOG_STRIPPED = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:undead_log_stripped"));
    public static final DeferredHolder<Block, Block> BLOCK_UNDEAD_WOOD = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:undead_wood"));
    public static final DeferredHolder<Block, Block> BLOCK_UNDEAD_WOOD_STRIPPED = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:undead_wood_stripped"));
    public static final DeferredHolder<Block, Block> BLOCK_UNDEAD_PLANK = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:undead_planks"));
    public static final DeferredHolder<Block, Block> BLOCK_UNDEAD_SAPLING = DeferredHolder.create(Registries.BLOCK, Identifier.parse("evilcraft:undead_sapling"));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBloodChest>> BLOCK_ENTITY_BLOOD_CHEST = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:blood_chest"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBloodInfuser>> BLOCK_ENTITY_BLOOD_INFUSER = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:blood_infuser"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBloodStain>> BLOCK_ENTITY_BLOOD_STAIN = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:blood_stain"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBoxOfEternalClosure>> BLOCK_ENTITY_BOX_OF_ETERNAL_CLOSURE = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:box_of_eternal_closure"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityColossalBloodChest>> BLOCK_ENTITY_COLOSSAL_BLOOD_CHEST = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:colossal_blood_chest"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityDarkTank>> BLOCK_ENTITY_DARK_TANK = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:dark_tank"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityDisplayStand>> BLOCK_ENTITY_DISPLAY_STAND = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:display_stand"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityEntangledChalice>> BLOCK_ENTITY_ENTANGLED_CHALICE = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:entangled_chalice"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityEnvironmentalAccumulator>> BLOCK_ENTITY_ENVIRONMENTAL_ACCUMULATOR = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:environmental_accumulator"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityEternalWater>> BLOCK_ENTITY_ETERNAL_WATER = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:eternal_water"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityInvisibleRedstone>> BLOCK_ENTITY_INVISIBLE_REDSTONE = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:invisible_redstone"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityPurifier>> BLOCK_ENTITY_PURIFIER = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:purifier"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySanguinaryEnvironmentalAccumulator>> BLOCK_ENTITY_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:sanguinary_environmental_accumulator"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySanguinaryPedestal>> BLOCK_ENTITY_SANGUINARY_PEDESTAL = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:sanguinary_pedestal"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySpiritFurnace>> BLOCK_ENTITY_SPIRIT_FURNACE = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:spirit_furnace"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySpiritPortal>> BLOCK_ENTITY_SPIRIT_PORTAL = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:spirit_portal"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySpiritReanimator>> BLOCK_ENTITY_SPIRIT_REANIMATOR = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("evilcraft:spirit_reanimator"));

    public static final DeferredHolder<Fluid, FlowingFluid> FLUID_BLOOD = DeferredHolder.create(Registries.FLUID, Identifier.parse("evilcraft:blood"));
    public static final DeferredHolder<Fluid, FlowingFluid> FLUID_POISON = DeferredHolder.create(Registries.FLUID, Identifier.parse("evilcraft:poison"));

    public static final DeferredHolder<MobEffect, MobEffect> POTION_PALING = DeferredHolder.create(Registries.MOB_EFFECT, Identifier.parse("evilcraft:paling"));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerBloodChest>> CONTAINER_BLOOD_CHEST = DeferredHolder.create(Registries.MENU, Identifier.parse("evilcraft:blood_chest"));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerBloodInfuser>> CONTAINER_BLOOD_INFUSER = DeferredHolder.create(Registries.MENU, Identifier.parse("evilcraft:blood_infuser"));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerColossalBloodChest>> CONTAINER_COLOSSAL_BLOOD_CHEST = DeferredHolder.create(Registries.MENU, Identifier.parse("evilcraft:colossal_blood_chest"));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerExaltedCrafter>> CONTAINER_EXALTED_CRAFTER = DeferredHolder.create(Registries.MENU, Identifier.parse("evilcraft:exalted_crafter"));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerOriginsOfDarkness>> CONTAINER_ORIGINS_OF_DARKNESS = DeferredHolder.create(Registries.MENU, Identifier.parse("evilcraft:origins_of_darkness"));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerPrimedPendant>> CONTAINER_PRIMED_PENDANT = DeferredHolder.create(Registries.MENU, Identifier.parse("evilcraft:primed_pendant"));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerSanguinaryEnvironmentalAccumulator>> CONTAINER_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR = DeferredHolder.create(Registries.MENU, Identifier.parse("evilcraft:sanguinary_environmental_accumulator"));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerSpiritFurnace>> CONTAINER_SPIRIT_FURNACE = DeferredHolder.create(Registries.MENU, Identifier.parse("evilcraft:spirit_furnace"));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerSpiritReanimator>> CONTAINER_SPIRIT_REANIMATOR = DeferredHolder.create(Registries.MENU, Identifier.parse("evilcraft:spirit_reanimator"));

    public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeBloodInfuser>> RECIPETYPE_BLOOD_INFUSER = DeferredHolder.create(Registries.RECIPE_TYPE, Identifier.parse("evilcraft:blood_infuser"));
    public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeEnvironmentalAccumulator>> RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR = DeferredHolder.create(Registries.RECIPE_TYPE, Identifier.parse("evilcraft:environmental_accumulator"));

    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> RECIPEBOOKCATEGORY_BLOOD_INFUSER = DeferredHolder.create(Registries.RECIPE_BOOK_CATEGORY, Identifier.parse("evilcraft:blood_infuser"));
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> RECIPEBOOKCATEGORY_ENVIRONMENTAL_ACCUMULATOR = DeferredHolder.create(Registries.RECIPE_BOOK_CATEGORY, Identifier.parse("evilcraft:environmental_accumulator"));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeBloodInfuser>> RECIPESERIALIZER_BLOOD_INFUSER = DeferredHolder.create(Registries.RECIPE_SERIALIZER, Identifier.parse("evilcraft:blood_infuser"));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeEnvironmentalAccumulator>> RECIPESERIALIZER_ENVIRONMENTAL_ACCUMULATOR = DeferredHolder.create(Registries.RECIPE_SERIALIZER, Identifier.parse("evilcraft:environmental_accumulator"));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeBloodExtractorCombination>> RECIPESERIALIZER_BLOODEXTRACTOR_COMBINATION = DeferredHolder.create(Registries.RECIPE_SERIALIZER, Identifier.parse("evilcraft:crafting_special_bloodextractor_combination"));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeFluidContainerCombination>> RECIPESERIALIZER_FLUIDCONTAINER_COMBINATION = DeferredHolder.create(Registries.RECIPE_SERIALIZER, Identifier.parse("evilcraft:crafting_special_fluidcontainer_combination"));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeDeadBush>> RECIPESERIALIZER_DEAD_BUSH = DeferredHolder.create(Registries.RECIPE_SERIALIZER, Identifier.parse("evilcraft:crafting_special_dead_bush"));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeBroomPartCombination>> RECIPESERIALIZER_BROOM_PART_COMBINATION = DeferredHolder.create(Registries.RECIPE_SERIALIZER, Identifier.parse("evilcraft:crafting_special_broom_part_combination"));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeFluidContainerClear>> RECIPESERIALIZER_FLUIDCONTAINER_CLEAR = DeferredHolder.create(Registries.RECIPE_SERIALIZER, Identifier.parse("evilcraft:crafting_special_fluidcontainer_clear"));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeEnvironmentalAccumulatorBiomeExtract>> RECIPESERIALIZER_BIOME_EXTRACT = DeferredHolder.create(Registries.RECIPE_SERIALIZER, Identifier.parse("evilcraft:environmental_accumulator_biome_extract"));

    public static final DeferredHolder<ParticleType<?>, ParticleType<ParticleBlurData>> PARTICLE_BLUR = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:blur"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ParticleBlurTargettedData>> PARTICLE_BLUR_TARGETTED = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:blur_targetted"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ParticleBlurTargettedEntityData>> PARTICLE_BLUR_TARGETTED_ENTITY = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:blur_targetted_entity"));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PARTICLE_BLOOD_BUBBLE = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:blood_bubble"));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PARTICLE_BLOOD_SPLASH = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:blood_splash"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ParticleBubbleExtendedData>> PARTICLE_BUBBLE_EXTENDED = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:bubble_extended"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ParticleColoredSmokeData>> PARTICLE_COLORED_SMOKE = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:colored_smoke"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ParticleDarkSmokeData>> PARTICLE_DARK_SMOKE = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:dark_smoke"));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PARTICLE_DEGRADE = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:degrade"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ParticleDistortData>> PARTICLE_DISTORT = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:distort"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ParticleExplosionExtendedData>> PARTICLE_EXPLOSION_EXTENDED = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:explosion_extended"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ParticleFartData>> PARTICLE_FART = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:fart"));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PARTICLE_MAGIC_FINISH = DeferredHolder.create(Registries.PARTICLE_TYPE, Identifier.parse("evilcraft:magic_finish"));

    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityControlledZombie>> ENTITY_CONTROLLED_ZOMBIE = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:controlled_zombie"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityNetherfish>> ENTITY_NETHERFISH = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:netherfish"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityPoisonousLibelle>> ENTITY_POISONOUS_LIBELLE = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:poisonous_libelle"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityVengeanceSpirit>> ENTITY_VENGEANCE_SPIRIT = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:vengeance_spirit"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityWerewolf>> ENTITY_WEREWOLF = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:werewolf"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityBiomeExtract>> ENTITY_BIOME_EXTRACT = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:biome_extract"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityBiomeExtract>> ENTITY_BLOOD_PEARL = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:blood_pearl"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityItemDarkStick>> ENTITY_ITEM_DARK_STICK = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:item_dark_stick"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityItemEmpowerable>> ENTITY_ITEM_EMPOWERABLE = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:item_empowerable"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityItemUndespawnable>> ENTITY_ITEM_UNDESPAWNABLE = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:item_undespawnable"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityLightningGrenade>> ENTITY_LIGHTNING_GRENADE = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:lightning_grenade"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityRedstoneGrenade>> ENTITY_REDSTONE_GRENADE = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:redstone_grenade"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityWeatherContainer>> ENTITY_WEATHER_CONTAINER = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:weather_container"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityAttackVengeanceBeam>> ENTITY_ANTI_VENGEANCE_BEAM = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:anti_vengeance_beam"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityAttackVengeanceBeam>> ENTITY_ATTACK_VENGEANCE_BEAM = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:attack_vengeance_beam"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityNecromancersHead>> ENTITY_NECROMANCER_HEAD = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:necromancers_head"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityLightningBombPrimed>> ENTITY_LIGHTNING_BOMB_PRIMED = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:lightning_bomb_primed"));
    public static final DeferredHolder<EntityType<?>, EntityType<? extends EntityBroom>> ENTITY_BROOM = DeferredHolder.create(Registries.ENTITY_TYPE, Identifier.parse("evilcraft:broom"));

    public static final DeferredHolder<CriterionTrigger<?>, BoxOfEternalClosureCaptureTrigger> TRIGGER_BOX_OF_ETERNAL_CLOSURE_CAPTURE = DeferredHolder.create(Registries.TRIGGER_TYPE, Identifier.parse("evilcraft:box_of_eternal_closure_capture"));
    public static final DeferredHolder<CriterionTrigger<?>, DistortTrigger> TRIGGER_DISTORT = DeferredHolder.create(Registries.TRIGGER_TYPE, Identifier.parse("evilcraft:distort"));
    public static final DeferredHolder<CriterionTrigger<?>, FartTrigger> TRIGGER_FART = DeferredHolder.create(Registries.TRIGGER_TYPE, Identifier.parse("evilcraft:fart"));
    public static final DeferredHolder<CriterionTrigger<?>, NecromanceTrigger> TRIGGER_NECROMANCE_TRIGGER = DeferredHolder.create(Registries.TRIGGER_TYPE, Identifier.parse("evilcraft:necromance"));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> VILLAGER_PROFESSION_WEREWOLF = DeferredHolder.create(Registries.VILLAGER_PROFESSION, Identifier.parse("evilcraft:werewolf"));

    public static final ResourceKey<Biome> BIOME_DEGRADED = ResourceKey.create(Registries.BIOME, Identifier.parse("evilcraft:degraded"));

    public static final DeferredHolder<Structure, Structure> STRUCTURE_DARK_TEMPLE = DeferredHolder.create(Registries.STRUCTURE, Identifier.parse("evilcraft:dark_temple"));
    public static final DeferredHolder<StructureType<?>, StructureType<WorldStructureDarkTemple>> STRUCTURE_TYPE_DARK_TEMPLE = DeferredHolder.create(Registries.STRUCTURE_TYPE, Identifier.parse("evilcraft:dark_temple"));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> STRUCTURE_PIECE_DARK_TEMPLE = DeferredHolder.create(Registries.STRUCTURE_PIECE, Identifier.parse("evilcraft:dark_temple_piece"));

    public static final DeferredHolder<SoundEvent, SoundEvent> SOUNDEVENT_EFFECT_VENGEANCEBEAM_BASE = DeferredHolder.create(Registries.SOUND_EVENT, Identifier.parse("evilcraft:effect_vengeancebeam_base"));
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUNDEVENT_EFFECT_VENGEANCEBEAM_START = DeferredHolder.create(Registries.SOUND_EVENT, Identifier.parse("evilcraft:effect_vengeancebeam_start"));
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUNDEVENT_EFFECT_VENGEANCEBEAM_STOP = DeferredHolder.create(Registries.SOUND_EVENT, Identifier.parse("evilcraft:effect_vengeancebeam_stop"));
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUNDEVENT_MOB_VENGEANCESPIRIT_AMBIENT = DeferredHolder.create(Registries.SOUND_EVENT, Identifier.parse("evilcraft:mob_vengeancespirit_ambient"));
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUNDEVENT_MOB_VENGEANCESPIRIT_DEATH = DeferredHolder.create(Registries.SOUND_EVENT, Identifier.parse("evilcraft:mob_vengeancespirit_death"));
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUNDEVENT_EFFECT_BOX_BEAM = DeferredHolder.create(Registries.SOUND_EVENT, Identifier.parse("evilcraft:effect_box_beam"));
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUNDEVENT_EFFECT_PAGE_FLIPSINGLE = DeferredHolder.create(Registries.SOUND_EVENT, Identifier.parse("evilcraft:effect_page_flipsingle"));
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUNDEVENT_EFFECT_PAGE_FLIPMULTIPLE = DeferredHolder.create(Registries.SOUND_EVENT, Identifier.parse("evilcraft:effect_page_flipmultiple"));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENCHANTMENTEFFECT_COMPONENT_AMPLIFY_DAMAGE = DeferredHolder.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Identifier.parse("evilcraft:amplify_damage"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ENCHANTMENTEFFECT_COMPONENT_STOP_DAMAGE = DeferredHolder.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Identifier.parse("evilcraft:stop_damage"));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> COMPONENT_ACTIVATED = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:activated"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DataComponentBiomeConfig.BiomeHolder>> COMPONENT_BIOME = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:biome"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> COMPONENT_BOX_PLAYER_ID = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:box_player_id"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> COMPONENT_BOX_PLAYER_NAME = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:box_player_name"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> COMPONENT_BOX_SPIRIT_DATA = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:box_spirit"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BroomModifiersContents>> COMPONENT_BROOM_MODIFIERS = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:broom_modifiers"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BroomPartsContents>> COMPONENT_BROOM_PARTS = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:broom_parts"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockState>> COMPONENT_DISPLAY_STAND_TYPE = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:display_stand_type"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> COMPONENT_EXALTED_CRAFTER_RETURN_TO_INNER = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:exalted_crafter_return_to_inner"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COMPONENT_POWER = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:power"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemWeatherContainer.WeatherContainerType>> COMPONENT_WEATHER_CONTAINER_TYPE = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:weather_container_type"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> COMPONENT_WORLD_SHARED_TANK_ID = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, Identifier.parse("evilcraft:world_shared_tank_id"));

    public static final DeferredHolder<PoiType, PoiType> POI_WEREWOLVIAN = DeferredHolder.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.parse("evilcraft:werewolvian"));

}
