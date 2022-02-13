package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

/**
 * Entity for the anti-vengeance beams.
 * @author rubensworks
 *
 */
public class EntityAttackVengeanceBeam extends EntityAntiVengeanceBeam {

    public EntityAttackVengeanceBeam(EntityType<? extends EntityAttackVengeanceBeam> type, World world) {
        super(type, world);
    }

    public EntityAttackVengeanceBeam(World world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_ATTACK_VENGEANCE_BEAM, world, entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void showNewBlurParticle() {
    	float scale = 0.6F - random.nextFloat() * 0.3F;
    	float red = random.nextFloat() * 0.03F + 0.1F;
        float green = random.nextFloat() * 0.03F;
        float blue = random.nextFloat() * 0.05F;
        float ageMultiplier = (float) (random.nextDouble() * 4.5D + 4D);
        Vector3d motion = getDeltaMovement();

        Minecraft.getInstance().levelRenderer.addParticle(
                new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                getX(), getY(), getZ(),
                deriveMotion(motion.x), deriveMotion(motion.y), deriveMotion(motion.z));
	}
    
    private double deriveMotion(double motion) {
    	return motion * 1D + (0.02D - random.nextDouble() * 0.04D);
    }

    protected void applyHitEffect(Entity entity) {
        if (entity instanceof EntityVengeanceSpirit) {
            if (this.getOwner() instanceof LivingEntity) {
                entity.hurt(ExtendedDamageSource.vengeanceBeam((LivingEntity) this.getOwner()), 1F);
                ((EntityVengeanceSpirit) entity).setRemainingLife(((EntityVengeanceSpirit) entity).getRemainingLife() + 10);
            }
        } else if (entity instanceof LivingEntity) {
            LivingEntity entityLiving = (LivingEntity) entity;
            entityLiving.removeAllEffects();
        }
    }
}
