package org.cyclops.evilcraft.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DataComponentWorldSharedTankIdConfig extends DataComponentConfig<String> {
    public DataComponentWorldSharedTankIdConfig() {
        super(EvilCraft._instance, "world_shared_tank_id", builder -> builder
                .persistent(Codec.STRING)
                .networkSynchronized(ByteBufCodecs.STRING_UTF8));
    }
}
