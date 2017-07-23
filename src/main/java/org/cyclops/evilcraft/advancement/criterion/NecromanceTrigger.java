package org.cyclops.evilcraft.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.advancement.criterion.BaseCriterionTrigger;
import org.cyclops.cyclopscore.advancement.criterion.ICriterionInstanceTestable;
import org.cyclops.evilcraft.Reference;

/**
 * Triggers when a player uses the Necromancer Staff.
 * @author rubensworks
 */
public class NecromanceTrigger extends BaseCriterionTrigger<Entity, NecromanceTrigger.Instance> {
    public NecromanceTrigger() {
        super(new ResourceLocation(Reference.MOD_ID, "necromance"));
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new Instance(getId(), EntityPredicate.deserialize(json.get("entity")));
    }

    public static class Instance extends AbstractCriterionInstance implements ICriterionInstanceTestable<Entity> {

        private final EntityPredicate entityPredicate;

        public Instance(ResourceLocation criterionIn, EntityPredicate entityPredicate) {
            super(criterionIn);
            this.entityPredicate = entityPredicate;
        }

        public boolean test(EntityPlayerMP player, Entity entity) {
            return this.entityPredicate.test(player, entity);
        }
    }

}
