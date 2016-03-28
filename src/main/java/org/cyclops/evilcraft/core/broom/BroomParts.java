package org.cyclops.evilcraft.core.broom;

import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;

/**
 * Collection of all broom parts.
 * @author rubensworks
 */
public final class BroomParts {

    public static final IBroomPartRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomPartRegistry.class);

    public static IBroomPart ROD_WOOD;
    public static IBroomPart BRUSH_WHEAT;
    public static IBroomPart CAP_DARKGEM;

    public static void load() {
        ROD_WOOD = REGISTRY.registerPart(new BroomPartBase(new ResourceLocation(Reference.MOD_ID, "rod_wood"), IBroomPart.BroomPartType.ROD));
        BRUSH_WHEAT = REGISTRY.registerPart(new BroomPartBase(new ResourceLocation(Reference.MOD_ID, "brush_wheat"), IBroomPart.BroomPartType.BRUSH));
        CAP_DARKGEM = REGISTRY.registerPart(new BroomPartBase(new ResourceLocation(Reference.MOD_ID, "cap_darkgem"), IBroomPart.BroomPartType.CAP));
    }

}
