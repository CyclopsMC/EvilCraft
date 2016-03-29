package org.cyclops.evilcraft.api.broom;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.init.IRegistry;

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
     * Assign an item to the given modifiers.
     * @param modifiers The modifiers.
     * @param item The item-form of this modifiers.
     */
    public void registerModifiersItem(Map<BroomModifier, Float> modifiers, ItemStack item);

    /**
     * Get the modifier of the given item-form.
     * @param item The item containing modifiers.
     * @return The modifier or null.
     */
    public Map<BroomModifier, Float> getModifiersFromItem(ItemStack item);

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
