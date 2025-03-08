package org.cyclops.evilcraft.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class BoxOfEternalClosureCaptureTriggerConfig extends CriterionTriggerConfigCommon<BoxOfEternalClosureCaptureTrigger.Instance, IModBase> {

    public BoxOfEternalClosureCaptureTriggerConfig() {
        super(
                EvilCraft._instance,
                "box_of_eternal_closure_capture",
                new BoxOfEternalClosureCaptureTrigger()
        );
    }

}
