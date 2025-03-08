package org.cyclops.evilcraft.component;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DataComponentDisplayStandTypeConfig extends DataComponentConfigCommon<BlockState, IModBase> {
    public DataComponentDisplayStandTypeConfig() {
            super(EvilCraft._instance, "display_stand_type", builder -> builder
                .persistent(BlockState.CODEC)
                .networkSynchronized(ByteBufCodecs.fromCodec(BlockState.CODEC)));
    }
}
