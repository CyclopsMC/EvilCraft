package org.cyclops.evilcraft.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;

import java.util.List;

/**
 * Copies display stand data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyDisplayStandData extends LootItemConditionalFunction {
    public static final MapCodec<LootFunctionCopyDisplayStandData> CODEC = RecordCodecBuilder.mapCodec(
            builder -> commonFields(builder).apply(builder, LootFunctionCopyDisplayStandData::new)
    );

    protected LootFunctionCopyDisplayStandData(List<LootItemCondition> conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        BlockEntity tile = lootContext.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (tile instanceof BlockEntityDisplayStand) {
            ItemStack type = ((BlockEntityDisplayStand) tile).getDisplayStandType();
            BlockDisplayStand.setDisplayStandType(itemStack, type);
        }
        return itemStack;
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }
}
