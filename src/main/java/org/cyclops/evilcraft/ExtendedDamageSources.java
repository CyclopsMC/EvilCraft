package org.cyclops.evilcraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

/**
 * @author rubensworks
 */
public class ExtendedDamageSources {

    public static final ResourceKey<DamageType> DAMAGE_TYPE_BROOM = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "broom"));
    public static final ResourceKey<DamageType> DAMAGE_TYPE_DIE_WITHOUT_ANY_REASON = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "die_without_any_reason"));
    public static final ResourceKey<DamageType> DAMAGE_TYPE_DISTORTED = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "distorted"));
    public static final ResourceKey<DamageType> DAMAGE_TYPE_PALING = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "paling"));
    public static final ResourceKey<DamageType> DAMAGE_TYPE_SPIKED = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "spiked"));
    public static final ResourceKey<DamageType> DAMAGE_TYPE_VENGEANCE_BEAM = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "vengeance_beam"));

    public static DamageSource dieWithoutAnyReason(Level level) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DAMAGE_TYPE_DIE_WITHOUT_ANY_REASON));
    }

    public static DamageSource distorted(Level level) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DAMAGE_TYPE_DISTORTED));
    }

    public static DamageSource spikedDamage(ServerLevel level, BlockPos pos) {
        FakePlayer entity = FakePlayerFactory.getMinecraft(level);
        entity.setPos(Vec3.atCenterOf(pos));
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DAMAGE_TYPE_SPIKED), entity);
    }

    public static DamageSource paling(Level level) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DAMAGE_TYPE_PALING));
    }

    public static DamageSource broomDamage(LivingEntity attacker) {
        return new DamageSource(attacker.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DAMAGE_TYPE_BROOM), attacker);
    }

    public static DamageSource vengeanceBeam(LivingEntity attacker) {
        return new DamageSource(attacker.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DAMAGE_TYPE_VENGEANCE_BEAM), attacker);
    }
}
