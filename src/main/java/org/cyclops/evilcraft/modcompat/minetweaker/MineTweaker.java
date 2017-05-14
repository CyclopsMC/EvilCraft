package org.cyclops.evilcraft.modcompat.minetweaker;

import minetweaker.MineTweakerAPI;
import org.cyclops.evilcraft.modcompat.minetweaker.handlers.BloodInfuserHandler;
import org.cyclops.evilcraft.modcompat.minetweaker.handlers.EnvironmentalAccumulatorHandler;

/**
 * @author rubensworks
 */
public class MineTweaker {

    public static void register() {
        MineTweakerAPI.registerClass(BloodInfuserHandler.class);
        MineTweakerAPI.registerClass(EnvironmentalAccumulatorHandler.class);
    }

}
