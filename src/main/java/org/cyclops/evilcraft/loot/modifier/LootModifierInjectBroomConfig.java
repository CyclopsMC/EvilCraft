package org.cyclops.evilcraft.loot.modifier;

import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfigNeoForge;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootModifierInjectBroomConfig extends LootModifierConfigNeoForge<LootModifierInjectBroom> {
    public LootModifierInjectBroomConfig() {
        super(EvilCraft._instance, "inject_broom", (eConfig) -> LootModifierInjectBroom.CODEC.get());
    }
}
