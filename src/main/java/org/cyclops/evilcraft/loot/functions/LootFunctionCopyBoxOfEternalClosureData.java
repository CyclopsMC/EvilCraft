package org.cyclops.evilcraft.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;

/**
 * Copies BOEC data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyBoxOfEternalClosureData extends LootItemConditionalFunction {
    public static final LootItemFunctionType TYPE = LootHelpers.registerFunction(new ResourceLocation(Reference.MOD_ID, "copy_box_of_eternal_closure_data"), new LootFunctionCopyBoxOfEternalClosureData.Serializer());

    protected LootFunctionCopyBoxOfEternalClosureData(LootItemCondition[] conditionsIn) {
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
                tag.put(BlockEntityBoxOfEternalClosure.NBTKEY_SPIRIT, spiritTag);
                if (playerId != null && !playerId.isEmpty()) {
                    tag.putString(BlockEntityBoxOfEternalClosure.NBTKEY_PLAYERID, playerId);
                }
                if (playerName != null && !playerName.isEmpty()) {
                    tag.putString(BlockEntityBoxOfEternalClosure.NBTKEY_PLAYERNAME, playerName);
                }
                itemStack.setTag(tag);
            }
        }
        return itemStack;
    }

    @Override
    public LootItemFunctionType getType() {
        return TYPE;
    }

    public static void load() {
        // Dummy call, to enforce class loading
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<LootFunctionCopyBoxOfEternalClosureData> {

        @Override
        public void serialize(JsonObject jsonObject, LootFunctionCopyBoxOfEternalClosureData lootFunctionCopyId, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public LootFunctionCopyBoxOfEternalClosureData deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] conditionsIn) {
            return new LootFunctionCopyBoxOfEternalClosureData(conditionsIn);
        }
    }

}
