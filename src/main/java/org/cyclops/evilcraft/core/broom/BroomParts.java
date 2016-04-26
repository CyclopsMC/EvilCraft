package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;
import org.cyclops.evilcraft.item.BroomPart;
import org.cyclops.evilcraft.item.DarkGem;

import java.util.Collections;

/**
 * Collection of all broom parts.
 * @author rubensworks
 */
public final class BroomParts {

    public static final IBroomPartRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomPartRegistry.class);

    public static IBroomPart ROD_WOOD;
    public static IBroomPart ROD_STONE;
    public static IBroomPart ROD_BONE;
    public static IBroomPart ROD_BLAZE;
    public static IBroomPart ROD_REED;
    public static IBroomPart ROD_NETHERRACK;

    public static IBroomPart BRUSH_WHEAT;
    public static IBroomPart BRUSH_WOOL;
    public static IBroomPart BRUSH_FEATHER;
    public static IBroomPart BRUSH_TWIG;
    public static IBroomPart BRUSH_LEAVES;

    public static IBroomPart CAP_GEM_DARKGEM;
    public static IBroomPart CAP_HEAD_SKELETON;
    public static IBroomPart CAP_HEAD_WITHERSKELETON;
    public static IBroomPart CAP_HEAD_ZOMBIE;
    public static IBroomPart CAP_HEAD_PLAYER;
    public static IBroomPart CAP_HEAD_CREEPER;
    public static IBroomPart CAP_HEAD_WITHER;
    public static IBroomPart CAP_METAL_IRON;
    public static IBroomPart CAP_METAL_GOLD;

    public static IBroomPart CAP_METAL_THAUMIUM;
    public static IBroomPart CAP_METAL_COPPER;
    public static IBroomPart CAP_METAL_SILVER;
    public static IBroomPart CAP_METAL_BRASS;
    public static IBroomPart CAP_METAL_ARDITE;
    public static IBroomPart CAP_METAL_COBALT;
    public static IBroomPart CAP_METAL_MANYULLYN;

    public static void loadPre() {
        ROD_WOOD = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_wood"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_STONE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_stone"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_BONE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_bone"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_BLAZE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_blaze"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_REED = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_reed"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_NETHERRACK = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_netherrack"),
                IBroomPart.BroomPartType.ROD, 1F));

        BRUSH_WHEAT = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "brush_wheat"),
                IBroomPart.BroomPartType.BRUSH, 0.4375F));
        BRUSH_WOOL = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "brush_wool"),
                IBroomPart.BroomPartType.BRUSH, 0.4375F));
        BRUSH_FEATHER = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "brush_feather"),
                IBroomPart.BroomPartType.BRUSH, 0.4375F));
        BRUSH_TWIG = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "brush_twig"),
                IBroomPart.BroomPartType.BRUSH, 0.4375F));
        BRUSH_LEAVES = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "brush_leaves"),
                IBroomPart.BroomPartType.BRUSH, 0.4375F));

        CAP_GEM_DARKGEM = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "cap_gem_darkgem"),
                IBroomPart.BroomPartType.CAP, 0.0625F));

        CAP_HEAD_SKELETON = registerCapHead("skeleton");
        CAP_HEAD_WITHERSKELETON = registerCapHead("witherskeleton");
        CAP_HEAD_ZOMBIE = registerCapHead("zombie");
        CAP_HEAD_PLAYER = registerCapHead("player");
        CAP_HEAD_CREEPER = registerCapHead("creeper");
        CAP_HEAD_WITHER = registerCapHead("wither");

        CAP_METAL_IRON = registerCapMetalOredict("iron", 216, 216, 216);
        CAP_METAL_GOLD = registerCapMetalOredict("gold", 255, 255, 139);
        CAP_METAL_THAUMIUM = registerCapMetalOredict("thaumium", 98, 81, 151);
        CAP_METAL_COPPER = registerCapMetalOredict("copper", 167, 108, 68);
        CAP_METAL_SILVER = registerCapMetalOredict("silver", 123, 135, 120);
        CAP_METAL_BRASS = registerCapMetalOredict("alubrass", 230, 195, 75);
        CAP_METAL_ARDITE = registerCapMetalOredict("ardite", 214, 71, 0);
        CAP_METAL_COBALT = registerCapMetalOredict("cobalt", 31, 126, 239);
        CAP_METAL_MANYULLYN = registerCapMetalOredict("manyullyn", 117, 58, 159);
    }

    public static void loadPost() {
        // ---------- Brushes ----------
        REGISTRY.registerPartItem(BRUSH_WHEAT, new ItemStack(Blocks.hay_block));
        REGISTRY.registerBaseModifiers(BRUSH_WHEAT, BroomModifiers.SPEED, 100F);
        REGISTRY.registerPartItem(BRUSH_WOOL, new ItemStack(Blocks.wool));
        REGISTRY.registerBaseModifiers(BRUSH_WOOL, ImmutableMap.of(
                BroomModifiers.SPEED, 80F,
                BroomModifiers.LEVITATION, 100F
        ));
        REGISTRY.registerBaseModifiers(BRUSH_FEATHER, BroomModifiers.LEVITATION, 200F);
        REGISTRY.registerBaseModifiers(BRUSH_TWIG, BroomModifiers.MANEUVERABILITY, 100F);
        REGISTRY.registerPartItem(BRUSH_LEAVES, new ItemStack(Blocks.leaves));
        REGISTRY.registerBaseModifiers(BRUSH_LEAVES, BroomModifiers.SPEED, 20F);

        // ---------- Caps ----------
        REGISTRY.registerPartItem(CAP_GEM_DARKGEM, new ItemStack(DarkGem.getInstance()));
        REGISTRY.registerBaseModifiers(CAP_GEM_DARKGEM, BroomModifiers.MANEUVERABILITY, 10F);

        REGISTRY.registerPartItem(CAP_HEAD_SKELETON, new ItemStack(Items.skull, 1, 0));
        REGISTRY.registerBaseModifiers(CAP_HEAD_SKELETON, BroomModifiers.MANEUVERABILITY, 150F);
        REGISTRY.registerPartItem(CAP_HEAD_WITHERSKELETON, new ItemStack(Items.skull, 1, 1));
        REGISTRY.registerBaseModifiers(CAP_HEAD_WITHERSKELETON, BroomModifiers.WITHERER, 5F);
        REGISTRY.registerPartItem(CAP_HEAD_ZOMBIE, new ItemStack(Items.skull, 1, 2));
        REGISTRY.registerBaseModifiers(CAP_HEAD_ZOMBIE, ImmutableMap.of(
                BroomModifiers.HUNGERER, 10F,
                BroomModifiers.DAMAGE, 2F
        ));
        // Later on, we could apply player skin textures, but that'd require a lot of hacking.
        // Because textures are applied at bake time, and player skins may require downloading time, so yeah...
        REGISTRY.registerPartItem(CAP_HEAD_PLAYER, new ItemStack(Items.skull, 1, 3));
        REGISTRY.registerBaseModifiers(CAP_HEAD_PLAYER, BroomModifiers.SPEED, 50F);
        REGISTRY.registerPartItem(CAP_HEAD_CREEPER, new ItemStack(Items.skull, 1, 4));
        REGISTRY.registerBaseModifiers(CAP_HEAD_CREEPER, BroomModifiers.KAMIKAZE, 10F);
        REGISTRY.registerBaseModifiers(CAP_HEAD_WITHER, BroomModifiers.WITHERSHIELD, 20F);

        registerPartOredictItem(CAP_METAL_IRON, "ingotIron");
        REGISTRY.registerBaseModifiers(CAP_METAL_IRON, ImmutableMap.of(
                BroomModifiers.SPEED, 40F,
                BroomModifiers.TOUGHNESS, 40F,
                BroomModifiers.MANEUVERABILITY, 10F
        ));
        registerPartOredictItem(CAP_METAL_GOLD, "ingotGold");
        REGISTRY.registerBaseModifiers(CAP_METAL_GOLD, ImmutableMap.of(
                BroomModifiers.SPEED, 30F,
                BroomModifiers.TOUGHNESS, 70F,
                BroomModifiers.MANEUVERABILITY, 30F
        ));
        registerPartOredictItem(CAP_METAL_THAUMIUM, "ingotThaumium");
        REGISTRY.registerBaseModifiers(CAP_METAL_THAUMIUM, ImmutableMap.of(
                BroomModifiers.SPEED, 40F,
                BroomModifiers.TOUGHNESS, 70F,
                BroomModifiers.MANEUVERABILITY, 30F
        ));
        registerPartOredictItem(CAP_METAL_COPPER, "ingotCopper");
        REGISTRY.registerBaseModifiers(CAP_METAL_COPPER, ImmutableMap.of(
                BroomModifiers.SPEED, 45F,
                BroomModifiers.TOUGHNESS, 50F,
                BroomModifiers.MANEUVERABILITY, 10F
        ));
        registerPartOredictItem(CAP_METAL_SILVER, "ingotSilver");
        REGISTRY.registerBaseModifiers(CAP_METAL_SILVER, ImmutableMap.of(
                BroomModifiers.SPEED, 50F,
                BroomModifiers.TOUGHNESS, 50F,
                BroomModifiers.MANEUVERABILITY, 10F
        ));
        registerPartOredictItem(CAP_METAL_ARDITE, "ingotArdite");
        REGISTRY.registerBaseModifiers(CAP_METAL_ARDITE, ImmutableMap.of(
                BroomModifiers.SPEED, 60F,
                BroomModifiers.TOUGHNESS, 50F,
                BroomModifiers.MANEUVERABILITY, 20F
        ));
        registerPartOredictItem(CAP_METAL_COBALT, "ingotCobalt");
        REGISTRY.registerBaseModifiers(CAP_METAL_COBALT, ImmutableMap.of(
                BroomModifiers.SPEED, 50F,
                BroomModifiers.TOUGHNESS, 60F,
                BroomModifiers.MANEUVERABILITY, 20F
        ));
        registerPartOredictItem(CAP_METAL_MANYULLYN, "ingotManyullyn");
        REGISTRY.registerBaseModifiers(CAP_METAL_MANYULLYN, ImmutableMap.of(
                BroomModifiers.SPEED, 60F,
                BroomModifiers.TOUGHNESS, 60F,
                BroomModifiers.MANEUVERABILITY, 20F
        ));

        // ---------- Rod modifiers ----------
        REGISTRY.registerBaseModifiers(ROD_WOOD, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 4F,
                BroomModifiers.SPEED, 100F,
                BroomModifiers.MANEUVERABILITY, 150F,
                BroomModifiers.ACCELERATION, 50F,
                BroomModifiers.LEVITATION, 20F
        ));
        REGISTRY.registerBaseModifiers(ROD_STONE, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 8F,
                BroomModifiers.SPEED, 50F,
                BroomModifiers.MANEUVERABILITY, 30F,
                BroomModifiers.ACCELERATION, 10F,
                BroomModifiers.TOUGHNESS, 20F
        ));
        REGISTRY.registerBaseModifiers(ROD_BONE, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 4F,
                BroomModifiers.SPEED, 150F,
                BroomModifiers.MANEUVERABILITY, 150F,
                BroomModifiers.ACCELERATION, 100F
        ));
        REGISTRY.registerBaseModifiers(ROD_BLAZE, new ImmutableMap.Builder<BroomModifier, Float>()
                        .put(BroomModifiers.MODIFIER_COUNT, 3F)
                        .put(BroomModifiers.SPEED, 250F)
                        .put(BroomModifiers.MANEUVERABILITY, 100F)
                        .put(BroomModifiers.ACCELERATION, 100F)
                        .put(BroomModifiers.LEVITATION, 50F)
                        .put(BroomModifiers.FLAME, 2F)
                        .build()
        );
        REGISTRY.registerBaseModifiers(ROD_REED, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 5F,
                BroomModifiers.SPEED, 50F,
                BroomModifiers.MANEUVERABILITY, 200F,
                BroomModifiers.ACCELERATION, 200F,
                BroomModifiers.LEVITATION, 30F
        ));
        REGISTRY.registerBaseModifiers(ROD_NETHERRACK, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 8F,
                BroomModifiers.SPEED, 100F,
                BroomModifiers.MANEUVERABILITY, 50F,
                BroomModifiers.ACCELERATION, 100F
        ));

        // Automatically register remaining parts for parts that don't have a custom item.
        for (IBroomPart part : REGISTRY.getParts()) {
            if (part.shouldAutoRegisterMissingItem() && REGISTRY.getItemsFromPart(part).isEmpty()) {
                ItemStack itemStack = new ItemStack(BroomPart.getInstance());
                REGISTRY.setBroomParts(itemStack, Collections.singleton(part));
                REGISTRY.registerPartItem(part, itemStack);
            }
        }
    }

    public static IBroomPart registerCapHead(String name) {
        return REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "cap_head_" + name),
                IBroomPart.BroomPartType.CAP, 0.5F));
    }

    public static IBroomPart registerCapMetalOredict(String name, int r, int g, int b) {
        if(OreDictionary.doesOreNameExist("ingot" + StringUtils.capitalize(name))) {
            return REGISTRY.registerPart(new BroomPartCapMetal(
                    new ResourceLocation(Reference.MOD_ID, "cap_metal_" + name),
                    Helpers.RGBToInt(r, g, b)));
        }
        return null;
    }

    public static void registerPartOredictItem(IBroomPart part, String name) {
        for (ItemStack itemStack : OreDictionary.getOres(name)) {
            REGISTRY.registerPartItem(part, itemStack);
        }
    }

}
