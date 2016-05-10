package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.item.MaceOfDistortion;

import java.util.List;

/**
 * An effect that will knockback the entities within the range of the degradable.
 * @author rubensworks
 *
 */
public class KnockbackDistortDegradation extends ConfigurableDegradationEffect {

    private static KnockbackDistortDegradation _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static KnockbackDistortDegradation getInstance() {
        return _instance;
    }
    
    private static final int MINIMUM_DEGRADATION = 3;
    private static final int POWER_LEVEL = 1;
    
    public KnockbackDistortDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
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
        BlockPos center = degradable.getLocation();
        double x = center.getX();
        double y = center.getY();
        double z = center.getZ();
        for(Entity entity : entities) {
            MaceOfDistortion.getInstance().distortEntity(
                    degradable.getDegradationWorld(),
                    null,
                    entity,
                    x, y, z,
                    (int) degradable.getDegradation() * 10,
                    POWER_LEVEL
            );
        }
    }
    
}
