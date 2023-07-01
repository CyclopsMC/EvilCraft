package org.cyclops.evilcraft.loot.modifier;

import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootModifierInjectBoxOfEternalClosureConfig extends LootModifierConfig<LootModifierInjectBoxOfEternalClosure> {
    public LootModifierInjectBoxOfEternalClosureConfig() {
        super(EvilCraft._instance, "inject_box_of_eternal_closure", (eConfig) -> LootModifierInjectBoxOfEternalClosure.CODEC.get());
    }
}
