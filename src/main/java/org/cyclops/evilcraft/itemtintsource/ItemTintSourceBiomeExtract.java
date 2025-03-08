package org.cyclops.evilcraft.itemtintsource;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.jetbrains.annotations.Nullable;

/**
 * @author rubensworks
 */
public record ItemTintSourceBiomeExtract() implements ItemTintSource {

    public static final MapCodec<ItemTintSourceBiomeExtract> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.point(new ItemTintSourceBiomeExtract())
    );

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        Holder<Biome> biome = RegistryEntries.ITEM_BIOME_EXTRACT.get().getBiome(itemStack);
        if(biome != null) {
            Triple<Float, Float, Float> rgb = IModHelpers.get().getBaseHelpers().intToRGB(biome.value().getFoliageColor());
            return IModHelpers.get().getBaseHelpers().RGBAToInt((int) (rgb.getLeft() * 255), (int) (rgb.getMiddle() * 255), (int) (rgb.getRight() * 255), 255);
        } else {
            return IModHelpers.get().getBaseHelpers().RGBAToInt(125, 125, 125, 255);
        }
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return ItemTintSourceBiomeExtract.MAP_CODEC;
    }
}
