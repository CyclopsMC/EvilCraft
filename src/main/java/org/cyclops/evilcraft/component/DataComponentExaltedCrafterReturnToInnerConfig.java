package org.cyclops.evilcraft.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DataComponentExaltedCrafterReturnToInnerConfig extends DataComponentConfig<Boolean> {
    public DataComponentExaltedCrafterReturnToInnerConfig() {
        super(EvilCraft._instance, "exalted_crafter_return_to_inner", builder -> builder
                .persistent(Codec.BOOL)
                .networkSynchronized(ByteBufCodecs.BOOL));
    }
}
