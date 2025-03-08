package org.cyclops.evilcraft.component;

import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.broom.BroomPartsContents;

/**
 * @author rubensworks
 */
public class DataComponentBroomPartsConfig extends DataComponentConfigCommon<BroomPartsContents, IModBase> {
    public DataComponentBroomPartsConfig() {
        super(EvilCraft._instance, "broom_parts", builder -> builder
                .persistent(BroomPartsContents.CODEC)
                .networkSynchronized(BroomPartsContents.STREAM_CODEC));
    }
}
