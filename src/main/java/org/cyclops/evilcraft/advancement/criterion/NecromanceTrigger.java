package org.cyclops.evilcraft.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

/**
 * Triggers when a player uses the Necromancer Staff.
 * @author rubensworks
 */
public class NecromanceTrigger extends SimpleCriterionTrigger<NecromanceTrigger.Instance> {

    public static final Codec<NecromanceTrigger.Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(NecromanceTrigger.Instance::player),
                            EntityPredicate.CODEC.fieldOf("entity").forGetter(NecromanceTrigger.Instance::entityPredicate)
                    )
                    .apply(p_311401_, NecromanceTrigger.Instance::new)
    );

    @Override
    public Codec<Instance> codec() {
        return CODEC;
    }

    public void test(ServerPlayer player, Entity entity) {
        this.trigger(player, (instance) -> instance.test(player, entity));
    }

    public static record Instance(
            Optional<ContextAwarePredicate> player,
            EntityPredicate entityPredicate
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public boolean test(ServerPlayer player, Entity entity) {
            return this.entityPredicate.matches(player, entity);
        }
    }

}
