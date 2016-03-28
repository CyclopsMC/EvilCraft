package org.cyclops.evilcraft.api.broom;

import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * A list of broom modifiers.
 * @author rubensworks
 */
public class BroomModifiers {

    public static final IBroomModifierRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomModifierRegistry.class);

    public static BroomModifier SPEED;
    public static BroomModifier ACCELERATION;
    public static BroomModifier MANEUVERABILITY;

    public static void load() {
        SPEED = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "speed"),
                BroomModifier.Type.ADDITIVE, 1F, true));
        ACCELERATION = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "acceleration"),
                BroomModifier.Type.MULTIPLICATIVE, 1F, true));
        MANEUVERABILITY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "maneuverability"),
                BroomModifier.Type.MULTIPLICATIVE, 1F, true));
    }

}
