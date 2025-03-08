package org.cyclops.evilcraft.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class FartTriggerConfig extends CriterionTriggerConfigCommon<FartTrigger.Instance, IModBase> {

    public FartTriggerConfig() {
        super(
                EvilCraft._instance,
                "fart",
                new FartTrigger()
        );
    }

}
