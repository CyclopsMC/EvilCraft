package org.cyclops.evilcraft.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DistortTriggerConfig extends CriterionTriggerConfigCommon<DistortTrigger.Instance, IModBase> {

    public DistortTriggerConfig() {
        super(
                EvilCraft._instance,
                "distort",
                new DistortTrigger()
        );
    }

}
