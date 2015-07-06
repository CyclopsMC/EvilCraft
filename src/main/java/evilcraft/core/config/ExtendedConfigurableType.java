package evilcraft.core.config;

import evilcraft.core.config.configurabletypeaction.DegradationEffectAction;
import evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.cyclopscore.config.ConfigurableType;

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
