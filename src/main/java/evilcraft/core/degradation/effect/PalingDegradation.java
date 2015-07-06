package evilcraft.core.degradation.effect;

import evilcraft.api.degradation.IDegradable;
import evilcraft.core.config.configurable.ConfigurableDegradationEffect;
import evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import evilcraft.potion.PotionPalingConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.List;

/**
 * Degradation effect that will apply paling to entities in the degradation area.
 * @author rubensworks
 *
 */
public class PalingDegradation extends ConfigurableDegradationEffect {

    private static PalingDegradation _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<DegradationEffectConfig> eConfig) {
        if(_instance == null)
            _instance = new PalingDegradation(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PalingDegradation getInstance() {
        return _instance;
    }

    private static final int MINIMUM_DEGRADATION = 10;
    private static final int PALING_DURATION_MULTIPLIER = 20 * 5;

    private PalingDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
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
                                PotionPalingConfig._instance.ID,
                                (int) degradable.getDegradation() * PALING_DURATION_MULTIPLIER, Math.min(10, (int) degradable.getDegradation() / 10))
                        );
            }
        }
    }

}
