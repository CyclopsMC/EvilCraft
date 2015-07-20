package org.cyclops.evilcraft.core.degradation;

import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.api.degradation.IDegradationRegistry;

import java.util.*;

/**
 * Registry for all the {@link IDegradationEffect}.
 * @author rubensworks
 *
 */
public class DegradationRegistry implements IDegradationRegistry {

    private static final Set<IDegradationEffect> DEGRADATION_EFFECTS =
            new LinkedHashSet<IDegradationEffect>();
    private static final List<IDegradationEffect> WEIGHTED_LIST =
            new ArrayList<IDegradationEffect>();
    private static final Random random = new Random();
    
    @Override
    public void registerDegradationEffect(String nameID, IDegradationEffect degradationEffect, int weight) {
        DEGRADATION_EFFECTS.add(degradationEffect);
        for(int i = 0; i < weight; i++) {
            WEIGHTED_LIST.add(degradationEffect);
        }
    }
    
    @Override
    public Set<IDegradationEffect> getDegradationEffects() {
        return DEGRADATION_EFFECTS;
    }
    
    @Override
    public IDegradationEffect getRandomDegradationEffect() {
        int index = random.nextInt(WEIGHTED_LIST.size());
        return WEIGHTED_LIST.get(index);
    }
    
}
