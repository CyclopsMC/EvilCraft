package org.cyclops.evilcraft.advancement.criterion;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import org.cyclops.cyclopscore.advancement.criterion.ICriterionInstanceTestable;
import org.cyclops.evilcraft.Reference;

import java.util.List;

/**
 * Triggers when a player uses the Mace of Distortion.
 * @author rubensworks
 */
public class DistortTrigger extends SimpleCriterionTrigger<DistortTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "distort");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        JsonElement jsonElement = json.get("min_entities");
        int minEntities = 0;
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            minEntities = GsonHelper.getAsInt(json, "min_entities");
        }
        return new Instance(getId(), entityPredicate, minEntities, EntityPredicate.fromJson(json.get("entity")));
    }

    public void test(ServerPlayer player, List<Entity> entities) {
        this.trigger(player, (instance) -> instance.test(player, entities));
    }

    public static class Instance extends AbstractCriterionTriggerInstance implements ICriterionInstanceTestable<List<Entity>> {

        private final int minEntities;
        private final EntityPredicate entityPredicate;

        public Instance(ResourceLocation criterionIn, ContextAwarePredicate player, int minEntities, EntityPredicate entityPredicate) {
            super(criterionIn, player);
            this.minEntities = minEntities;
            this.entityPredicate = entityPredicate;
        }

        public boolean test(ServerPlayer player, List<Entity> entities) {
            int count = 0;
            for (Entity entity : entities) {
                if (this.entityPredicate.matches(player, entity)) {
                    count++;
                }
            }
            return count >= this.minEntities;
        }
    }

}
