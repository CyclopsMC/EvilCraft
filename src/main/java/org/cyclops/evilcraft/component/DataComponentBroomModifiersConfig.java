package org.cyclops.evilcraft.component;

import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.broom.BroomModifiersContents;

/**
 * @author rubensworks
 */
public class DataComponentBroomModifiersConfig extends DataComponentConfigCommon<BroomModifiersContents, IModBase> {
    public DataComponentBroomModifiersConfig() {
        super(EvilCraft._instance, "broom_modifiers", builder -> builder
                .persistent(BroomModifiersContents.CODEC)
                .networkSynchronized(BroomModifiersContents.STREAM_CODEC));
    }
}
