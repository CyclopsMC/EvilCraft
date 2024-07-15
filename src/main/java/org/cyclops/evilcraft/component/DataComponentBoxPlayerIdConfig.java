package org.cyclops.evilcraft.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DataComponentBoxPlayerIdConfig extends DataComponentConfig<String> {
    public DataComponentBoxPlayerIdConfig() {
        super(EvilCraft._instance, "box_player_id", builder -> builder
                .persistent(Codec.STRING)
                .networkSynchronized(ByteBufCodecs.STRING_UTF8));
    }
}
