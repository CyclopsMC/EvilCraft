package org.cyclops.evilcraft.enchantment.entityeffect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.cyclops.evilcraft.Reference;

/**
 * @author rubensworks
 */
@EventBusSubscriber(modid = Reference.MOD_ID)
public record EnchantmentEntityEffectHealFromDamage(
        LevelBasedValue damageMultiplier
) implements EnchantmentEntityEffect {
    public static final MapCodec<EnchantmentEntityEffectHealFromDamage> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            LevelBasedValue.CODEC.fieldOf("damage_multiplier").forGetter(EnchantmentEntityEffectHealFromDamage::damageMultiplier)
                    )
                    .apply(builder, EnchantmentEntityEffectHealFromDamage::new)
    );

    private static LivingDamageEvent.Post lastDamageEvent;
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void stealLife(LivingDamageEvent.Post event) {
        if (!event.getEntity().level().isClientSide()) {
            lastDamageEvent = event;
        }
    }

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec) {
        if (lastDamageEvent != null && lastDamageEvent.getSource().getEntity() == entity) {
            float damage = lastDamageEvent.getNewDamage();
            enchantedItemInUse.owner().heal(this.damageMultiplier.calculate(enchantmentLevel) * damage);
            lastDamageEvent = null;
        }
    }

    @Override
    public MapCodec<EnchantmentEntityEffectHealFromDamage> codec() {
        return CODEC;
    }
}
