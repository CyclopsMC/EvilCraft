package org.cyclops.evilcraft.core.broom;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author rubensworks
 */
public class BroomModifiersContents {

    public static final Codec<BroomModifiersContents> CODEC = ExtraCodecs
            .strictUnboundedMap(
                    ResourceLocation.CODEC.xmap(
                            BroomModifiers.REGISTRY::getModifier,
                            BroomModifier::getId
                    ),
                    Codec.FLOAT
            )
            .xmap(BroomModifiersContents::new, BroomModifiersContents::getModifiers);
    public static final StreamCodec<RegistryFriendlyByteBuf, BroomModifiersContents> STREAM_CODEC =
            ByteBufCodecs.<RegistryFriendlyByteBuf, BroomModifier, Float, Map<BroomModifier, Float>>map(
                            HashMap::new,
                            ResourceLocation.STREAM_CODEC.map(
                                    BroomModifiers.REGISTRY::getModifier,
                                    BroomModifier::getId
                            ),
                            ByteBufCodecs.FLOAT
                    )
                    .map(BroomModifiersContents::new, BroomModifiersContents::getModifiers);

    private final Map<BroomModifier, Float> modifiers;

    public BroomModifiersContents(Map<BroomModifier, Float> modifiers) {
        this.modifiers = modifiers;
    }

    public Map<BroomModifier, Float> getModifiers() {
        return modifiers;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(modifiers);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BroomModifiersContents that)) return false;

        return Objects.equals(modifiers, that.modifiers);
    }
}
