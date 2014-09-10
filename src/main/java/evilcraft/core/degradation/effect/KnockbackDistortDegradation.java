package evilcraft.core.degradation.effect;

import java.util.List;

import net.minecraft.entity.Entity;
import evilcraft.api.ILocation;
import evilcraft.api.degradation.IDegradable;
import evilcraft.core.config.configurable.ConfigurableDegradationEffect;
import evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.item.MaceOfDistortion;

/**
 * An effect that will knockback the entities within the range of the degradable.
 * @author rubensworks
 *
 */
public class KnockbackDistortDegradation extends ConfigurableDegradationEffect {

    private static KnockbackDistortDegradation _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<DegradationEffectConfig> eConfig) {
        if(_instance == null)
            _instance = new KnockbackDistortDegradation(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static KnockbackDistortDegradation getInstance() {
        return _instance;
    }
    
    private static final int MINIMUM_DEGRADATION = 3;
    private static final int POWER_LEVEL = 1;
    
    private KnockbackDistortDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
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
        ILocation center = degradable.getLocation();
        double x = center.getCoordinates()[0];
        double y = center.getCoordinates()[1];
        double z = center.getCoordinates()[2];
        for(Entity entity : entities) {
            MaceOfDistortion.distortEntity(
                    degradable.getWorld(),
                    null,
                    entity,
                    x, y, z,
                    (int) degradable.getDegradation() * 10,
                    POWER_LEVEL
            );
        }
    }
    
}
