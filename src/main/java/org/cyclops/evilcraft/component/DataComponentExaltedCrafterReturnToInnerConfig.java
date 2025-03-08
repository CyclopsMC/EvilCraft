package org.cyclops.evilcraft.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DataComponentExaltedCrafterReturnToInnerConfig extends DataComponentConfigCommon<Boolean, IModBase> {
    public DataComponentExaltedCrafterReturnToInnerConfig() {
        super(EvilCraft._instance, "exalted_crafter_return_to_inner", builder -> builder
                .persistent(Codec.BOOL)
                .networkSynchronized(ByteBufCodecs.BOOL));
    }
}
