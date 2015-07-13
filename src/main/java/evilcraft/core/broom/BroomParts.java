package evilcraft.core.broom;

import evilcraft.EvilCraft;
import evilcraft.api.broom.IBroomPart;
import evilcraft.api.broom.IBroomPartRegistry;

/**
 * Collection of all broom parts.
 * @author rubensworks
 */
public final class BroomParts {

    public static final IBroomPartRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomPartRegistry.class);

    // These are just dummy parts for now
    public static final IBroomPart ROD = REGISTRY.registerPart(new BroomPartBase(IBroomPart.BroomPartType.ROD));
    public static final IBroomPart BRUSH = REGISTRY.registerPart(new BroomPartBase(IBroomPart.BroomPartType.BRUSH));
    public static final IBroomPart CAP = REGISTRY.registerPart(new BroomPartBase(IBroomPart.BroomPartType.CAP));

}
