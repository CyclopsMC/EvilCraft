package org.cyclops.evilcraft.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

/**
 * Triggers when a player captures an entity with a Box of Eternal Closure
 * @author rubensworks
 */
public class BoxOfEternalClosureCaptureTrigger extends SimpleCriterionTrigger<BoxOfEternalClosureCaptureTrigger.Instance> {

    public static final Codec<BoxOfEternalClosureCaptureTrigger.Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(BoxOfEternalClosureCaptureTrigger.Instance::player),
                            ExtraCodecs.strictOptionalField(EntityPredicate.CODEC, "entity").forGetter(BoxOfEternalClosureCaptureTrigger.Instance::entity)
                    )
                    .apply(p_311401_, BoxOfEternalClosureCaptureTrigger.Instance::new)
    );

    public void test(ServerPlayer player, Entity entity) {
        this.trigger(player, (instance) -> instance.test(player, entity));
    }

    @Override
    public Codec<Instance> codec() {
        return CODEC;
    }

    public static record Instance(
            Optional<ContextAwarePredicate> player,
            Optional<EntityPredicate> entity
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public boolean test(ServerPlayer player, Entity entity) {
            return this.entity.map(p -> p.matches(player, entity)).orElse(true);
        }
    }

}
