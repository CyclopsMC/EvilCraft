package org.cyclops.evilcraft.entity.monster;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
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
public class EntityPoisonousLibelle extends FlyingEntity implements IMob {

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

    public EntityPoisonousLibelle(EntityType<? extends EntityPoisonousLibelle> typeIn, World worldIn) {
        super(typeIn, worldIn);
        this.xpReward = 10;
    }

    public EntityPoisonousLibelle(World world) {
        this(RegistryEntries.ENTITY_POISONOUS_LIBELLE, world);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void checkLibelleSpawn(LivingSpawnEvent.CheckSpawn event) {
        if(event.getEntityLiving() instanceof EntityPoisonousLibelle) {
            if(((EntityPoisonousLibelle) event.getEntityLiving()).getY() < EntityPoisonousLibelleConfig.minY) {
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
    public CreatureAttribute getMobType() {
        return CreatureAttribute.ARTHROPOD;
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

        if (this.level.isClientSide()) {
            f = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
            f1 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

            if (f1 <= -0.3F && f >= -0.3F && this.random.nextInt(45) == 0) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BAT_AMBIENT,
                        SoundCategory.AMBIENT, 0.1F, 0.8F + this.random.nextFloat() * 0.3F);
            }
        }

        this.prevAnimTime = this.animTime;

        Vector3d m = getDeltaMovement();
        f = 0.2F / (MathHelper.sqrt(m.x * m.x + m.z * m.z) * 10.0F + 1.0F);
        f *= (float)Math.pow(2.0D, m.y);

        this.animTime += f;
        
        double distanceY;
        double distanceZ;
        double distance;
        double distanceX;
        float limitDistanceY;
        double limitDifferenceYaw;

        if (this.level.isClientSide()) {
            // Correct rotation of the entity when rotating
            if (this.lerpSteps > 0) {
                distanceX = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
                distanceY = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
                distanceZ = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
                distance = MathHelper.wrapDegrees(this.lerpYRot - (double) this.yRot);
                this.yRot = (float)((double)this.yRot + distance / (double)this.lerpSteps);
                // MCP: newPosX should probably be interpPitch
                this.xRot = (float)((double)this.xRot + (this.targetX - (double)this.xRot) / (double)this.lerpSteps);
                --this.lerpSteps;
                this.setPos(distanceX, distanceY, distanceZ);
                this.setRot(this.yRot, this.xRot);
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

            distanceY /= (double) MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ);
            limitDistanceY = 0.6F;

            if (distanceY < (double)(-limitDistanceY)) {
                distanceY = (double)(-limitDistanceY);
            }

            if (distanceY > (double)limitDistanceY) {
                distanceY = (double)limitDistanceY;
            }

            setDeltaMovement(getDeltaMovement().add(0, distanceY * 0.1D, 0));
            this.yRot = MathHelper.wrapDegrees(this.yRot);
            double newYaw = 180.0D - Math.atan2(distanceX, distanceZ) * 180.0D / Math.PI;
            double differenceYaw = MathHelper.wrapDegrees(newYaw - (double)this.yRot);

            limitDifferenceYaw = 50.0D;
            
            if (differenceYaw > limitDifferenceYaw) {
                differenceYaw = limitDifferenceYaw;
            }

            if (differenceYaw < -limitDifferenceYaw) {
                differenceYaw = -limitDifferenceYaw;
            }

            Vector3d distanceVector = new Vector3d(this.targetX - this.getX(), this.targetY - this.getY(), this.targetZ - this.getZ()).normalize();
            Vector3d rotationVector = new Vector3d((double)MathHelper.sin(this.yRot * (float)Math.PI / 180.0F), m.y, (double)(-MathHelper.cos(this.yRot * (float)Math.PI / 180.0F))).normalize();
            float dynamicMotionMultiplier = (float)(rotationVector.dot(distanceVector) + 0.5D) / 1.5F;

            if (dynamicMotionMultiplier < 0.0F) {
                dynamicMotionMultiplier = 0.0F;
            }

            this.randomYawVelocity *= 0.8F;
            float motionDistanceHeightPlaneFloat = MathHelper.sqrt(m.x * m.x + m.z * m.z) * 1.0F + 1.0F;
            double motionDistanceHeightPlane = Math.sqrt(m.x * m.x + m.z * m.z) * 1.0D + 1.0D;

            if (motionDistanceHeightPlane > 40.0D) {
                motionDistanceHeightPlane = 40.0D;
            }

            this.randomYawVelocity = (float)((double)this.randomYawVelocity + differenceYaw * (0.7D / motionDistanceHeightPlane / (double)motionDistanceHeightPlaneFloat));
            this.yRot += this.randomYawVelocity * 0.1F;
            float scaledMotionDistanceHeightPlane = (float)(2.0D / (motionDistanceHeightPlane + 1.0D));
            float staticMotionMultiplier = 0.03F;
            this.moveRelative(staticMotionMultiplier * (dynamicMotionMultiplier * scaledMotionDistanceHeightPlane + (1.0F - scaledMotionDistanceHeightPlane)), new Vector3d(0.0D, 0.0D, -1.0D));

            this.move(MoverType.SELF, getDeltaMovement());

            Vector3d vec3d3 = this.getDeltaMovement().normalize();
            double d6 = 0.8D + 0.15D * (vec3d3.dot(rotationVector) + 1.0D) / 2.0D;
            this.setDeltaMovement(this.getDeltaMovement().multiply(d6, 0.91F, d6));
        }

        this.yBodyRot = this.yRot;
        
        if (!this.level.isClientSide() && this.hurtTime == 0 && this.isAlive()) {
            this.attackEntitiesInList(this.level.getEntities(this, this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D)));
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
        
        if (!this.level.isClientSide() && this.level.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
    }

    private void attackEntitiesInList(List<Entity> entities) {
        int chance = EntityPoisonousLibelleConfig.poisonChance;
        for (Entity entity : entities) {
            if(chance > 0 && level.random.nextInt(chance) == 0) {
                if (entity instanceof LivingEntity) {
                    boolean shouldAttack = true;
                    if (entity instanceof PlayerEntity) {
                        if (((PlayerEntity) entity).isCreative()) {
                            shouldAttack = false;
                            setNewTarget();
                        }
                    }
                    if (shouldAttack) {
                        if (EntityPoisonousLibelleConfig.hasAttackDamage)
                            entity.hurt(DamageSource.mobAttack(this), 0.5F);
                        ((LivingEntity) entity).addEffect(new EffectInstance(Effects.POISON, POISON_DURATION * 20, 1));
                    }
                }
            }
        }
    }

    private void setNewTarget() {
        this.forceNewTarget = false;

        boolean targetSet = false;
        if (this.random.nextInt(2) == 0 && !this.level.players().isEmpty() && !this.level.isDay()) {
            this.target = (Entity)this.level.players().get(this.random.nextInt(this.level.players().size()));
            targetSet = true;
            if(target instanceof PlayerEntity) {
                if(((PlayerEntity)target).isCreative()) {
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