package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.network.NetworkHooks;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemBloodPearlOfTeleportation;
import org.cyclops.evilcraft.item.ItemBloodPearlOfTeleportationConfig;

import javax.annotation.Nonnull;

/**
 * Entity for the {@link ItemBloodPearlOfTeleportation}.
 * @author rubensworks
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class EntityBloodPearl extends ThrowableProjectile implements ItemSupplier {

    public EntityBloodPearl(EntityType<? extends EntityBloodPearl> type, Level world) {
        super(type, world);
    }

    public EntityBloodPearl(Level world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_BLOOD_PEARL, entity, world);
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHit(HitResult position) {
        if (position.getType() == HitResult.Type.ENTITY) {
            ((EntityHitResult) position).getEntity()
                    .hurt(level().damageSources().thrown(this, this.getOwner()), 0.0F);
        }

        if (!this.level().isClientSide()) {
            if (this.getOwner() != null && this.getOwner() instanceof ServerPlayer) {
                ServerPlayer entityplayermp = (ServerPlayer)this.getOwner();

                if (entityplayermp.connection.connection.isConnected() && entityplayermp.level() == this.level()) {
                    EntityTeleportEvent event = new EntityTeleportEvent(entityplayermp, this.getX(), this.getY(), this.getZ());
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        if (this.getOwner().isPassenger()) {
                            this.getOwner().stopRiding();
                        }

                        this.getOwner().teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        this.getOwner().fallDistance = 0.0F;
                        ((LivingEntity) this.getOwner()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
                                ItemBloodPearlOfTeleportationConfig.slownessDuration * 20, 2));
                    }
                }
            }

            this.remove(RemovalReason.DISCARDED);
        } else {
            for (int i = 0; i < 32; ++i) {
                Minecraft.getInstance().levelRenderer.addParticle(
                        ParticleTypes.PORTAL, false,
                        this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(),
                        this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
            }
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegistryEntries.ITEM_BLOOD_PEARL_OF_TELEPORTATION);
    }
}
