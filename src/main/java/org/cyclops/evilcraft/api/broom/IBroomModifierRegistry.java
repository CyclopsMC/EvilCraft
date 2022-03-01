package org.cyclops.evilcraft.api.broom;

import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.init.IRegistry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * Registry for broom modifiers.
 * @author rubensworks
 */
public interface IBroomModifierRegistry extends IRegistry {

    /**
     * Register a new modifier.
     * @param modifier The modifier.
     * @return The registered modifier.
     */
    public BroomModifier registerModifier(BroomModifier modifier);

    /**
     * Override the default modifier broom part.
     * @param modifier The modifier.
     * @param broomPart The broom part for the modifier.
     */
    public void overrideDefaultModifierPart(BroomModifier modifier, @Nullable IBroomPart broomPart);

    /**
     * Get the broom part for the given modifier.
     * @param modifier The modifier.
     * @return The broom part.
     */
    public @Nullable IBroomPart getModifierPart(BroomModifier modifier);

    /**
     * Clear the registry of all modifier item registrations.
     */
    public void clearModifierItems();

    /**
     * Assign an item to the given modifiers.
     * @param modifiers The modifiers.
     * @param item The item-form of this modifiers.
     */
    public void registerModifiersItem(Map<BroomModifier, Float> modifiers, ItemStack item);

    /**
     * Assign an item to the given modifier.
     * @param modifier The modifier.
     * @param modifierValue The modifier value
     * @param item The item-form of this modifiers.
     */
    public void registerModifiersItem(BroomModifier modifier, float modifierValue, ItemStack item);

    /**
     * Get the modifier of the given item-form.
     * @param item The item containing modifiers.
     * @return The modifiers map.
     */
    public Map<BroomModifier, Float> getModifiersFromItem(ItemStack item);

    /**
     * Get the item of a modifier
     * @param modifier The modifier.
     * @return The itemstacks map.
     */
    public Map<ItemStack, Float> getItemsFromModifier(BroomModifier modifier);

    /**
     * @return All modifiers.
     */
    public Collection<BroomModifier> getModifiers();

    /**
     * Get all applied modifiers on then given broom.
     * @param broomStack The broom.
     * @return The applied modifiers
     */
    public Map<BroomModifier, Float> getModifiers(ItemStack broomStack);

    /**
     * Apply the given modifiers to the given broom.
     * @param broomStack The broom.
     * @param modifiers The modifiers to set.
     */
    public void setModifiers(ItemStack broomStack, Map<BroomModifier, Float> modifiers);

}
