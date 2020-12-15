package org.cyclops.evilcraft.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerMutable;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

/**
 * Copies BOEC data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyBoxOfEternalClosureData implements ILootFunction {

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        TileEntity tile = lootContext.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof TileBoxOfEternalClosure) {
            CompoundNBT tag = new CompoundNBT();
            tag.put(TileBoxOfEternalClosure.NBTKEY_SPIRIT, ((TileBoxOfEternalClosure) tile).getSpiritTag());
            tag.putString(TileBoxOfEternalClosure.NBTKEY_PLAYERID, ((TileBoxOfEternalClosure) tile).getPlayerId());
            tag.putString(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME, ((TileBoxOfEternalClosure) tile).getPlayerName());
            itemStack.setTag(tag);
        }
        return itemStack;
    }

    public static class Serializer extends ILootFunction.Serializer<LootFunctionCopyBoxOfEternalClosureData> {

        public Serializer() {
            super(new ResourceLocation(Reference.MOD_ID, "copy_box_of_eternal_closure_data"), LootFunctionCopyBoxOfEternalClosureData.class);
        }

        @Override
        public void serialize(JsonObject jsonObject, LootFunctionCopyBoxOfEternalClosureData lootFunctionCopyId, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public LootFunctionCopyBoxOfEternalClosureData deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return new LootFunctionCopyBoxOfEternalClosureData();
        }
    }

}
