package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.ExtendedDamageSources;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

/**
 * Entity for the anti-vengeance beams.
 * @author rubensworks
 *
 */
public class EntityAttackVengeanceBeam extends EntityAntiVengeanceBeam {

    public EntityAttackVengeanceBeam(EntityType<? extends EntityAttackVengeanceBeam> type, Level world) {
        super(type, world);
    }

    public EntityAttackVengeanceBeam(Level world, LivingEntity entity) {
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
        Vec3 motion = getDeltaMovement();

        Minecraft.getInstance().levelRenderer.addParticle(
                new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                getX(), getY(), getZ(),
                deriveMotion(motion.x), deriveMotion(motion.y), deriveMotion(motion.z));
    }

    private double deriveMotion(double motion) {
        return motion * 1D + (0.02D - random.nextDouble() * 0.04D);
    }

    @Override
    protected void applyHitEffect(Entity entity) {
        if (entity instanceof EntityVengeanceSpirit) {
            if (this.getOwner() instanceof LivingEntity) {
                entity.hurt(ExtendedDamageSources.vengeanceBeam((LivingEntity) this.getOwner()), 1F);
                ((EntityVengeanceSpirit) entity).setRemainingLife(((EntityVengeanceSpirit) entity).getRemainingLife() + 10);
            }
        } else if (entity instanceof LivingEntity) {
            LivingEntity entityLiving = (LivingEntity) entity;
            entityLiving.removeAllEffects();
        }
    }
}
