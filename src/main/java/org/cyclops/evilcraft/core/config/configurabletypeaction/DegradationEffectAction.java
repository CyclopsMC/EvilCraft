package org.cyclops.evilcraft.core.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeAction;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.api.degradation.IDegradationRegistry;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * The action used for {@link DegradationEffectConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class DegradationEffectAction extends ConfigurableTypeAction<DegradationEffectConfig, IDegradationEffect> {

    @Override
    public void onRegisterSetup(DegradationEffectConfig eConfig) {
        super.onRegisterSetup(eConfig);
        EvilCraft._instance.getRegistryManager().getRegistry(IDegradationRegistry.class).registerDegradationEffect(
                eConfig.getNamedId(), eConfig.getInstance(), eConfig.getWeight());
    }

}
