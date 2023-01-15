package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

import java.util.List;

/**
 * Degradation effect that will apply paling to entities in the degradation area.
 * @author rubensworks
 *
 */
public class PalingDegradation implements IDegradationEffect {

    private static final int MINIMUM_DEGRADATION = 10;
    private static final int PALING_DURATION_MULTIPLIER = 20 * 5;

    public PalingDegradation(DegradationEffectConfig eConfig) {

    }

    @Override
    public boolean canRun(IDegradable degradable) {
        return degradable.getDegradation() >= MINIMUM_DEGRADATION;
    }

    @Override
    public void runClientSide(IDegradable degradable) {

    }

    @Override
    public void runServerSide(IDegradable degradable) {
        List<Entity> entities = degradable.getAreaEntities();
        for(Entity entity : entities) {
            if(entity instanceof LivingEntity) {
                ((LivingEntity) entity).addEffect(
                        new MobEffectInstance(RegistryEntries.POTION_PALING,
                                (int) degradable.getDegradation() * PALING_DURATION_MULTIPLIER, Math.min(10, (int) degradable.getDegradation() / 10)));
            }
        }
    }

}
