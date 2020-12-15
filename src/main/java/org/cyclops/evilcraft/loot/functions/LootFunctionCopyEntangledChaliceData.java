package org.cyclops.evilcraft.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.item.ItemEntangledChalice;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

/**
 * Copies entangled chalice data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyEntangledChaliceData implements ILootFunction {

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        TileEntity tile = lootContext.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof TileEntangledChalice) {
            String tankId = ((TileEntangledChalice) tile).getWorldTankId();
            ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStack).orElse(null);
            fluidHandler.setTankID(tankId);
        }
        return itemStack;
    }

    public static class Serializer extends ILootFunction.Serializer<LootFunctionCopyEntangledChaliceData> {

        public Serializer() {
            super(new ResourceLocation(Reference.MOD_ID, "copy_entangled_chalice_data"), LootFunctionCopyEntangledChaliceData.class);
        }

        @Override
        public void serialize(JsonObject jsonObject, LootFunctionCopyEntangledChaliceData lootFunctionCopyId, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public LootFunctionCopyEntangledChaliceData deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return new LootFunctionCopyEntangledChaliceData();
        }
    }

}
