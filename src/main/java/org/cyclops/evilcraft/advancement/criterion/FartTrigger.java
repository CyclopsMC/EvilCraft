package org.cyclops.evilcraft.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Triggers when a player farts.
 * @author rubensworks
 */
public class FartTrigger extends SimpleCriterionTrigger<FartTrigger.Instance> {

    public static final Codec<FartTrigger.Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(FartTrigger.Instance::player)
                    )
                    .apply(p_311401_, FartTrigger.Instance::new)
    );

    @Override
    public Codec<Instance> codec() {
        return CODEC;
    }

    public void test(ServerPlayer player) {
        this.trigger(player, (instance) -> instance.test(player, null));
    }

    public static record Instance(
            Optional<ContextAwarePredicate> player
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public boolean test(ServerPlayer player, Void v) {
            return true;
        }
    }

}
