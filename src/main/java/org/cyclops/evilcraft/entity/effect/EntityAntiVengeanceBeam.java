package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Entity for the anti-vengeance beams.
 * @author rubensworks
 *
 */
public class EntityAntiVengeanceBeam extends ThrowableProjectile {

    private static final int MAX_AGE = 10 * 20;

    private int age = 0;
    private int soundTick = 0;

    public EntityAntiVengeanceBeam(EntityType<? extends EntityAntiVengeanceBeam> type, Level world) {
        super(type, world);
        setDeltaMovement(getDeltaMovement().multiply(0.25, 0.25, 0.25));
    }

    public EntityAntiVengeanceBeam(EntityType<? extends EntityAntiVengeanceBeam> type, Level world, LivingEntity entity) {
        super(type, entity, world);
        setDeltaMovement(getDeltaMovement().multiply(0.25, 0.25, 0.25));
    }

    public EntityAntiVengeanceBeam(Level world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_ANTI_VENGEANCE_BEAM, entity, world);
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Nullable
    protected EntityHitResult rayTraceEntities(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D),
                (entity) -> !entity.isSpectator() && entity.isAlive() && entity.isPickable() && (entity != this.getOwner()));
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        Vec3 motion = getDeltaMovement();
        Vec3 vec3 = new Vec3(this.getX(), this.getY(), this.getZ());
        Vec3 vec31 = new Vec3(this.getX(), this.getY(), this.getZ()).add(motion);
        EntityHitResult entityRayTraceResult = this.rayTraceEntities(vec3, vec31);
        vec3 = new Vec3(this.getX(), this.getY(), this.getZ());
        vec31 = new Vec3(this.getX(), this.getY(), this.getZ()).add(motion);

        soundTick++;
        if(soundTick > 3 && this.getId() % 10 == 0) {
            soundTick = 0;
        }

        if (!this.level().isClientSide()) {
            Entity entity = null;
            List<Entity> list = this.level().getEntities(this, this.getBoundingBox()
                    .move(motion).inflate(1.0D));
            double d0 = 0.0D;

            for (Entity entity1 : list) {
                if (entity1 instanceof EntityVengeanceSpirit) {
                    float f = 0.3F;
                    AABB axisalignedbb = entity1.getBoundingBox().inflate((double) f);
                    EntityHitResult movingobjectposition1 = ProjectileUtil.getEntityHitResult(level(), this, vec3, vec31, axisalignedbb, (e) -> true);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.getLocation());

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                entityRayTraceResult = new EntityHitResult(entity);
            }
        } else {
            for(int i = 0; i < level().random.nextInt(5) + 5; i++) {
                showNewBlurParticle();
            }
            if(soundTick == 1) {
                // Play beam sound
                level().playLocalSound(getX(), getY(), getZ(),
                        EvilCraftSoundEvents.effect_vengeancebeam_base, SoundSource.NEUTRAL,
                        0.5F + level().random.nextFloat() * 0.2F, 1.0F, false);
            }
        }

        if (entityRayTraceResult != null) {
            this.onHit(entityRayTraceResult);
        }

        if(age++ > MAX_AGE) {
            this.remove(RemovalReason.DISCARDED);
        }

        super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    protected void showNewBlurParticle() {
        float scale = 0.6F - random.nextFloat() * 0.3F;
        float red = random.nextFloat() * 0.03F + 0.01F;
        float green = random.nextFloat() * 0.03F;
        float blue = random.nextFloat() * 0.05F + 0.05F;
        float ageMultiplier = (float) (random.nextDouble() * 6.5D + 4D);
        Vec3 motion = getDeltaMovement();

        Minecraft.getInstance().levelRenderer.addParticle(
                new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                getX(), getY(), getZ(),
                deriveMotion(motion.x), deriveMotion(motion.y), deriveMotion(motion.z));
    }

    private double deriveMotion(double motion) {
        return motion * 0.5D + (0.02D - random.nextDouble() * 0.04D);
    }

    protected void applyHitEffect(Entity entity) {
        if (entity instanceof EntityVengeanceSpirit spirit) {
            Vec3 motion = getDeltaMovement();
            spirit.onHit(getX(), getY(), getZ(), motion.x, motion.y, motion.z);
            if (getOwner() instanceof ServerPlayer owner) {
                spirit.addEntanglingPlayer(owner);
            }
        }
    }

    @Override
    protected void onHit(HitResult position) {
        if (!this.level().isClientSide()) {
            if (position.getType() == HitResult.Type.ENTITY && this.getOwner() != null && this.getOwner() instanceof ServerPlayer) {
                applyHitEffect(((EntityHitResult) position).getEntity());
            }
        }
        this.remove(RemovalReason.DISCARDED);
    }
}
