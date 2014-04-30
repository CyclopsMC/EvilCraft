package evilcraft.api.degradation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Registry for all the {@link IDegradationEffect}.
 * @author rubensworks
 *
 */
public class DegradationRegistry {

    private static final Set<IDegradationEffect> DEGRADATION_EFFECTS =
            new LinkedHashSet<IDegradationEffect>();
    private static final List<IDegradationEffect> WEIGHTED_LIST =
            new ArrayList<IDegradationEffect>();
    private static final Random random = new Random();
    
    /**
     * Register a new degradation effect.
     * @param nameID The unique name of this effect.
     * @param degradationEffect The effect to register.
     * @param weight The weight that this effect can occur.
     */
    public static void registerDegradationEffect(String nameID, IDegradationEffect degradationEffect, int weight) {
        DEGRADATION_EFFECTS.add(degradationEffect);
        for(int i = 0; i < weight; i++) {
            WEIGHTED_LIST.add(degradationEffect);
        }
    }
    
    /**
     * Get all the registered degradation effects.
     * @return The registered effects.
     */
    public static Set<IDegradationEffect> getDegradationEffects() {
        return DEGRADATION_EFFECTS;
    }
    
    /**
     * Get a degradation effect based on a weighted random choice.
     * @return A weighted random effect.
     */
    public static IDegradationEffect getRandomDegradationEffect() {
        int index = random.nextInt(WEIGHTED_LIST.size());
        return WEIGHTED_LIST.get(index);
    }
    
}
