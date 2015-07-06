package evilcraft.core.degradation.effect;

import evilcraft.api.degradation.IDegradable;
import evilcraft.core.config.configurable.ConfigurableDegradationEffect;
import evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import evilcraft.item.MaceOfDistortion;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.List;

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
        BlockPos center = degradable.getLocation();
        double x = center.getX();
        double y = center.getY();
        double z = center.getZ();
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
