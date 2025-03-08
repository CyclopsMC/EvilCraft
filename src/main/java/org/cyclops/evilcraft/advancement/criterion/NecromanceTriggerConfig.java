package org.cyclops.evilcraft.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class NecromanceTriggerConfig extends CriterionTriggerConfigCommon<NecromanceTrigger.Instance, IModBase> {

    public NecromanceTriggerConfig() {
        super(
                EvilCraft._instance,
                "necromance",
                new NecromanceTrigger()
        );
    }

}
