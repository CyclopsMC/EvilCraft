package org.cyclops.evilcraft.api.broom;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.item.DarkSpikeConfig;
import org.cyclops.evilcraft.item.GarmonboziaConfig;

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

    public static BroomModifier DAMAGE;

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

        DAMAGE = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "damage"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false));
    }

    public static void loadPost() {
        REGISTRY.registerModifiersItem(MODIFIER_COUNT, 1F, new ItemStack(Items.nether_star));
        REGISTRY.registerModifiersItem(MODIFIER_COUNT, 1F, new ItemStack(GarmonboziaConfig._instance.getItemInstance()));

        REGISTRY.registerModifiersItem(SPEED, 1F, new ItemStack(Items.redstone));
        REGISTRY.registerModifiersItem(SPEED, 9F, new ItemStack(Blocks.redstone_block));

        REGISTRY.registerModifiersItem(ACCELERATION, 1F, new ItemStack(Items.coal));
        REGISTRY.registerModifiersItem(ACCELERATION, 9F, new ItemStack(Blocks.coal_block));

        REGISTRY.registerModifiersItem(MANEUVERABILITY, 2F, new ItemStack(Items.glowstone_dust));
        REGISTRY.registerModifiersItem(MANEUVERABILITY, 8F, new ItemStack(Blocks.glowstone));

        REGISTRY.registerModifiersItem(DAMAGE, 2F, new ItemStack(DarkSpikeConfig._instance.getItemInstance()));
        REGISTRY.registerModifiersItem(DAMAGE, 1F, new ItemStack(Items.quartz));
    }

}
