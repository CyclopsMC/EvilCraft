package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.evilcraft.Achievements;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.client.particle.EntityBlurFX;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;

/**
 * Entity for the anti-vengeance beams.
 * @author rubensworks
 *
 */
public class EntityAttackVengeanceBeam extends EntityAntiVengeanceBeam {

    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityAttackVengeanceBeam(World world) {
        super(world);
    }

    /**
     * Make a new instance in a world by a placer {@link EntityLivingBase}.
     * @param world The world.
     * @param entity The {@link EntityLivingBase} that placed this {@link Entity}.
     */
    public EntityAttackVengeanceBeam(World world, EntityLivingBase entity) {
        super(world, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void showNewBlurParticle() {
    	float scale = 0.6F - rand.nextFloat() * 0.3F;
    	float red = rand.nextFloat() * 0.03F + 0.1F;
        float green = rand.nextFloat() * 0.03F;
        float blue = rand.nextFloat() * 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 4.5D + 4D);
        
		EntityBlurFX blur = new EntityBlurFX(worldObj, posX, posY, posZ, scale,
				deriveMotion(motionX), deriveMotion(motionY), deriveMotion(motionZ),
				red, green, blue, ageMultiplier);
		Minecraft.getMinecraft().effectRenderer.addEffect(blur);
	}
    
    private double deriveMotion(double motion) {
    	return motion * 1D + (0.02D - rand.nextDouble() * 0.04D);
    }

    protected void applyHitEffect(Entity entity) {
        if (entity instanceof VengeanceSpirit) {
            ((EntityPlayerMP) this.getThrower()).addStat(Achievements.CLOSURE, 1);
            entity.attackEntityFrom(ExtendedDamageSource.vengeanceBeam(this.getThrower()), 1F);
            ((VengeanceSpirit) entity).setRemainingLife(((VengeanceSpirit) entity).getRemainingLife() + 10);
        } else if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            entityLiving.clearActivePotions();
        }
    }
}
