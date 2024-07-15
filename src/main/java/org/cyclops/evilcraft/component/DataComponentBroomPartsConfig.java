package org.cyclops.evilcraft.component;

import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.broom.BroomPartsContents;

/**
 * @author rubensworks
 */
public class DataComponentBroomPartsConfig extends DataComponentConfig<BroomPartsContents> {
    public DataComponentBroomPartsConfig() {
        super(EvilCraft._instance, "broom_parts", builder -> builder
                .persistent(BroomPartsContents.CODEC)
                .networkSynchronized(BroomPartsContents.STREAM_CODEC));
    }
}
