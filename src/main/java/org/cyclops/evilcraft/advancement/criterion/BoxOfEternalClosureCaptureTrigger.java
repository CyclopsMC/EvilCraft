package org.cyclops.evilcraft.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.cyclops.cyclopscore.advancement.criterion.ICriterionInstanceTestable;
import org.cyclops.evilcraft.Reference;

/**
 * Triggers when a player captures an entity with a Box of Eternal Closure
 * @author rubensworks
 */
public class BoxOfEternalClosureCaptureTrigger extends SimpleCriterionTrigger<BoxOfEternalClosureCaptureTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "box_of_eternal_closure_capture");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        return new Instance(getId(), entityPredicate, EntityPredicate.fromJson(json.get("entity")));
    }

    public void test(ServerPlayer player, Entity entity) {
        this.trigger(player, (instance) -> instance.test(player, entity));
    }

    public static class Instance extends AbstractCriterionTriggerInstance implements ICriterionInstanceTestable<Entity> {

        private final EntityPredicate entityPredicate;

        public Instance(ResourceLocation criterionIn, ContextAwarePredicate player, EntityPredicate entityPredicate) {
            super(criterionIn, player);
            this.entityPredicate = entityPredicate;
        }

        public boolean test(ServerPlayer player, Entity entity) {
            return this.entityPredicate.matches(player, entity);
        }
    }

}
