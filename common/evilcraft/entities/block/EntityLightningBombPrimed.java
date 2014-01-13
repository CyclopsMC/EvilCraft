package evilcraft.entities.block;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;

public class EntityLightningBombPrimed extends EntityTNTPrimed implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.ENTITY;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public EntityLightningBombPrimed(World par1World) {
        super(par1World);
        setFuse();
    }
    
    public EntityLightningBombPrimed(World world, double x, double y, double z, EntityLivingBase placer) {
        super(world, x, y, z, placer);
        setFuse();
    }
    
    protected void setFuse() {
        this.fuse = EntityLightningBombPrimedConfig.fuse;
    }

    @Override
    public String getUniqueName() {
        return "entities.block."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        if (this.fuse-- <= 0) {
            this.setDead();

            if (!this.worldObj.isRemote) {
                this.explode(this.worldObj, this.posX, this.posY, this.posZ);
            }
        } else {
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode(World world, double x, double y, double z) {
        Random rand = new Random();
        for (int i = 0; i < 32; ++i) {
            world.spawnParticle("magicCrit", x, y + rand.nextDouble() * 2.0D, z, rand.nextGaussian(), 0.0D, rand.nextGaussian());
        }

        if (!world.isRemote) {             
            world.addWeatherEffect(new EntityLightningBolt(world, x, y, z));
        }
    }
    
}
