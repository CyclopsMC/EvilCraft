package org.cyclops.evilcraft.api.broom;

import com.google.common.collect.Maps;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

import java.util.Map;

/**
 * A list of broom modifiers.
 * @author rubensworks
 */
public class BroomModifiers {

    public static final IBroomModifierRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomModifierRegistry.class);

    public static BroomModifier SPEED;
    public static BroomModifier ACCELERATION;
    public static BroomModifier MANEUVERABILITY;

    public static void loadPre() {
        SPEED = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "speed"),
                BroomModifier.Type.ADDITIVE, 1F, true));
        ACCELERATION = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "acceleration"),
                BroomModifier.Type.ADDITIVE, 1F, true));
        MANEUVERABILITY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "maneuverability"),
                BroomModifier.Type.ADDITIVE, 1F, true));
    }

    public static void loadPost() {
        Map<BroomModifier, Float> redstoneMap = Maps.newHashMap();
        redstoneMap.put(SPEED, 1F);
        REGISTRY.registerModifiersItem(redstoneMap, new ItemStack(Items.redstone));

        Map<BroomModifier, Float> coalMap = Maps.newHashMap();
        coalMap.put(ACCELERATION, 1F);
        REGISTRY.registerModifiersItem(coalMap, new ItemStack(Items.coal));

        Map<BroomModifier, Float> glowstoneMap = Maps.newHashMap();
        glowstoneMap.put(MANEUVERABILITY, 1F);
        REGISTRY.registerModifiersItem(glowstoneMap, new ItemStack(Items.glowstone_dust));
    }

}
