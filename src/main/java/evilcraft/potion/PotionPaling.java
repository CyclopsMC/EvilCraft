package evilcraft.potion;

import evilcraft.ExtendedDamageSource;
import evilcraft.core.config.configurable.ConfigurablePotion;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.PotionConfig;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.entity.EntityLivingBase;

/**
 * Potion effect for letting entities fade away and leaving a portal behind in their place.
 * @author rubensworks
 *
 */
public class PotionPaling extends ConfigurablePotion {

    private static PotionPaling _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<PotionConfig> eConfig) {
        if(_instance == null)
            _instance = new PotionPaling(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PotionPaling getInstance() {
        return _instance;
    }

    private PotionPaling(ExtendedConfig<PotionConfig> eConfig) {
        super(eConfig, true, RenderHelpers.RGBToInt(56, 25, 97), 0);
    }

    @Override
    protected void onUpdate(EntityLivingBase entity) {
        entity.attackEntityFrom(ExtendedDamageSource.paling, ((float) getAmplifier(entity)) / 4);
    }
}
