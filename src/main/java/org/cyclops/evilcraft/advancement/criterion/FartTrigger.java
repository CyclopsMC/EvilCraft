package org.cyclops.evilcraft.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.advancement.criterion.ICriterionInstanceTestable;
import org.cyclops.evilcraft.Reference;

/**
 * Triggers when a player farts.
 * @author rubensworks
 */
public class FartTrigger extends AbstractCriterionTrigger<FartTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "fart");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new Instance(getId(), entityPredicate);
    }

    public void test(ServerPlayerEntity player) {
        this.trigger(player, (instance) -> instance.test(player, null));
    }

    public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<Void> {
        public Instance(ResourceLocation criterionIn, EntityPredicate.AndPredicate player) {
            super(criterionIn, player);
        }

        public boolean test(ServerPlayerEntity player, Void v) {
            return true;
        }
    }

}
