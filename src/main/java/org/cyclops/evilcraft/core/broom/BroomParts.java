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

    public static IBroomPart BRUSH_WHEAT;

    public static IBroomPart CAP_DARKGEM;

    public static void loadPre() {
        ROD_WOOD = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "rod_wood"),
                IBroomPart.BroomPartType.ROD, 1F));

        BRUSH_WHEAT = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "brush_wheat"),
                IBroomPart.BroomPartType.BRUSH, 0.4375F));

        CAP_DARKGEM = REGISTRY.registerPart(new BroomPartBase(
                new ResourceLocation(Reference.MOD_ID, "cap_darkgem"),
                IBroomPart.BroomPartType.CAP, 0.0625F));
    }

    public static void loadPost() {
        REGISTRY.registerPartItem(BRUSH_WHEAT, new ItemStack(Blocks.hay_block));
        Map<BroomModifier, Float> hayBlockMap = Maps.newHashMap();
        hayBlockMap.put(BroomModifiers.SPEED, 10F);
        REGISTRY.registerBaseModifiers(BRUSH_WHEAT, hayBlockMap);

        REGISTRY.registerPartItem(CAP_DARKGEM, new ItemStack(DarkGem.getInstance()));
        Map<BroomModifier, Float> darkGemMap = Maps.newHashMap();
        hayBlockMap.put(BroomModifiers.MANEUVERABILITY, 10F);
        REGISTRY.registerBaseModifiers(CAP_DARKGEM, darkGemMap);

        Map<BroomModifier, Float> woodRodMap = Maps.newHashMap();
        woodRodMap.put(BroomModifiers.MODIFIER_COUNT, 4F);
        REGISTRY.registerBaseModifiers(ROD_WOOD, woodRodMap);

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
