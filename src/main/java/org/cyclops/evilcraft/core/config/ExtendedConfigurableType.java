package org.cyclops.evilcraft.core.config;

import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.evilcraft.core.config.configurabletypeaction.DegradationEffectAction;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * The different types of {@link org.cyclops.cyclopscore.config.configurable.IConfigurable}.
 * @author rubensworks
 *
 */
public class ExtendedConfigurableType {
    /**
     * Degradation effect type.
     */
    public static final ConfigurableType DEGRADATIONEFFECT = new ConfigurableType(true, DegradationEffectConfig.class, new DegradationEffectAction(), "degradation effect");
}
