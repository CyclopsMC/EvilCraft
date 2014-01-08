package evilcraft.entities.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.Configurable;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

public class EntityLightningGrenade extends EntityThrowable implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.ENTITY;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    public EntityLightningGrenade(World par1World) {
        super(par1World);
    }

    public EntityLightningGrenade(World par1World, EntityLivingBase par2EntityLivingBase) {
        super(par1World, par2EntityLivingBase);
    }
    
    @SideOnly(Side.CLIENT)
    public EntityLightningGrenade(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
    }

    @Override
    public String getUniqueName() {
        return "entities.item."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
    }

    @Override
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
        if (par1MovingObjectPosition.entityHit != null) {
            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        for (int i = 0; i < 32; ++i) {
            this.worldObj.spawnParticle("magicCrit", this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }

        if (!this.worldObj.isRemote) {
            if (this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP) {                
                this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ));
            }

            this.setDead();
        }
    }

}
