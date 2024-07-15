package org.cyclops.evilcraft.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DataComponentBoxSpiritConfig extends DataComponentConfig<CompoundTag> {

    public DataComponentBoxSpiritConfig() {
        super(EvilCraft._instance, "box_spirit", builder -> builder
                .persistent(Codec.PASSTHROUGH.xmap(
                        dynamic -> (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue(),
                        tag -> new Dynamic<>(NbtOps.INSTANCE, tag)
                ))
                .networkSynchronized(ByteBufCodecs.COMPOUND_TAG));
    }
}
