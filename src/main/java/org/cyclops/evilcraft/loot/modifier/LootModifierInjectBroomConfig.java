package org.cyclops.evilcraft.loot.modifier;

import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootModifierInjectBroomConfig extends LootModifierConfig<LootModifierInjectBroom> {
    public LootModifierInjectBroomConfig() {
        super(EvilCraft._instance, "inject_broom", (eConfig) -> LootModifierInjectBroom.CODEC.get());
    }
}
