package org.cyclops.evilcraft.potion;

import net.minecraft.entity.EntityLivingBase;
import org.cyclops.cyclopscore.config.configurable.ConfigurablePotion;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.PotionConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.ExtendedDamageSource;

/**
 * Potion effect for letting entities fade away and leaving a portal behind in their place.
 * @author rubensworks
 *
 */
public class PotionPaling extends ConfigurablePotion {

    private static PotionPaling _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PotionPaling getInstance() {
        return _instance;
    }

    public PotionPaling(ExtendedConfig<PotionConfig> eConfig) {
        super(eConfig, true, Helpers.RGBToInt(56, 25, 97), 0);
    }

    @Override
    protected void onUpdate(EntityLivingBase entity) {
        entity.attackEntityFrom(ExtendedDamageSource.paling, ((float) getAmplifier(entity)) / 4);
    }
}
