package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.item.BloodPearlOfTeleportation;
import org.cyclops.evilcraft.item.BloodPearlOfTeleportationConfig;

/**
 * Entity for the {@link BloodPearlOfTeleportation}.
 * @author rubensworks
 *
 */
public class EntityBloodPearl extends EntityThrowable implements IConfigurable {
    
    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityBloodPearl(World world) {
        super(world);
    }

    /**
     * Make a new instance in a world by a placer {@link EntityLivingBase}.
     * @param world The world.
     * @param entity The {@link EntityLivingBase} that placed this {@link Entity}.
     */
    public EntityBloodPearl(World world, EntityLivingBase entity) {
        super(world, entity);
    }
    
    /**
     * Make a new instance at the given location in a world.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    @SideOnly(Side.CLIENT)
    public EntityBloodPearl(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    protected void onImpact(RayTraceResult position) {
        if (position.entityHit != null) {
            position.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        for (int i = 0; i < 32; ++i) {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }

        if (!this.worldObj.isRemote) {
            if (this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP) {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this.getThrower();

                if (entityplayermp.playerNetServerHandler.netManager.isChannelOpen() && entityplayermp.worldObj == this.worldObj) {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, this.posX, this.posY, this.posZ, 0.0F);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        if (this.getThrower().isRiding()) {
                            this.getThrower().dismountRidingEntity();
                        }
    
                        this.getThrower().setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        this.getThrower().fallDistance = 0.0F;
                        this.getThrower().attackEntityFrom(DamageSource.fall, event.getAttackDamage());
                        this.getThrower().addPotionEffect(new PotionEffect(MobEffects.moveSlowdown,
                        		BloodPearlOfTeleportationConfig.slownessDuration * 20, 2));
                    }
                }
            }

            this.setDead();
        }
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }

}
