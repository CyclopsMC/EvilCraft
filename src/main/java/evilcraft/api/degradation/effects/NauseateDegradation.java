package evilcraft.api.degradation.effects;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import evilcraft.api.config.DegradationEffectConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableDegradationEffect;
import evilcraft.api.degradation.IDegradable;

/**
 * Degradation effect that will nauseate entities in the degradation area.
 * @author rubensworks
 *
 */
public class NauseateDegradation extends ConfigurableDegradationEffect {
    
    private static NauseateDegradation _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<DegradationEffectConfig> eConfig) {
        if(_instance == null)
            _instance = new NauseateDegradation(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static NauseateDegradation getInstance() {
        return _instance;
    }
    
    private static final int MINIMUM_DEGRADATION = 5;
    private static final int NAUSEA_DURATION_MULTIPLIER = 20 * 4;
    
    private NauseateDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
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
                                Potion.confusion.id,
                                (int) degradable.getDegradation() * NAUSEA_DURATION_MULTIPLIER, 1)
                        );
            }
        }
    }

}
