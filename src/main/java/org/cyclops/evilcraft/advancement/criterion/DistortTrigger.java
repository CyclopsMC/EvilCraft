package org.cyclops.evilcraft.advancement.criterion;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.advancement.criterion.ICriterionInstanceTestable;
import org.cyclops.evilcraft.Reference;

import java.util.List;

/**
 * Triggers when a player uses the Mace of Distortion.
 * @author rubensworks
 */
public class DistortTrigger extends AbstractCriterionTrigger<DistortTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "distort");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        JsonElement jsonElement = json.get("min_entities");
        int minEntities = 0;
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            minEntities = JSONUtils.getInt(json, "min_entities");
        }
        return new Instance(getId(), entityPredicate, minEntities, EntityPredicate.deserialize(json.get("entity")));
    }

    public void test(ServerPlayerEntity player, List<Entity> entities) {
        this.triggerListeners(player, (instance) -> instance.test(player, entities));
    }

    public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<List<Entity>> {

        private final int minEntities;
        private final EntityPredicate entityPredicate;

        public Instance(ResourceLocation criterionIn, EntityPredicate.AndPredicate player, int minEntities, EntityPredicate entityPredicate) {
            super(criterionIn, player);
            this.minEntities = minEntities;
            this.entityPredicate = entityPredicate;
        }

        public boolean test(ServerPlayerEntity player, List<Entity> entities) {
            int count = 0;
            for (Entity entity : entities) {
                if (this.entityPredicate.test(player, entity)) {
                    count++;
                }
            }
            return count >= this.minEntities;
        }
    }

}
