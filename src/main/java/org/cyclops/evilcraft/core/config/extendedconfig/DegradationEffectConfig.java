package org.cyclops.evilcraft.core.config.extendedconfig;

import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.core.config.ExtendedConfigurableType;

import java.util.function.Function;

/**
 * Config for degradation effects.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class DegradationEffectConfig extends ExtendedConfig<DegradationEffectConfig, IDegradationEffect> {

    private int weight;

    public DegradationEffectConfig(String namedId, Function<DegradationEffectConfig, ? extends IDegradationEffect> elementConstructor, int weight) {
        super(EvilCraft._instance, namedId, elementConstructor);
        this.weight = weight;
    }

    @Override
    public String getTranslationKey() {
        return "degradationeffect." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ExtendedConfigurableType.DEGRADATIONEFFECT;
    }

    /**
     * Get the weight.
     * @return The weight.
     */
    public int getWeight() {
        return weight;
    }

}
