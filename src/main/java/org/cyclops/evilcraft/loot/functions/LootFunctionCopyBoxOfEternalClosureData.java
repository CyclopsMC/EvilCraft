package org.cyclops.evilcraft.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;

import java.util.List;

/**
 * Copies BOEC data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyBoxOfEternalClosureData extends LootItemConditionalFunction {
    public static final MapCodec<LootFunctionCopyBoxOfEternalClosureData> CODEC = RecordCodecBuilder.mapCodec(
            builder -> commonFields(builder).apply(builder, LootFunctionCopyBoxOfEternalClosureData::new)
    );
    public static final LootItemFunctionType TYPE = new LootItemFunctionType(LootFunctionCopyBoxOfEternalClosureData.CODEC);

    protected LootFunctionCopyBoxOfEternalClosureData(List<LootItemCondition> conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        BlockEntity tile = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if (tile instanceof BlockEntityBoxOfEternalClosure) {
            CompoundTag tag = new CompoundTag();
            CompoundTag spiritTag = ((BlockEntityBoxOfEternalClosure) tile).getSpiritTag();
            String playerId = ((BlockEntityBoxOfEternalClosure) tile).getPlayerId();
            String playerName = ((BlockEntityBoxOfEternalClosure) tile).getPlayerName();
            if (spiritTag.size() > 0 || (playerId != null && !playerId.isEmpty()) || (playerName != null && !playerName.isEmpty())) {
                itemStack.set(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA, spiritTag);
                if (playerId != null && !playerId.isEmpty()) {
                    itemStack.set(RegistryEntries.COMPONENT_BOX_PLAYER_ID, playerId);
                }
                if (playerName != null && !playerName.isEmpty()) {
                    itemStack.set(RegistryEntries.COMPONENT_BOX_PLAYER_NAME, playerName);
                }
            }
        }
        return itemStack;
    }

    @Override
    public LootItemFunctionType getType() {
        return TYPE;
    }
}
