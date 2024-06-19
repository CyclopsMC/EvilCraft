package org.cyclops.evilcraft.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class FartTriggerConfig extends CriterionTriggerConfig<FartTrigger.Instance> {

    public FartTriggerConfig() {
        super(
                EvilCraft._instance,
                "fart",
                new FartTrigger()
        );
    }

}
