package org.cyclops.evilcraft.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.advancement.criterion.BaseCriterionTrigger;
import org.cyclops.cyclopscore.advancement.criterion.ICriterionInstanceTestable;
import org.cyclops.evilcraft.Reference;

import java.util.List;

/**
 * Triggers when a player uses the Mace of Distortion.
 * @author rubensworks
 */
public class DistortTrigger extends BaseCriterionTrigger<List<Entity>, DistortTrigger.Instance> {
    public DistortTrigger() {
        super(new ResourceLocation(Reference.MOD_ID, "distort"));
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        JsonElement jsonElement = json.get("min_entities");
        int minEntities = 0;
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            minEntities = JsonUtils.getInt(json, "min_entities");
        }
        return new Instance(getId(), minEntities, EntityPredicate.deserialize(json.get("entity")));
    }

    public static class Instance extends AbstractCriterionInstance implements ICriterionInstanceTestable<List<Entity>> {

        private final int minEntities;
        private final EntityPredicate entityPredicate;

        public Instance(ResourceLocation criterionIn, int minEntities, EntityPredicate entityPredicate) {
            super(criterionIn);
            this.minEntities = minEntities;
            this.entityPredicate = entityPredicate;
        }

        public boolean test(EntityPlayerMP player, List<Entity> entities) {
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
