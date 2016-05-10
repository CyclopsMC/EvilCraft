package org.cyclops.evilcraft.entity.monster;

import net.minecraft.entity.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.item.PoisonSacConfig;

import java.util.List;

/**
 * A libelle that poisons you.
 * @author rubensworks
 *
 */
public class PoisonousLibelle extends EntityFlying implements IConfigurable, IMob {
    
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

    /**
     * Make a new instance.
     * @param world The world.
     */
    public PoisonousLibelle(World world) {
        super(world);

        this.setSize(0.5F, 0.45F);
        this.isImmuneToFire = false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3125D);
    }

    @Override
    protected Item getDropItem() {
        if(Configs.isEnabled(PoisonSacConfig.class))
            return PoisonSacConfig._instance.getItemInstance();
        else
            return super.getDropItem();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.entity_bat_hurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.entity_bat_death;
    }

    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onLivingUpdate() {        
        float f;
        float f1;

        if (this.worldObj.isRemote) {
            f = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
            f1 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

            if (f1 <= -0.3F && f >= -0.3F && this.rand.nextInt(45) == 0) {
                this.worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.entity_bat_ambient,
                        SoundCategory.AMBIENT, 0.1F, 0.8F + this.rand.nextFloat() * 0.3F);
            }
        }

        this.prevAnimTime = this.animTime;

        f = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
        f *= (float)Math.pow(2.0D, this.motionY);

        this.animTime += f;
        
        double distanceY;
        double distanceZ;
        double distance;
        double distanceX;
        float limitDistanceY;
        double limitDifferenceYaw;

        if (this.worldObj.isRemote) {
            // Correct rotation of the entity when rotating
            if (this.newPosRotationIncrements > 0) {
                distanceX = this.posX + (this.interpTargetX - this.posX) / (double)this.newPosRotationIncrements;
                distanceY = this.posY + (this.interpTargetY - this.posY) / (double)this.newPosRotationIncrements;
                distanceZ = this.posZ + (this.interpTargetZ - this.posZ) / (double)this.newPosRotationIncrements;
                distance = MathHelper.wrapAngleTo180_double(this.interpTargetYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + distance / (double)this.newPosRotationIncrements);
                // MCP: newPosX should probably be interpPitch
                this.rotationPitch = (float)((double)this.rotationPitch + (this.newPosX - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                --this.newPosRotationIncrements;
                this.setPosition(distanceX, distanceY, distanceZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        } else {
            // Calc distance to target
            distanceX = this.targetX - this.posX;
            distanceY = this.targetY - this.posY;
            distanceZ = this.targetZ - this.posZ;
            distance = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;

            if (this.target != null) {
                this.targetX = this.target.posX;
                this.targetZ = this.target.posZ;
                double distancedX = this.targetX - this.posX;
                double distancedZ = this.targetZ - this.posZ;
                double distancedHeightPlane = Math.sqrt(distancedX * distancedX + distancedZ * distancedZ);
                double Yplus = 0.4D + distancedHeightPlane / 80.0D - 1.0D;

                // Limit height difference
                if (Yplus > 10.0D) {
                    Yplus = 10.0D;
                }

                this.targetY = Math.min(this.target.getEntityBoundingBox().minY + Yplus, MAXHEIGHT);
            } else {
                this.targetX += this.rand.nextGaussian() * 2.0D;
                this.targetZ += this.rand.nextGaussian() * 2.0D;
            }

            // Reset target
            if (this.forceNewTarget
                    || distance < 3.0D
                    || distance > 250.0D
                    || this.isCollidedHorizontally
                    || this.isCollidedVertically
                    || this.targetY > MAXHEIGHT) {
                this.setNewTarget();
            }

            distanceY /= (double) MathHelper.sqrt_double(distanceX * distanceX + distanceZ * distanceZ);
            limitDistanceY = 0.6F;

            if (distanceY < (double)(-limitDistanceY)) {
                distanceY = (double)(-limitDistanceY);
            }

            if (distanceY > (double)limitDistanceY) {
                distanceY = (double)limitDistanceY;
            }

            this.motionY += distanceY * 0.1D;
            this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
            double newYaw = 180.0D - Math.atan2(distanceX, distanceZ) * 180.0D / Math.PI;
            double differenceYaw = MathHelper.wrapAngleTo180_double(newYaw - (double)this.rotationYaw);

            limitDifferenceYaw = 50.0D;
            
            if (differenceYaw > limitDifferenceYaw) {
                differenceYaw = limitDifferenceYaw;
            }

            if (differenceYaw < -limitDifferenceYaw) {
                differenceYaw = -limitDifferenceYaw;
            }

            Vec3d distanceVector = new Vec3d(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
            Vec3d rotationVector = new Vec3d((double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, (double)(-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
            float dynamicMotionMultiplier = (float)(rotationVector.dotProduct(distanceVector) + 0.5D) / 1.5F;

            if (dynamicMotionMultiplier < 0.0F) {
                dynamicMotionMultiplier = 0.0F;
            }

            this.randomYawVelocity *= 0.8F;
            float motionDistanceHeightPlaneFloat = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
            double motionDistanceHeightPlane = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;

            if (motionDistanceHeightPlane > 40.0D) {
                motionDistanceHeightPlane = 40.0D;
            }

            this.randomYawVelocity = (float)((double)this.randomYawVelocity + differenceYaw * (0.7D / motionDistanceHeightPlane / (double)motionDistanceHeightPlaneFloat));
            this.rotationYaw += this.randomYawVelocity * 0.1F;
            float scaledMotionDistanceHeightPlane = (float)(2.0D / (motionDistanceHeightPlane + 1.0D));
            float staticMotionMultiplier = 0.06F;
            this.moveFlying(0.0F, -1.0F, staticMotionMultiplier * (dynamicMotionMultiplier * scaledMotionDistanceHeightPlane + (1.0F - scaledMotionDistanceHeightPlane)));

            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            Vec3d motionVector = new Vec3d(this.motionX, this.motionY, this.motionZ).normalize();
            float motionRotation = (float)(motionVector.dotProduct(rotationVector) + 1.0D) / 2.0F;
            motionRotation = 0.8F + 0.15F * motionRotation;
            this.motionX *= (double)motionRotation;
            this.motionZ *= (double)motionRotation;
            this.motionY *= 0.9D;
            
            this.motionX /= 1.5;
            this.motionY /= 1.2;
            this.motionZ /= 1.5;
        }

        this.renderYawOffset = this.rotationYaw;
        
        if (!this.worldObj.isRemote && this.hurtTime == 0 && !this.isDead) {
            this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D)));
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
        
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    private void attackEntitiesInList(List<Entity> entities) {
        int chance = PoisonousLibelleConfig.poisonChance;
        for (Entity entity : entities) {
            if(chance > 0 && worldObj.rand.nextInt(chance) == 0) {
                if (entity instanceof EntityLivingBase) {
                    boolean shouldAttack = true;
                    if (entity instanceof EntityPlayer) {
                        if (((EntityPlayer) entity).capabilities.isCreativeMode) {
                            shouldAttack = false;
                            setNewTarget();
                        }
                    }
                    if (shouldAttack) {
                        if (PoisonousLibelleConfig.hasAttackDamage)
                            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 0.5F);
                        ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.poison, POISON_DURATION * 20, 1));
                    }
                }
            }
        }
    }

    private void setNewTarget() {
        this.forceNewTarget = false;

        boolean targetSet = false;
        if (this.rand.nextInt(2) == 0 && !this.worldObj.playerEntities.isEmpty() && !this.worldObj.isDaytime()) {
            this.target = (Entity)this.worldObj.playerEntities.get(this.rand.nextInt(this.worldObj.playerEntities.size()));
            targetSet = true;
            if(target instanceof EntityPlayer) {
                if(((EntityPlayer)target).capabilities.isCreativeMode) {
                    targetSet = false;
                }
            }
        }
        
        if(!targetSet) {
            boolean flag;

            do {
                this.targetX = this.posX;
                this.targetY = (double)(MAXHEIGHT - this.rand.nextFloat() * 30.0F);
                this.targetZ = this.posZ;
                this.targetX += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                this.targetZ += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                double d0 = this.posX - this.targetX;
                double d1 = this.posY - this.targetY;
                double d2 = this.posZ - this.targetZ;
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

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }

}