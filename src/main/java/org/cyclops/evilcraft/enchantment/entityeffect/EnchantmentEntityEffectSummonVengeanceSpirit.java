package org.cyclops.evilcraft.enchantment.entityeffect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.cyclops.evilcraft.item.ItemVengeanceRing;

/**
 * @author rubensworks
 */
public record EnchantmentEntityEffectSummonVengeanceSpirit(
        int area,
        int chance
) implements EnchantmentEntityEffect {
    public static final MapCodec<EnchantmentEntityEffectSummonVengeanceSpirit> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            Codec.INT.fieldOf("area").forGetter(EnchantmentEntityEffectSummonVengeanceSpirit::area),
                            Codec.INT.fieldOf("chance").forGetter(EnchantmentEntityEffectSummonVengeanceSpirit::chance)
                    )
                    .apply(builder, EnchantmentEntityEffectSummonVengeanceSpirit::new)
    );

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec) {
        if (enchantmentLevel > 0) {
            int chance = Math.max(1, chance() / enchantmentLevel);
            if (chance > 0 && level.random.nextInt(chance) == 0) {
                if (entity instanceof Projectile projectile) {
                    entity = projectile.getEffectSource();
                }
                ItemVengeanceRing.toggleVengeanceArea(level, entity, area * enchantmentLevel, true, true, true);
            }
        }
    }

    @Override
    public MapCodec<EnchantmentEntityEffectSummonVengeanceSpirit> codec() {
        return CODEC;
    }
}
