package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.potion.PotionPalingConfig;

import java.util.List;

/**
 * Degradation effect that will apply paling to entities in the degradation area.
 * @author rubensworks
 *
 */
public class PalingDegradation extends ConfigurableDegradationEffect {

    private static PalingDegradation _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PalingDegradation getInstance() {
        return _instance;
    }

    private static final int MINIMUM_DEGRADATION = 10;
    private static final int PALING_DURATION_MULTIPLIER = 20 * 5;

    public PalingDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
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
                                PotionPalingConfig._instance.getPotion(),
                                (int) degradable.getDegradation() * PALING_DURATION_MULTIPLIER, Math.min(10, (int) degradable.getDegradation() / 10))
                        );
            }
        }
    }

}
