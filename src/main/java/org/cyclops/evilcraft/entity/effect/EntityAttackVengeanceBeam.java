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
    	float scale = 0.6F - rand.nextFloat() * 0.3F;
    	float red = rand.nextFloat() * 0.03F + 0.1F;
        float green = rand.nextFloat() * 0.03F;
        float blue = rand.nextFloat() * 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 4.5D + 4D);
        Vector3d motion = getMotion();

        Minecraft.getInstance().worldRenderer.addParticle(
                new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                getPosX(), getPosY(), getPosZ(),
                deriveMotion(motion.x), deriveMotion(motion.y), deriveMotion(motion.z));
	}
    
    private double deriveMotion(double motion) {
    	return motion * 1D + (0.02D - rand.nextDouble() * 0.04D);
    }

    protected void applyHitEffect(Entity entity) {
        if (entity instanceof EntityVengeanceSpirit) {
            if (this.func_234616_v_() instanceof LivingEntity) {
                entity.attackEntityFrom(ExtendedDamageSource.vengeanceBeam((LivingEntity) this.func_234616_v_()), 1F);
                ((EntityVengeanceSpirit) entity).setRemainingLife(((EntityVengeanceSpirit) entity).getRemainingLife() + 10);
            }
        } else if (entity instanceof LivingEntity) {
            LivingEntity entityLiving = (LivingEntity) entity;
            entityLiving.clearActivePotions();
        }
    }
}
