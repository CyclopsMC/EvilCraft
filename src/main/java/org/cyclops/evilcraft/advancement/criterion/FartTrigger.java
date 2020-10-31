package org.cyclops.evilcraft.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.advancement.criterion.BaseCriterionTrigger;
import org.cyclops.cyclopscore.advancement.criterion.ICriterionInstanceTestable;
import org.cyclops.evilcraft.Reference;

/**
 * Triggers when a player farts.
 * @author rubensworks
 */
public class FartTrigger extends BaseCriterionTrigger<Void, FartTrigger.Instance> {
    public FartTrigger() {
        super(new ResourceLocation(Reference.MOD_ID, "fart"));
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new Instance(getId());
    }

    public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<Void> {
        public Instance(ResourceLocation criterionIn) {
            super(criterionIn);
        }

        public boolean test(ServerPlayerEntity player, Void v) {
            return true;
        }
    }

}
