package org.cyclops.evilcraft.entity.monster;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.List;

/**
 * A libelle that poisons you.
 * @author rubensworks
 *
 */
public class EntityPoisonousLibelle extends FlyingMob implements Enemy {

    static {
        MinecraftForge.EVENT_BUS.register(EntityPoisonousLibelle.class);
    }

    private static final int POISON_DURATION = 2;

    /**
     * Target X.
     */
    public double targetX;
    /**
     * Target Y.
     */
    public double targetY;
    /**
     * Target Z.
     */
    public double targetZ;

    /**
     * Previous animation time.
     */
    public float prevAnimTime;
    /**
     * Current animation time.
     */
    public float animTime;
    /**
     * If a new search for a target is toggled.
     */
    public boolean forceNewTarget;
    private Entity target;

    private static int WINGLENGTH = 4;
    private int wingProgress = 0;
    private boolean wingGoUp = true;

    private static final int MAXHEIGHT = 80;
    private float randomYawVelocity;

    public EntityPoisonousLibelle(EntityType<? extends EntityPoisonousLibelle> typeIn, Level worldIn) {
        super(typeIn, worldIn);
        this.xpReward = 10;
    }

    public EntityPoisonousLibelle(Level world) {
        this(RegistryEntries.ENTITY_POISONOUS_LIBELLE, world);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void checkLibelleSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if(event.getEntity() instanceof EntityPoisonousLibelle) {
            if(event.getEntity().getY() < EntityPoisonousLibelleConfig.minY) {
                event.setSpawnCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @Override
    public SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BAT_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void aiStep() {
        super.aiStep();
        float f;
        float f1;

        if (this.level().isClientSide()) {
            f = Mth.cos(this.animTime * (float)Math.PI * 2.0F);
            f1 = Mth.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

            if (f1 <= -0.3F && f >= -0.3F && this.random.nextInt(45) == 0) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BAT_AMBIENT,
                        SoundSource.AMBIENT, 0.1F, 0.8F + this.random.nextFloat() * 0.3F);
            }
        }

        this.prevAnimTime = this.animTime;

        Vec3 m = getDeltaMovement();
        f = 0.2F / (Mth.sqrt((float) (m.x * m.x + m.z * m.z)) * 10.0F + 1.0F);
        f *= (float)Math.pow(2.0D, m.y);

        this.animTime += f;

        double distanceY;
        double distanceZ;
        double distance;
        double distanceX;
        float limitDistanceY;
        double limitDifferenceYaw;

        if (this.level().isClientSide()) {
            // Correct rotation of the entity when rotating
            if (this.lerpSteps > 0) {
                distanceX = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
                distanceY = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
                distanceZ = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
                distance = Mth.wrapDegrees(this.lerpYRot - (double) this.getYRot());
                this.setYRot((float)((double)this.getYRot() + distance / (double)this.lerpSteps));
                // MCP: newPosX should probably be interpPitch
                this.setXRot((float)((double)this.getXRot() + (this.targetX - (double)this.getXRot()) / (double)this.lerpSteps));
                --this.lerpSteps;
                this.setPos(distanceX, distanceY, distanceZ);
                this.setRot(this.getYRot(), this.getXRot());
            }
        } else {
            // Calc distance to target
            distanceX = this.targetX - this.getX();
            distanceY = this.targetY - this.getY();
            distanceZ = this.targetZ - this.getZ();
            distance = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;

            if (this.target != null) {
                this.targetX = this.target.getX();
                this.targetZ = this.target.getZ();
                double distancedX = this.targetX - this.getX();
                double distancedZ = this.targetZ - this.getZ();
                double distancedHeightPlane = Math.sqrt(distancedX * distancedX + distancedZ * distancedZ);
                double Yplus = 0.4D + distancedHeightPlane / 80.0D - 1.0D;

                // Limit height difference
                if (Yplus > 10.0D) {
                    Yplus = 10.0D;
                }

                this.targetY = Math.min(this.target.getBoundingBox().minY + Yplus, MAXHEIGHT);
            } else {
                this.targetX += (this.random.nextDouble() * 2.0D - 1.0D) * 2.0D;
                this.targetZ += (this.random.nextDouble() * 2.0D - 1.0D) * 2.0D;
            }

            // Reset target
            if (this.forceNewTarget
                    || distance < 3.0D
                    || distance > 250.0D
                    || this.horizontalCollision
                    || this.verticalCollision
                    || this.targetY > MAXHEIGHT) {
                this.setNewTarget();
            }

            distanceY /= (double) Mth.sqrt((float) (distanceX * distanceX + distanceZ * distanceZ));
            limitDistanceY = 0.6F;

            if (distanceY < (double)(-limitDistanceY)) {
                distanceY = (double)(-limitDistanceY);
            }

            if (distanceY > (double)limitDistanceY) {
                distanceY = (double)limitDistanceY;
            }

            setDeltaMovement(getDeltaMovement().add(0, distanceY * 0.1D, 0));
            this.setYRot(Mth.wrapDegrees(this.getYRot()));
            double newYaw = 180.0D - Math.atan2(distanceX, distanceZ) * 180.0D / Math.PI;
            double differenceYaw = Mth.wrapDegrees(newYaw - (double)this.getYRot());

            limitDifferenceYaw = 50.0D;

            if (differenceYaw > limitDifferenceYaw) {
                differenceYaw = limitDifferenceYaw;
            }

            if (differenceYaw < -limitDifferenceYaw) {
                differenceYaw = -limitDifferenceYaw;
            }

            Vec3 distanceVector = new Vec3(this.targetX - this.getX(), this.targetY - this.getY(), this.targetZ - this.getZ()).normalize();
            Vec3 rotationVector = new Vec3((double)Mth.sin(this.getYRot() * (float)Math.PI / 180.0F), m.y, (double)(-Mth.cos(this.getYRot() * (float)Math.PI / 180.0F))).normalize();
            float dynamicMotionMultiplier = (float)(rotationVector.dot(distanceVector) + 0.5D) / 1.5F;

            if (dynamicMotionMultiplier < 0.0F) {
                dynamicMotionMultiplier = 0.0F;
            }

            this.randomYawVelocity *= 0.8F;
            float motionDistanceHeightPlaneFloat = Mth.sqrt((float) (m.x * m.x + m.z * m.z)) * 1.0F + 1.0F;
            double motionDistanceHeightPlane = Math.sqrt(m.x * m.x + m.z * m.z) * 1.0D + 1.0D;

            if (motionDistanceHeightPlane > 40.0D) {
                motionDistanceHeightPlane = 40.0D;
            }

            this.randomYawVelocity = (float)((double)this.randomYawVelocity + differenceYaw * (0.7D / motionDistanceHeightPlane / (double)motionDistanceHeightPlaneFloat));
            this.setYRot(this.getYRot() + this.randomYawVelocity * 0.1F);
            float scaledMotionDistanceHeightPlane = (float)(2.0D / (motionDistanceHeightPlane + 1.0D));
            float staticMotionMultiplier = 0.03F;
            this.moveRelative(staticMotionMultiplier * (dynamicMotionMultiplier * scaledMotionDistanceHeightPlane + (1.0F - scaledMotionDistanceHeightPlane)), new Vec3(0.0D, 0.0D, -1.0D));

            this.move(MoverType.SELF, getDeltaMovement());

            Vec3 vec3d3 = this.getDeltaMovement().normalize();
            double d6 = 0.8D + 0.15D * (vec3d3.dot(rotationVector) + 1.0D) / 2.0D;
            this.setDeltaMovement(this.getDeltaMovement().multiply(d6, 0.91F, d6));
        }

        this.yBodyRot = this.getYRot();

        if (!this.level().isClientSide() && this.hurtTime == 0 && this.isAlive()) {
            this.attackEntitiesInList(this.level().getEntities(this, this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D)));
        }

        // Update wing progress
        if(wingGoUp) {
            wingProgress++;
            if(wingProgress > WINGLENGTH)
                wingGoUp = false;
        } else {
            wingProgress--;
            if(wingProgress < -WINGLENGTH)
                wingGoUp = true;
        }

        if (!this.level().isClientSide() && this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private void attackEntitiesInList(List<Entity> entities) {
        int chance = EntityPoisonousLibelleConfig.poisonChance;
        for (Entity entity : entities) {
            if(chance > 0 && level().random.nextInt(chance) == 0) {
                if (entity instanceof LivingEntity) {
                    boolean shouldAttack = true;
                    if (entity instanceof Player) {
                        if (((Player) entity).isCreative()) {
                            shouldAttack = false;
                            setNewTarget();
                        }
                    }
                    if (shouldAttack) {
                        if (EntityPoisonousLibelleConfig.hasAttackDamage)
                            entity.hurt(level().damageSources().mobAttack(this), 0.5F);
                        ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION * 20, 1));
                    }
                }
            }
        }
    }

    private void setNewTarget() {
        this.forceNewTarget = false;

        boolean targetSet = false;
        if (this.random.nextInt(2) == 0 && !this.level().players().isEmpty() && !this.level().isDay()) {
            this.target = (Entity)this.level().players().get(this.random.nextInt(this.level().players().size()));
            targetSet = true;
            if(target instanceof Player) {
                if(((Player)target).isCreative()) {
                    targetSet = false;
                }
            }
        }

        if(!targetSet) {
            boolean flag;

            do {
                this.targetX = this.getX();
                this.targetY = (double)(MAXHEIGHT - this.random.nextFloat() * 30.0F);
                this.targetZ = this.getZ();
                this.targetX += (double)(this.random.nextFloat() * 120.0F - 60.0F);
                this.targetZ += (double)(this.random.nextFloat() * 120.0F - 60.0F);
                double d0 = this.getX() - this.targetX;
                double d1 = this.getY() - this.targetY;
                double d2 = this.getZ() - this.targetZ;
                flag = d0 * d0 + d1 * d1 + d2 * d2 > 20.0D;
            } while (!flag);

            this.target = null;
        }
    }

    /**
     * Get the wing progress scaled to the given parameter.
     * @param scale The scale.
     * @return The scaled progress.
     */
    public float getWingProgressScaled(float scale) {
        return (float)wingProgress / (float)WINGLENGTH * scale;
    }

}
