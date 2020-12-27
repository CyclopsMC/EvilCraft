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
        this.experienceValue = 10;
    }

    public EntityPoisonousLibelle(World world) {
        this(RegistryEntries.ENTITY_POISONOUS_LIBELLE, world);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void checkLibelleSpawn(LivingSpawnEvent.CheckSpawn event) {
        if(event.getEntityLiving() instanceof EntityPoisonousLibelle) {
            if(((EntityPoisonousLibelle) event.getEntityLiving()).getPosY() < EntityPoisonousLibelleConfig.minY) {
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
        return SoundEvents.ENTITY_BAT_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BAT_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void livingTick() {
        super.livingTick();
        float f;
        float f1;

        if (this.world.isRemote()) {
            f = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
            f1 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

            if (f1 <= -0.3F && f >= -0.3F && this.rand.nextInt(45) == 0) {
                this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_BAT_AMBIENT,
                        SoundCategory.AMBIENT, 0.1F, 0.8F + this.rand.nextFloat() * 0.3F);
            }
        }

        this.prevAnimTime = this.animTime;

        Vector3d m = getMotion();
        f = 0.2F / (MathHelper.sqrt(m.x * m.x + m.z * m.z) * 10.0F + 1.0F);
        f *= (float)Math.pow(2.0D, m.y);

        this.animTime += f;
        
        double distanceY;
        double distanceZ;
        double distance;
        double distanceX;
        float limitDistanceY;
        double limitDifferenceYaw;

        if (this.world.isRemote()) {
            // Correct rotation of the entity when rotating
            if (this.newPosRotationIncrements > 0) {
                distanceX = this.getPosX() + (this.interpTargetX - this.getPosX()) / (double)this.newPosRotationIncrements;
                distanceY = this.getPosY() + (this.interpTargetY - this.getPosY()) / (double)this.newPosRotationIncrements;
                distanceZ = this.getPosZ() + (this.interpTargetZ - this.getPosZ()) / (double)this.newPosRotationIncrements;
                distance = MathHelper.wrapDegrees(this.interpTargetYaw - (double) this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + distance / (double)this.newPosRotationIncrements);
                // MCP: newPosX should probably be interpPitch
                this.rotationPitch = (float)((double)this.rotationPitch + (this.targetX - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                --this.newPosRotationIncrements;
                this.setPosition(distanceX, distanceY, distanceZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        } else {
            // Calc distance to target
            distanceX = this.targetX - this.getPosX();
            distanceY = this.targetY - this.getPosY();
            distanceZ = this.targetZ - this.getPosZ();
            distance = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;

            if (this.target != null) {
                this.targetX = this.target.getPosX();
                this.targetZ = this.target.getPosZ();
                double distancedX = this.targetX - this.getPosX();
                double distancedZ = this.targetZ - this.getPosZ();
                double distancedHeightPlane = Math.sqrt(distancedX * distancedX + distancedZ * distancedZ);
                double Yplus = 0.4D + distancedHeightPlane / 80.0D - 1.0D;

                // Limit height difference
                if (Yplus > 10.0D) {
                    Yplus = 10.0D;
                }

                this.targetY = Math.min(this.target.getBoundingBox().minY + Yplus, MAXHEIGHT);
            } else {
                this.targetX += (this.rand.nextDouble() * 2.0D - 1.0D) * 2.0D;
                this.targetZ += (this.rand.nextDouble() * 2.0D - 1.0D) * 2.0D;
            }

            // Reset target
            if (this.forceNewTarget
                    || distance < 3.0D
                    || distance > 250.0D
                    || this.collidedHorizontally
                    || this.collidedVertically
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

            setMotion(getMotion().add(0, distanceY * 0.1D, 0));
            this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
            double newYaw = 180.0D - Math.atan2(distanceX, distanceZ) * 180.0D / Math.PI;
            double differenceYaw = MathHelper.wrapDegrees(newYaw - (double)this.rotationYaw);

            limitDifferenceYaw = 50.0D;
            
            if (differenceYaw > limitDifferenceYaw) {
                differenceYaw = limitDifferenceYaw;
            }

            if (differenceYaw < -limitDifferenceYaw) {
                differenceYaw = -limitDifferenceYaw;
            }

            Vector3d distanceVector = new Vector3d(this.targetX - this.getPosX(), this.targetY - this.getPosY(), this.targetZ - this.getPosZ()).normalize();
            Vector3d rotationVector = new Vector3d((double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), m.y, (double)(-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
            float dynamicMotionMultiplier = (float)(rotationVector.dotProduct(distanceVector) + 0.5D) / 1.5F;

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
            this.rotationYaw += this.randomYawVelocity * 0.1F;
            float scaledMotionDistanceHeightPlane = (float)(2.0D / (motionDistanceHeightPlane + 1.0D));
            float staticMotionMultiplier = 0.03F;
            this.moveRelative(staticMotionMultiplier * (dynamicMotionMultiplier * scaledMotionDistanceHeightPlane + (1.0F - scaledMotionDistanceHeightPlane)), new Vector3d(0.0D, 0.0D, -1.0D));

            this.move(MoverType.SELF, getMotion());

            Vector3d vec3d3 = this.getMotion().normalize();
            double d6 = 0.8D + 0.15D * (vec3d3.dotProduct(rotationVector) + 1.0D) / 2.0D;
            this.setMotion(this.getMotion().mul(d6, 0.91F, d6));
        }

        this.renderYawOffset = this.rotationYaw;
        
        if (!this.world.isRemote() && this.hurtTime == 0 && this.isAlive()) {
            this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(1.0D, 0.0D, 1.0D)));
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
        
        if (!this.world.isRemote() && this.world.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
    }

    private void attackEntitiesInList(List<Entity> entities) {
        int chance = EntityPoisonousLibelleConfig.poisonChance;
        for (Entity entity : entities) {
            if(chance > 0 && world.rand.nextInt(chance) == 0) {
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
                            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 0.5F);
                        ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, POISON_DURATION * 20, 1));
                    }
                }
            }
        }
    }

    private void setNewTarget() {
        this.forceNewTarget = false;

        boolean targetSet = false;
        if (this.rand.nextInt(2) == 0 && !this.world.getPlayers().isEmpty() && !this.world.isDaytime()) {
            this.target = (Entity)this.world.getPlayers().get(this.rand.nextInt(this.world.getPlayers().size()));
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
                this.targetX = this.getPosX();
                this.targetY = (double)(MAXHEIGHT - this.rand.nextFloat() * 30.0F);
                this.targetZ = this.getPosZ();
                this.targetX += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                this.targetZ += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                double d0 = this.getPosX() - this.targetX;
                double d1 = this.getPosY() - this.targetY;
                double d2 = this.getPosZ() - this.targetZ;
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