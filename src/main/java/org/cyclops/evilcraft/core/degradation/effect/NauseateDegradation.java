package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

import java.util.List;

/**
 * Degradation effect that will nauseate entities in the degradation area.
 * @author rubensworks
 *
 */
public class NauseateDegradation implements IDegradationEffect {
    
    private static final int MINIMUM_DEGRADATION = 5;
    private static final int NAUSEA_DURATION_MULTIPLIER = 20 * 4;
    
    public NauseateDegradation(DegradationEffectConfig eConfig) {

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
                ((LivingEntity) entity).addPotionEffect(
                        new EffectInstance(Effects.NAUSEA,
                                (int) degradable.getDegradation() * NAUSEA_DURATION_MULTIPLIER, 1));
            }
        }
    }

}
