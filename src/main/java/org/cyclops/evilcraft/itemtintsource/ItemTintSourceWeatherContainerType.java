package org.cyclops.evilcraft.itemtintsource;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.item.ItemWeatherContainer;
import org.jetbrains.annotations.Nullable;

/**
 * @author rubensworks
 */
public record ItemTintSourceWeatherContainerType() implements ItemTintSource {

    public static final MapCodec<ItemTintSourceWeatherContainerType> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.point(new ItemTintSourceWeatherContainerType())
    );

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        return ItemWeatherContainer.getWeatherType(stack).getDamageRenderColor();
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return ItemTintSourceWeatherContainerType.MAP_CODEC;
    }
}
