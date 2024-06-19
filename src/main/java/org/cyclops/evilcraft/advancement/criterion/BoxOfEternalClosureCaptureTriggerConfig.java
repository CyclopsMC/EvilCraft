package org.cyclops.evilcraft.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class BoxOfEternalClosureCaptureTriggerConfig extends CriterionTriggerConfig<BoxOfEternalClosureCaptureTrigger.Instance> {

    public BoxOfEternalClosureCaptureTriggerConfig() {
        super(
                EvilCraft._instance,
                "box_of_eternal_closure_capture",
                new BoxOfEternalClosureCaptureTrigger()
        );
    }

}
