package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;

import java.util.Collections;

/**
 * Collection of all broom parts.
 * @author rubensworks
 */
public final class BroomParts {

    public static final IBroomPartRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomPartRegistry.class);

    public static void init() {

    }

    public static IBroomPart ROD_BARE;
    public static IBroomPart ROD_WOOD;
    public static IBroomPart ROD_STONE;
    public static IBroomPart ROD_BONE;
    public static IBroomPart ROD_BLAZE;
    public static IBroomPart ROD_REED;
    public static IBroomPart ROD_NETHERRACK;
    public static IBroomPart ROD_OBSIDIAN;
    public static IBroomPart ROD_UNDEAD;
    public static IBroomPart ROD_PRISMARINE;
    public static IBroomPart ROD_ICE;
    public static IBroomPart ROD_SPONGE;
    public static IBroomPart ROD_ENDSTONE;
    public static IBroomPart ROD_PURPUR;
    public static IBroomPart ROD_BAMBOO;

    public static IBroomPart BRUSH_BARE;
    public static IBroomPart BRUSH_WHEAT;
    public static IBroomPart BRUSH_WOOL;
    public static IBroomPart BRUSH_FEATHER;
    public static IBroomPart BRUSH_TWIG;
    public static IBroomPart BRUSH_LEAVES;
    public static IBroomPart BRUSH_HONEY;

    public static IBroomPart CAP_BARE;
    public static IBroomPart CAP_GEM_DARK;
    public static IBroomPart CAP_GEM_DIAMOND;
    public static IBroomPart CAP_GEM_EMERALD;
    public static IBroomPart CAP_GEM_QUARTZ;
    //public static IBroomPart CAP_GEM_LAPIS;
    public static IBroomPart CAP_GEM_DARKPOWER;

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

    public static IBroomPart CAP_SLIME;

    public static void loadPre() {
        ROD_BARE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_bare"),
                IBroomPart.BroomPartType.ROD, 1F));
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
        ROD_OBSIDIAN = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_obsidian"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_UNDEAD = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_undead"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_PRISMARINE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_prismarine"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_ICE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_ice"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_SPONGE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_sponge"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_ENDSTONE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_endstone"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_PURPUR = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_purpur"),
                IBroomPart.BroomPartType.ROD, 1F));
        ROD_BAMBOO = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_bamboo"),
                IBroomPart.BroomPartType.ROD, 1F));

        BRUSH_BARE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "brush_bare"),
                IBroomPart.BroomPartType.BRUSH, 0.4375F));
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
        BRUSH_HONEY = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "brush_honey"),
                IBroomPart.BroomPartType.BRUSH, 0.4375F));

        CAP_BARE = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "cap_bare"),
                IBroomPart.BroomPartType.CAP, 0.0625F));

        CAP_GEM_DARK = registerCapGem("dark", 55, 55, 55);
        CAP_GEM_DIAMOND = registerCapGem("diamond", 105, 223, 218);
        CAP_GEM_EMERALD = registerCapGem("emerald", 66, 216, 109);
        CAP_GEM_QUARTZ = registerCapGem("quartz", 237, 235, 228);
        //CAP_GEM_LAPIS = registerCapGemOredict("lapis", 38, 79, 162);
        CAP_GEM_DARKPOWER = registerCapGem("dark_power", 112, 59, 59);

        CAP_HEAD_SKELETON = registerCapHead("skeleton");
        CAP_HEAD_WITHERSKELETON = registerCapHead("witherskeleton");
        CAP_HEAD_ZOMBIE = registerCapHead("zombie");
        CAP_HEAD_PLAYER = registerCapHead("player");
        CAP_HEAD_CREEPER = registerCapHead("creeper");
        CAP_HEAD_WITHER = registerCapHead("wither");

        CAP_METAL_IRON = registerCapMetal("iron", 216, 216, 216);
        CAP_METAL_GOLD = registerCapMetal("gold", 255, 255, 139);
        CAP_METAL_COPPER = registerCapMetal("copper", 167, 108, 68);
        CAP_METAL_SILVER = registerCapMetal("silver", 123, 135, 120);
        CAP_METAL_BRASS = registerCapMetal("alubrass", 230, 195, 75);
        CAP_METAL_ARDITE = registerCapMetal("ardite", 214, 71, 0);
        CAP_METAL_COBALT = registerCapMetal("cobalt", 31, 126, 239);
        CAP_METAL_MANYULLYN = registerCapMetal("manyullyn", 117, 58, 159);

        CAP_SLIME = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "cap_slime"),
                IBroomPart.BroomPartType.CAP, 0.0625F));
    }

    public static void loadPost() {
        for (IBroomPart part : REGISTRY.getParts()) {
            if (part.shouldAutoRegisterMissingItem() && REGISTRY.getItemsFromPart(part).isEmpty()) {
                ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_BROOM_PART);
                REGISTRY.setBroomParts(itemStack, Collections.singleton(part));
                REGISTRY.registerPartItem(part, itemStack);
            }
        }

        // ---------- Rod modifiers ----------
        REGISTRY.registerBaseModifiers(ROD_WOOD, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 4F,
                BroomModifiers.SPEED, 100F,
                BroomModifiers.MANEUVERABILITY, 150F,
                BroomModifiers.ACCELERATION, 50F,
                BroomModifiers.LEVITATION, 20F
        ));
        REGISTRY.registerBaseModifiers(ROD_STONE, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 6F,
                BroomModifiers.SPEED, 50F,
                BroomModifiers.MANEUVERABILITY, 30F,
                BroomModifiers.ACCELERATION, 10F,
                BroomModifiers.STURDYNESS, 40F
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
        REGISTRY.registerBaseModifiers(ROD_OBSIDIAN, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 8F,
                BroomModifiers.SPEED, 70F,
                BroomModifiers.MANEUVERABILITY, 50F,
                BroomModifiers.ACCELERATION, 10F,
                BroomModifiers.STURDYNESS, 100F
        ));
        REGISTRY.registerBaseModifiers(ROD_UNDEAD, new ImmutableMap.Builder<BroomModifier, Float>()
                        .put(BroomModifiers.MODIFIER_COUNT, 4F)
                        .put(BroomModifiers.SPEED, 120F)
                        .put(BroomModifiers.MANEUVERABILITY, 150F)
                        .put(BroomModifiers.ACCELERATION, 50F)
                        .put(BroomModifiers.LEVITATION, 20F)
                        .put(BroomModifiers.EFFICIENCY, 10F)
                        .build()
        );
        REGISTRY.registerBaseModifiers(ROD_PRISMARINE, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 4F,
                BroomModifiers.SPEED, 70F,
                BroomModifiers.ACCELERATION, 10F,
                BroomModifiers.STURDYNESS, 50F,
                BroomModifiers.SWIMMING, 100F
        ));
        REGISTRY.registerBaseModifiers(ROD_ICE, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 4F,
                BroomModifiers.SPEED, 70F,
                BroomModifiers.ACCELERATION, 50F,
                BroomModifiers.STURDYNESS, 50F,
                BroomModifiers.ICY, 10F
        ));
        REGISTRY.registerBaseModifiers(ROD_SPONGE, new ImmutableMap.Builder<BroomModifier, Float>()
                        .put(BroomModifiers.MODIFIER_COUNT, 4F)
                        .put(BroomModifiers.SPEED, 50F)
                        .put(BroomModifiers.ACCELERATION, 10F)
                        .put(BroomModifiers.STURDYNESS, 80F)
                        .put(BroomModifiers.SWIMMING, 120F)
                        .put(BroomModifiers.BOUNCY, 2F)
                        .build()
        );
        REGISTRY.registerBaseModifiers(ROD_ENDSTONE, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 6F,
                BroomModifiers.SPEED, 80F,
                BroomModifiers.MANEUVERABILITY, 50F,
                BroomModifiers.ACCELERATION, 50F,
                BroomModifiers.STURDYNESS, 50F
        ));
        REGISTRY.registerBaseModifiers(ROD_PURPUR, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 5F,
                BroomModifiers.SPEED, 100F,
                BroomModifiers.MANEUVERABILITY, 75F,
                BroomModifiers.ACCELERATION, 50F,
                BroomModifiers.LEVITATION, 100F
        ));
        REGISTRY.registerBaseModifiers(ROD_BAMBOO, ImmutableMap.of(
                BroomModifiers.MODIFIER_COUNT, 6F,
                BroomModifiers.SPEED, 100F,
                BroomModifiers.MANEUVERABILITY, 150F,
                BroomModifiers.ACCELERATION, 50F,
                BroomModifiers.STURDYNESS, 40F
        ));

        // ---------- Brushes ----------
        REGISTRY.registerBaseModifiers(BRUSH_WHEAT, BroomModifiers.SPEED, 100F);
        REGISTRY.registerBaseModifiers(BRUSH_WOOL, ImmutableMap.of(
                BroomModifiers.SPEED, 80F,
                BroomModifiers.LEVITATION, 100F
        ));
        REGISTRY.registerBaseModifiers(BRUSH_FEATHER, BroomModifiers.LEVITATION, 200F);
        REGISTRY.registerBaseModifiers(BRUSH_TWIG, BroomModifiers.MANEUVERABILITY, 100F);
        REGISTRY.registerBaseModifiers(BRUSH_LEAVES, BroomModifiers.SPEED, 20F);
        REGISTRY.registerBaseModifiers(BRUSH_HONEY, ImmutableMap.of(
                BroomModifiers.STURDYNESS, 100F,
                BroomModifiers.STICKY, 10F
        ));

        // ---------- Caps ----------
        REGISTRY.registerBaseModifiers(CAP_GEM_DARK, ImmutableMap.of(
                BroomModifiers.MANEUVERABILITY, 50F,
                BroomModifiers.MODIFIER_COUNT, 1F
        ));
        REGISTRY.registerBaseModifiers(CAP_GEM_DIAMOND, ImmutableMap.of(
                BroomModifiers.MANEUVERABILITY, 50F,
                BroomModifiers.MODIFIER_COUNT, 2F,
                BroomModifiers.STURDYNESS, 100F
        ));
        REGISTRY.registerBaseModifiers(CAP_GEM_EMERALD, ImmutableMap.of(
                BroomModifiers.MANEUVERABILITY, 50F,
                BroomModifiers.MODIFIER_COUNT, 2F,
                BroomModifiers.ACCELERATION, 100F
        ));
        REGISTRY.registerBaseModifiers(CAP_GEM_QUARTZ, ImmutableMap.of(
                BroomModifiers.MANEUVERABILITY, 50F,
                BroomModifiers.DAMAGE, 50F
        ));
        /*REGISTRY.registerBaseModifiers(CAP_GEM_LAPIS, ImmutableMap.of(
                BroomModifiers.MANEUVERABILITY, 50F,
                BroomModifiers.LUCK, 50F
        ));*/
        REGISTRY.registerBaseModifiers(CAP_GEM_DARKPOWER, ImmutableMap.of(
                BroomModifiers.MANEUVERABILITY, 50F,
                BroomModifiers.MODIFIER_COUNT, 1F,
                BroomModifiers.EFFICIENCY, 10F
        ));

        REGISTRY.registerBaseModifiers(CAP_HEAD_SKELETON, BroomModifiers.MANEUVERABILITY, 150F);
        REGISTRY.registerBaseModifiers(CAP_HEAD_WITHERSKELETON, BroomModifiers.WITHERER, 5F);
        REGISTRY.registerBaseModifiers(CAP_HEAD_ZOMBIE, ImmutableMap.of(
                BroomModifiers.HUNGERER, 10F,
                BroomModifiers.DAMAGE, 2F
        ));
        // Later on, we could apply player skin textures, but that'd require a lot of hacking.
        // Because textures are applied at bake time, and player skins may require downloading time, so yeah...
        REGISTRY.registerBaseModifiers(CAP_HEAD_PLAYER, BroomModifiers.SPEED, 50F);
        REGISTRY.registerBaseModifiers(CAP_HEAD_CREEPER, BroomModifiers.KAMIKAZE, 10F);
        REGISTRY.registerBaseModifiers(CAP_HEAD_WITHER, BroomModifiers.WITHERSHIELD, 20F);

        REGISTRY.registerBaseModifiers(CAP_METAL_IRON, ImmutableMap.of(
                BroomModifiers.SPEED, 100F,
                BroomModifiers.STURDYNESS, 40F,
                BroomModifiers.MANEUVERABILITY, 10F
        ));
        REGISTRY.registerBaseModifiers(CAP_METAL_GOLD, ImmutableMap.of(
                BroomModifiers.SPEED, 80F,
                BroomModifiers.STURDYNESS, 70F,
                BroomModifiers.MANEUVERABILITY, 30F
        ));
        REGISTRY.registerBaseModifiers(CAP_METAL_THAUMIUM, ImmutableMap.of(
                BroomModifiers.SPEED, 100F,
                BroomModifiers.STURDYNESS, 70F,
                BroomModifiers.MANEUVERABILITY, 30F
        ));
        REGISTRY.registerBaseModifiers(CAP_METAL_COPPER, ImmutableMap.of(
                BroomModifiers.SPEED, 120F,
                BroomModifiers.STURDYNESS, 50F,
                BroomModifiers.MANEUVERABILITY, 10F
        ));
        REGISTRY.registerBaseModifiers(CAP_METAL_SILVER, ImmutableMap.of(
                BroomModifiers.SPEED, 130F,
                BroomModifiers.STURDYNESS, 50F,
                BroomModifiers.MANEUVERABILITY, 10F
        ));
        REGISTRY.registerBaseModifiers(CAP_METAL_BRASS, ImmutableMap.of(
                BroomModifiers.SPEED, 80F,
                BroomModifiers.STURDYNESS, 60F,
                BroomModifiers.MANEUVERABILITY, 30F
        ));
        REGISTRY.registerBaseModifiers(CAP_METAL_ARDITE, ImmutableMap.of(
                BroomModifiers.SPEED, 150F,
                BroomModifiers.STURDYNESS, 50F,
                BroomModifiers.MANEUVERABILITY, 20F
        ));
        REGISTRY.registerBaseModifiers(CAP_METAL_COBALT, ImmutableMap.of(
                BroomModifiers.SPEED, 130F,
                BroomModifiers.STURDYNESS, 60F,
                BroomModifiers.MANEUVERABILITY, 20F
        ));
        REGISTRY.registerBaseModifiers(CAP_METAL_MANYULLYN, ImmutableMap.of(
                BroomModifiers.SPEED, 150F,
                BroomModifiers.STURDYNESS, 60F,
                BroomModifiers.MANEUVERABILITY, 20F
        ));

        REGISTRY.registerBaseModifiers(CAP_SLIME, ImmutableMap.of(
                BroomModifiers.SPEED, 50F,
                BroomModifiers.ACCELERATION, 150F,
                BroomModifiers.STURDYNESS, 30F,
                BroomModifiers.BOUNCY, 15F
        ));

        int combinations =
                  REGISTRY.getParts(IBroomPart.BroomPartType.ROD).size()
                * REGISTRY.getParts(IBroomPart.BroomPartType.CAP).size()
                * REGISTRY.getParts(IBroomPart.BroomPartType.BRUSH).size();
        EvilCraft.clog(String.format("%s possible Broom base combinations are ready for flying!", combinations));
    }

    public static IBroomPart registerCapGem(String name, int r, int g, int b) {
        return REGISTRY.registerPart(new BroomPartCapGem(
                new ResourceLocation(Reference.MOD_ID, "cap_gem_" + name),
                Helpers.RGBToInt(r, g, b)));
    }

    public static IBroomPart registerCapHead(String name) {
        return REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "cap_head_" + name),
                IBroomPart.BroomPartType.CAP, 0.5F));
    }

    public static IBroomPart registerCapMetal(String name, int r, int g, int b) {
        return REGISTRY.registerPart(new BroomPartCapMetal(
                new ResourceLocation(Reference.MOD_ID, "cap_metal_" + name),
                Helpers.RGBToInt(r, g, b)));
    }
}
