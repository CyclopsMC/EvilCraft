package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;
import org.cyclops.evilcraft.item.BroomPart;
import org.cyclops.evilcraft.item.DarkGem;

import java.util.Collections;
import java.util.Map;

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

    public static IBroomPart CAP_DARKGEM;

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

        CAP_DARKGEM = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "cap_darkgem"),
                IBroomPart.BroomPartType.CAP, 0.0625F));
    }

    public static void loadPost() {
        REGISTRY.registerPartItem(BRUSH_WHEAT, new ItemStack(Blocks.hay_block));
        REGISTRY.registerBaseModifiers(BroomModifiers.SPEED, 100F, BRUSH_WHEAT);
        REGISTRY.registerPartItem(BRUSH_WOOL, new ItemStack(Blocks.wool));
        Map<BroomModifier, Float> brushWoolModifiers = Maps.newHashMap();
        brushWoolModifiers.put(BroomModifiers.SPEED, 80F);
        brushWoolModifiers.put(BroomModifiers.LEVITATION, 100F);
        REGISTRY.registerBaseModifiers(brushWoolModifiers, BRUSH_WOOL);
        REGISTRY.registerBaseModifiers(BroomModifiers.LEVITATION, 200F, BRUSH_FEATHER);
        REGISTRY.registerBaseModifiers(BroomModifiers.MANEUVERABILITY, 100F, BRUSH_TWIG);
        REGISTRY.registerPartItem(BRUSH_LEAVES, new ItemStack(Blocks.leaves));
        REGISTRY.registerBaseModifiers(BroomModifiers.SPEED, 20F, BRUSH_LEAVES);

        REGISTRY.registerPartItem(CAP_DARKGEM, new ItemStack(DarkGem.getInstance()));
        REGISTRY.registerBaseModifiers(BroomModifiers.MANEUVERABILITY, 10F, CAP_DARKGEM);

        // Rod modifiers
        Map<BroomModifier, Float> rodWoodModifiers = Maps.newHashMap();
        rodWoodModifiers.put(BroomModifiers.MODIFIER_COUNT, 4F);
        rodWoodModifiers.put(BroomModifiers.SPEED, 100F);
        rodWoodModifiers.put(BroomModifiers.MANEUVERABILITY, 150F);
        rodWoodModifiers.put(BroomModifiers.ACCELERATION, 50F);
        rodWoodModifiers.put(BroomModifiers.LEVITATION, 20F);
        REGISTRY.registerBaseModifiers(rodWoodModifiers, ROD_WOOD);

        Map<BroomModifier, Float> rodStoneModifiers = Maps.newHashMap();
        rodStoneModifiers.put(BroomModifiers.MODIFIER_COUNT, 8F);
        rodStoneModifiers.put(BroomModifiers.SPEED, 50F);
        rodStoneModifiers.put(BroomModifiers.MANEUVERABILITY, 30F);
        rodStoneModifiers.put(BroomModifiers.ACCELERATION, 10F);
        REGISTRY.registerBaseModifiers(rodStoneModifiers, ROD_STONE);

        Map<BroomModifier, Float> rodBoneModifiers = Maps.newHashMap();
        rodBoneModifiers.put(BroomModifiers.MODIFIER_COUNT, 4F);
        rodBoneModifiers.put(BroomModifiers.SPEED, 150F);
        rodBoneModifiers.put(BroomModifiers.MANEUVERABILITY, 150F);
        rodBoneModifiers.put(BroomModifiers.ACCELERATION, 100F);
        REGISTRY.registerBaseModifiers(rodBoneModifiers, ROD_BONE);

        Map<BroomModifier, Float> rodBlazeModifiers = Maps.newHashMap();
        rodBlazeModifiers.put(BroomModifiers.MODIFIER_COUNT, 3F);
        rodBlazeModifiers.put(BroomModifiers.SPEED, 250F);
        rodBlazeModifiers.put(BroomModifiers.MANEUVERABILITY, 100F);
        rodBlazeModifiers.put(BroomModifiers.ACCELERATION, 100F);
        rodBlazeModifiers.put(BroomModifiers.LEVITATION, 50F);
        REGISTRY.registerBaseModifiers(rodBlazeModifiers, ROD_BLAZE);

        Map<BroomModifier, Float> rodReedModifiers = Maps.newHashMap();
        rodReedModifiers.put(BroomModifiers.MODIFIER_COUNT, 5F);
        rodReedModifiers.put(BroomModifiers.SPEED, 50F);
        rodReedModifiers.put(BroomModifiers.MANEUVERABILITY, 200F);
        rodReedModifiers.put(BroomModifiers.ACCELERATION, 200F);
        rodReedModifiers.put(BroomModifiers.LEVITATION, 30F);
        REGISTRY.registerBaseModifiers(rodReedModifiers, ROD_REED);

        Map<BroomModifier, Float> rodNetherrackModifiers = Maps.newHashMap();
        rodNetherrackModifiers.put(BroomModifiers.MODIFIER_COUNT, 8F);
        rodNetherrackModifiers.put(BroomModifiers.SPEED, 100F);
        rodNetherrackModifiers.put(BroomModifiers.MANEUVERABILITY, 50F);
        rodNetherrackModifiers.put(BroomModifiers.ACCELERATION, 100F);
        REGISTRY.registerBaseModifiers(rodNetherrackModifiers, ROD_NETHERRACK);

        // Automatically register remaining parts for parts that don't have a custom item.
        for (IBroomPart part : REGISTRY.getParts()) {
            if(REGISTRY.getItemFromPart(part) == null) {
                ItemStack itemStack = new ItemStack(BroomPart.getInstance());
                REGISTRY.setBroomParts(itemStack, Collections.singleton(part));
                REGISTRY.registerPartItem(part, itemStack);
            }
        }

    }

}
