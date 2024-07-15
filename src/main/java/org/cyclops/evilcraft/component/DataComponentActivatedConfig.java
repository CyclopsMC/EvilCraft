package org.cyclops.evilcraft.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DataComponentActivatedConfig extends DataComponentConfig<Boolean> {
    public DataComponentActivatedConfig() {
        super(EvilCraft._instance, "activated", builder -> builder
                .persistent(Codec.BOOL)
                .networkSynchronized(ByteBufCodecs.BOOL));
    }
}
