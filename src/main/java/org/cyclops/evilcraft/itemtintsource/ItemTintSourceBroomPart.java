package org.cyclops.evilcraft.itemtintsource;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.item.ItemBroomPart;
import org.jetbrains.annotations.Nullable;

/**
 * @author rubensworks
 */
public record ItemTintSourceBroomPart() implements ItemTintSource {

    public static final MapCodec<ItemTintSourceBroomPart> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.point(new ItemTintSourceBroomPart())
    );

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        IBroomPart part = ItemBroomPart.getPart(itemStack);
        if (part != null) {
            return part.getModelColor();
        }
        return -1;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return ItemTintSourceBroomPart.MAP_CODEC;
    }
}
