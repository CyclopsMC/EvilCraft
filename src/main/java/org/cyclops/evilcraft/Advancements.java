package org.cyclops.evilcraft;

import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.evilcraft.advancement.criterion.BoxOfEternalClosureCaptureTrigger;
import org.cyclops.evilcraft.advancement.criterion.DistortTrigger;
import org.cyclops.evilcraft.advancement.criterion.FartTrigger;
import org.cyclops.evilcraft.advancement.criterion.NecromanceTrigger;

/**
 * Advancement-related logic.
 * @author rubensworks
 */
public class Advancements {

    public static final FartTrigger FART =
            AdvancementHelpers.registerCriteriaTrigger(new FartTrigger());
    public static final DistortTrigger DISTORT =
            AdvancementHelpers.registerCriteriaTrigger(new DistortTrigger());
    public static final BoxOfEternalClosureCaptureTrigger BOX_OF_ETERNAL_CLOSURE_CAPTURE =
            AdvancementHelpers.registerCriteriaTrigger(new BoxOfEternalClosureCaptureTrigger());
    public static final NecromanceTrigger NECROMANCE =
            AdvancementHelpers.registerCriteriaTrigger(new NecromanceTrigger());

    public static void load() {}

}
