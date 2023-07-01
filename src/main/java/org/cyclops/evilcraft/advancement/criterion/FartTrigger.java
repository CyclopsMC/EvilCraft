package org.cyclops.evilcraft.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.cyclops.cyclopscore.advancement.criterion.ICriterionInstanceTestable;
import org.cyclops.evilcraft.Reference;

/**
 * Triggers when a player farts.
 * @author rubensworks
 */
public class FartTrigger extends SimpleCriterionTrigger<FartTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "fart");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        return new Instance(getId(), entityPredicate);
    }

    public void test(ServerPlayer player) {
        this.trigger(player, (instance) -> instance.test(player, null));
    }

    public static class Instance extends AbstractCriterionTriggerInstance implements ICriterionInstanceTestable<Void> {
        public Instance(ResourceLocation criterionIn, ContextAwarePredicate player) {
            super(criterionIn, player);
        }

        public boolean test(ServerPlayer player, Void v) {
            return true;
        }
    }

}
