package org.cyclops.evilcraft.core.broom;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.evilcraft.api.broom.IBroomPart;

import java.util.List;
import java.util.Objects;

/**
 * @author rubensworks
 */
public class BroomPartsContents {

    public static final Codec<BroomPartsContents> CODEC = Codec.list(
                    ResourceLocation.CODEC.xmap(
                            BroomParts.REGISTRY::getPart,
                            IBroomPart::getId
                    )
            )
            .xmap(BroomPartsContents::new, BroomPartsContents::getParts);
    public static final StreamCodec<ByteBuf, BroomPartsContents> STREAM_CODEC =
            ResourceLocation.STREAM_CODEC.map(
                            BroomParts.REGISTRY::getPart,
                            IBroomPart::getId
                    ).apply(ByteBufCodecs.list())
                    .map(BroomPartsContents::new, BroomPartsContents::getParts);

    private final List<IBroomPart> parts;

    public BroomPartsContents(List<IBroomPart> parts) {
        this.parts = parts;
    }

    public List<IBroomPart> getParts() {
        return parts;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(parts);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BroomPartsContents that)) return false;

        return Objects.equals(parts, that.parts);
    }
}
