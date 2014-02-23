package evilcraft.entities.monster;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.items.PoisonSacConfig;

/**
 * A libelle that poisons you.
 * @author rubensworks
 *
 */
public class PoisonousLibelle extends EntityFlying implements Configurable{

    protected ExtendedConfig<?> eConfig = null;

    /**
     * The type for this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.MOB;
    
    private static final int POISON_DURATION = 3;

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

    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    /**
     * Make a new instance.
     * @param world The world.
     */
    public PoisonousLibelle(World world) {
        super(world);

        this.getNavigator().setAvoidsWater(true);
        this.setSize(1.0F, 0.9F);
        this.isImmuneToFire = false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(1.0D);
    }

    @Override
    protected int getDropItemId() {
        if(PoisonSacConfig._instance.isEnabled())
            return PoisonSacConfig._instance.ID;
        else
            return super.getDropItemId();
    }

    @Override
    protected String getLivingSound() {
        return null;
    }

    @Override
    protected String getHurtSound() {
        return "mob.bat.hurt1";
    }

    @Override
    protected String getDeathSound() {
        return "mob.bat.death";
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public String getUniqueName() {
        return "entities.monster."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
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

            if (f1 <= -0.3F && f >= -0.3F) {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.bat.idle", 0.5F, 0.8F + this.rand.nextFloat() * 0.3F, false);
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
                distanceX = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
                distanceY = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
                distanceZ = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;
                distance = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + distance / (double)this.newPosRotationIncrements);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
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

                this.targetY = Math.min(this.target.boundingBox.minY + Yplus, MAXHEIGHT);
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

            distanceY /= (double)MathHelper.sqrt_double(distanceX * distanceX + distanceZ * distanceZ);
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

            Vec3 distanceVector = this.worldObj.getWorldVec3Pool().getVecFromPool(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
            Vec3 rotationVector = this.worldObj.getWorldVec3Pool().getVecFromPool((double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, (double)(-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
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

            Vec3 motionVector = this.worldObj.getWorldVec3Pool().getVecFromPool(this.motionX, this.motionY, this.motionZ).normalize();
            float motionRotation = (float)(motionVector.dotProduct(rotationVector) + 1.0D) / 2.0F;
            motionRotation = 0.8F + 0.15F * motionRotation;
            this.motionX *= (double)motionRotation;
            this.motionZ *= (double)motionRotation;
            this.motionY *= 0.9D;
        }

        this.renderYawOffset = this.rotationYaw;
        
        if (!this.worldObj.isRemote && this.hurtTime == 0) {
            this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 0.0D, 1.0D)));
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
        
        if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0) {
            this.setDead();
        }
    }

    private void attackEntitiesInList(List<Entity> entities) {
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = (Entity)entities.get(i);
            if (entity instanceof EntityLivingBase) {
                boolean shouldAttack = true;
                if(entity instanceof EntityPlayer) {
                    if(((EntityPlayer)entity).capabilities.isCreativeMode) {
                        shouldAttack = false;
                        setNewTarget();
                    }
                }
                if(shouldAttack) {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), 0.5F);
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, POISON_DURATION * 20, 1));
                }
            }
        }
    }

    private void setNewTarget() {
        this.forceNewTarget = false;

        boolean targetSet = false;
        if (this.rand.nextInt(2) == 0 && !this.worldObj.playerEntities.isEmpty()) {
            this.target = (Entity)this.worldObj.playerEntities.get(this.rand.nextInt(this.worldObj.playerEntities.size()));
            targetSet = true;
            if(target instanceof EntityPlayer) {
                if(((EntityPlayer)target).capabilities.isCreativeMode) {
                    targetSet = false;
                }
            }
        }
        
        if(!targetSet) {
            boolean flag = false;

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

}