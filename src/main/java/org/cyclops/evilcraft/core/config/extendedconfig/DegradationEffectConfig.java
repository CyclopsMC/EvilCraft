package org.cyclops.evilcraft.core.config.extendedconfig;

import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.config.ExtendedConfigurableType;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableDegradationEffect;

/**
 * Config for degradation effects.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class DegradationEffectConfig extends ExtendedConfig<DegradationEffectConfig> {

    private int weight;
    
    /**
     * Make a new instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     * @param weight The weight of the degradation effect.
     */
    public DegradationEffectConfig(boolean enabled, String namedId, String comment,
            Class<? extends ConfigurableDegradationEffect> element, int weight) {
        super(EvilCraft._instance, enabled, namedId, comment, element);
        this.weight = weight;
    }
    
    @Override
	public String getUnlocalizedName() {
		return "degradationeffect." + getNamedId();
	}
    
    @Override
	public ConfigurableType getHolderType() {
		return ExtendedConfigurableType.DEGRADATIONEFFECT;
	}
    
    /**
     * Get the biome configurable
     * @return The biome.
     */
    public ConfigurableDegradationEffect getDegradationEffect() {
        return (ConfigurableDegradationEffect) this.getSubInstance();
    }

    /**
     * Get the weight.
     * @return The weight.
     */
    public int getWeight() {
        return weight;
    }

}
