package org.cyclops.evilcraft.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.advancement.criterion.ICriterionInstanceTestable;
import org.cyclops.evilcraft.Reference;

/**
 * Triggers when a player captures an entity with a Box of Eternal Closure
 * @author rubensworks
 */
public class BoxOfEternalClosureCaptureTrigger extends AbstractCriterionTrigger<BoxOfEternalClosureCaptureTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "box_of_eternal_closure_capture");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new Instance(getId(), entityPredicate, EntityPredicate.fromJson(json.get("entity")));
    }

    public void test(ServerPlayerEntity player, Entity entity) {
        this.trigger(player, (instance) -> instance.test(player, entity));
    }

    public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<Entity> {

        private final EntityPredicate entityPredicate;

        public Instance(ResourceLocation criterionIn, EntityPredicate.AndPredicate player, EntityPredicate entityPredicate) {
            super(criterionIn, player);
            this.entityPredicate = entityPredicate;
        }

        public boolean test(ServerPlayerEntity player, Entity entity) {
            return this.entityPredicate.matches(player, entity);
        }
    }

}
