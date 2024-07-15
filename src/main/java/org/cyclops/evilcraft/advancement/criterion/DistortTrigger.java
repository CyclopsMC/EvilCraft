package org.cyclops.evilcraft.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Optional;

/**
 * Triggers when a player uses the Mace of Distortion.
 * @author rubensworks
 */
public class DistortTrigger extends SimpleCriterionTrigger<DistortTrigger.Instance> {

    public static final Codec<DistortTrigger.Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(DistortTrigger.Instance::player),
                            Codec.INT.optionalFieldOf("min_entities").forGetter(DistortTrigger.Instance::minEntities),
                            EntityPredicate.CODEC.optionalFieldOf("entity").forGetter(DistortTrigger.Instance::entityPredicate)
                    )
                    .apply(p_311401_, DistortTrigger.Instance::new)
    );

    @Override
    public Codec<Instance> codec() {
        return CODEC;
    }

    public void test(ServerPlayer player, List<Entity> entities) {
        this.trigger(player, (instance) -> instance.test(player, entities));
    }

    public static record Instance(
            Optional<ContextAwarePredicate> player,
            Optional<Integer> minEntities,
            Optional<EntityPredicate> entityPredicate
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public boolean test(ServerPlayer player, List<Entity> entities) {
            int count = 0;
            for (Entity entity : entities) {
                if (this.entityPredicate.map(p -> p.matches(player, entity)).orElse(true)) {
                    count++;
                }
            }
            return count >= this.minEntities.orElse(0);
        }
    }

}
