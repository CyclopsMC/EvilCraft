package org.cyclops.evilcraft.core.broom;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
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
        ItemStack rodWoodStack = new ItemStack(BroomPart.getInstance());
        REGISTRY.setBroomParts(rodWoodStack, Collections.singleton(ROD_WOOD));
        REGISTRY.registerPartItem(ROD_WOOD, rodWoodStack);

        REGISTRY.registerPartItem(BRUSH_WHEAT, new ItemStack(Blocks.hay_block));

        REGISTRY.registerPartItem(CAP_DARKGEM, new ItemStack(DarkGem.getInstance()));
    }

}
