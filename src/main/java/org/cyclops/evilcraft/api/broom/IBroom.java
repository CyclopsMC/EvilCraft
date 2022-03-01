package org.cyclops.evilcraft.api.broom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * Indicates if an item is a broom.
 * @author rubensworks
 */
public interface IBroom {

    /**
     * All parts from the given broom.
     * @param itemStack The broom stack.
     * @return The broom parts.
     */
    public Collection<IBroomPart> getBroomParts(ItemStack itemStack);

    /**
     * All modifiers from the given broom.
     * @param itemStack The broom stack.
     * @return The broom modifiers.
     */
    public Map<BroomModifier, Float> getBroomModifiers(ItemStack itemStack);

    /**
     * If this broom can consume a given energy amount.
     * @param amount The energy amount to drain.
     * @param itemStack The broom stack.
     * @param entityLiving The mounted entity.
     * @return If the given amount can be drained.
     */
    public boolean canConsumeBroomEnergy(int amount, ItemStack itemStack, @Nullable LivingEntity entityLiving);

    /**
     * Consume a given energy amount.
     * @param amount The energy amount to drain.
     * @param itemStack The broom stack.
     * @param entityLiving The mounted entity.
     * @return The energy amount that was drained.
     */
    public int consumeBroom(int amount, ItemStack itemStack, @Nullable LivingEntity entityLiving);

}
