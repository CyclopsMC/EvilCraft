package org.cyclops.evilcraft.core.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeActionCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.api.degradation.IDegradationRegistry;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * The action used for {@link DegradationEffectConfig}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
public class DegradationEffectAction extends ConfigurableTypeActionCommon<DegradationEffectConfig, IDegradationEffect, IModBase> {

    @Override
    public void onRegisterSetup(DegradationEffectConfig eConfig) {
        super.onRegisterSetup(eConfig);
        EvilCraft._instance.getRegistryManager().getRegistry(IDegradationRegistry.class).registerDegradationEffect(
                eConfig.getNamedId(), eConfig.getInstance(), eConfig.getWeight());
    }

}
