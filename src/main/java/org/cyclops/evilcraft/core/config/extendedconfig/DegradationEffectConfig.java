package org.cyclops.evilcraft.core.config.extendedconfig;

import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.core.config.ExtendedConfigurableType;

import java.util.function.Function;

/**
 * Config for degradation effects.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class DegradationEffectConfig extends ExtendedConfigCommon<DegradationEffectConfig, IDegradationEffect, IModBase> {

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
    public ConfigurableTypeCommon getConfigurableType() {
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
