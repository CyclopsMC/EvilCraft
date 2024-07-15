package org.cyclops.evilcraft.component;

import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.broom.BroomModifiersContents;

/**
 * @author rubensworks
 */
public class DataComponentBroomModifiersConfig extends DataComponentConfig<BroomModifiersContents> {
    public DataComponentBroomModifiersConfig() {
        super(EvilCraft._instance, "broom_modifiers", builder -> builder
                .persistent(BroomModifiersContents.CODEC)
                .networkSynchronized(BroomModifiersContents.STREAM_CODEC));
    }
}
