package org.cyclops.evilcraft.itemtintsource;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.item.ItemBowlOfPromises;
import org.jetbrains.annotations.Nullable;

/**
 * @author rubensworks
 */
public record ItemTintSourceBowlOfPromisesType() implements ItemTintSource {

    public static final MapCodec<ItemTintSourceBowlOfPromisesType> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.point(new ItemTintSourceBowlOfPromisesType())
    );

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        if(((ItemBowlOfPromises) itemStack.getItem()).getType().isActive()) {
            float division = (((float) (((ItemBowlOfPromises.Type.values().length - 2) -
                    (((ItemBowlOfPromises) itemStack.getItem()).getType().getTier())) - 1) / 3) + 1);
            int channel = (int) (255 / division);
            return IModHelpers.get().getBaseHelpers().RGBAToInt(channel, channel, channel, 255);
        }
        return -1;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return ItemTintSourceBowlOfPromisesType.MAP_CODEC;
    }
}
