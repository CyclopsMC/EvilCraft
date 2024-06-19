package org.cyclops.evilcraft.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DistortTriggerConfig extends CriterionTriggerConfig<DistortTrigger.Instance> {

    public DistortTriggerConfig() {
        super(
                EvilCraft._instance,
                "distort",
                new DistortTrigger()
        );
    }

}
