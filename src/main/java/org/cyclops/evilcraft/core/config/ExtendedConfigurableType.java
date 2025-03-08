package org.cyclops.evilcraft.core.config;

import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.evilcraft.core.config.configurabletypeaction.DegradationEffectAction;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * The different types of configurable.
 * @author rubensworks
 *
 */
public class ExtendedConfigurableType {
    /**
     * Degradation effect type.
     */
    public static final ConfigurableTypeCommon DEGRADATIONEFFECT = new ConfigurableTypeCommon(true, DegradationEffectConfig.class, new DegradationEffectAction(), "degradation_effect");
}
