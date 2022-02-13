package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemBloodPearlOfTeleportation;
import org.cyclops.evilcraft.item.ItemBloodPearlOfTeleportationConfig;

import javax.annotation.Nonnull;

/**
 * Entity for the {@link ItemBloodPearlOfTeleportation}.
 * @author rubensworks
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityBloodPearl extends ThrowableEntity implements IRendersAsItem {

    public EntityBloodPearl(EntityType<? extends EntityBloodPearl> type, World world) {
        super(type, world);
    }

    public EntityBloodPearl(World world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_BLOOD_PEARL, entity, world);
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHit(RayTraceResult position) {
        if (position.getType() == RayTraceResult.Type.ENTITY) {
            ((EntityRayTraceResult) position).getEntity()
                    .hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
        }

        if (!this.level.isClientSide()) {
            if (this.getOwner() != null && this.getOwner() instanceof ServerPlayerEntity) {
                ServerPlayerEntity entityplayermp = (ServerPlayerEntity)this.getOwner();

                if (entityplayermp.connection.connection.isConnected() && entityplayermp.level == this.level) {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, this.getX(), this.getY(), this.getZ(), 0.0F);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        if (this.getOwner().isPassenger()) {
                            this.getOwner().stopRiding();
                        }
    
                        this.getOwner().teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        this.getOwner().fallDistance = 0.0F;
                        this.getOwner().hurt(DamageSource.FALL, event.getAttackDamage());
                        ((LivingEntity) this.getOwner()).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN,
                        		ItemBloodPearlOfTeleportationConfig.slownessDuration * 20, 2));
                    }
                }
            }

            this.remove();
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
