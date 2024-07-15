package org.cyclops.evilcraft.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DataComponentBoxPlayerNameConfig extends DataComponentConfig<String> {
    public DataComponentBoxPlayerNameConfig() {
        super(EvilCraft._instance, "box_player_name", builder -> builder
                .persistent(Codec.STRING)
                .networkSynchronized(ByteBufCodecs.STRING_UTF8));
    }
}
