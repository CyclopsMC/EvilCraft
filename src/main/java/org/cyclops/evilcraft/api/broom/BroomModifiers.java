package org.cyclops.evilcraft.api.broom;

import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.item.GarmonboziaConfig;

import java.util.Map;

/**
 * A list of broom modifiers.
 * @author rubensworks
 */
public class BroomModifiers {

    public static final IBroomModifierRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomModifierRegistry.class);

    public static BroomModifier MODIFIER_COUNT;
    public static BroomModifier SPEED;
    public static BroomModifier ACCELERATION;
    public static BroomModifier MANEUVERABILITY;

    public static void loadPre() {
        MODIFIER_COUNT = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "modifier_count"),
                BroomModifier.Type.ADDITIVE, 0F, 1F, 3, true));
        SPEED = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "speed"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true));
        ACCELERATION = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "acceleration"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true));
        MANEUVERABILITY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "maneuverability"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true));
    }

    protected static <K, V> Map<K, V> singleMap(K key, V value) {
        Map<K, V> map = Maps.newHashMap();
        map.put(key, value);
        return map;
    }

    public static void loadPost() {
        REGISTRY.registerModifiersItem(singleMap(MODIFIER_COUNT, 1F), new ItemStack(Items.nether_star));
        REGISTRY.registerModifiersItem(singleMap(MODIFIER_COUNT, 1F), new ItemStack(GarmonboziaConfig._instance.getItemInstance()));

        REGISTRY.registerModifiersItem(singleMap(SPEED, 1F), new ItemStack(Items.redstone));
        REGISTRY.registerModifiersItem(singleMap(SPEED, 9F), new ItemStack(Blocks.redstone_block));

        REGISTRY.registerModifiersItem(singleMap(ACCELERATION, 1F), new ItemStack(Items.coal));
        REGISTRY.registerModifiersItem(singleMap(ACCELERATION, 9F), new ItemStack(Blocks.coal_block));

        REGISTRY.registerModifiersItem(singleMap(MANEUVERABILITY, 2F), new ItemStack(Items.glowstone_dust));
        REGISTRY.registerModifiersItem(singleMap(MANEUVERABILITY, 8F), new ItemStack(Blocks.glowstone));
    }

}
