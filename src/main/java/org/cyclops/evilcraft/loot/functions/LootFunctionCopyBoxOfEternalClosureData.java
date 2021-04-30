package org.cyclops.evilcraft.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

/**
 * Copies BOEC data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyBoxOfEternalClosureData extends LootFunction {
    public static final LootFunctionType TYPE = LootHelpers.registerFunction(new ResourceLocation(Reference.MOD_ID, "copy_box_of_eternal_closure_data"), new LootFunctionCopyBoxOfEternalClosureData.Serializer());

    protected LootFunctionCopyBoxOfEternalClosureData(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack doApply(ItemStack itemStack, LootContext lootContext) {
        TileEntity tile = lootContext.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof TileBoxOfEternalClosure) {
            CompoundNBT tag = new CompoundNBT();
            tag.put(TileBoxOfEternalClosure.NBTKEY_SPIRIT, ((TileBoxOfEternalClosure) tile).getSpiritTag());
            String playerId = ((TileBoxOfEternalClosure) tile).getPlayerId();
            tag.putString(TileBoxOfEternalClosure.NBTKEY_PLAYERID, playerId == null ? "" : playerId);
            String playerName = ((TileBoxOfEternalClosure) tile).getPlayerName();
            tag.putString(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME, playerName == null ? "" : playerName);
            itemStack.setTag(tag);
        }
        return itemStack;
    }

    @Override
    public LootFunctionType getFunctionType() {
        return TYPE;
    }

    public static void load() {
        // Dummy call, to enforce class loading
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionCopyBoxOfEternalClosureData> {

        @Override
        public void serialize(JsonObject jsonObject, LootFunctionCopyBoxOfEternalClosureData lootFunctionCopyId, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public LootFunctionCopyBoxOfEternalClosureData deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, ILootCondition[] conditionsIn) {
            return new LootFunctionCopyBoxOfEternalClosureData(conditionsIn);
        }
    }

}
