package org.cyclops.evilcraft.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class NecromanceTriggerConfig extends CriterionTriggerConfig<NecromanceTrigger.Instance> {

    public NecromanceTriggerConfig() {
        super(
                EvilCraft._instance,
                "necromance",
                new NecromanceTrigger()
        );
    }

}
