package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

import java.util.List;

/**
 * Degradation effect that will nauseate entities in the degradation area.
 * @author rubensworks
 *
 */
public class NauseateDegradation extends ConfigurableDegradationEffect {
    
    private static NauseateDegradation _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static NauseateDegradation getInstance() {
        return _instance;
    }
    
    private static final int MINIMUM_DEGRADATION = 5;
    private static final int NAUSEA_DURATION_MULTIPLIER = 20 * 4;
    
    public NauseateDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
        super(eConfig);
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
            if(entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(
                        new PotionEffect(
                                MobEffects.confusion,
                                (int) degradable.getDegradation() * NAUSEA_DURATION_MULTIPLIER, 1)
                        );
            }
        }
    }

}
