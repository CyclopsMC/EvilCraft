package org.cyclops.evilcraft.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.item.ItemEntangledChalice;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

/**
 * Copies entangled chalice data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyEntangledChaliceData extends LootFunction {
    public static final LootFunctionType TYPE = LootHelpers.registerFunction(new ResourceLocation(Reference.MOD_ID, "copy_entangled_chalice_data"), new LootFunctionCopyEntangledChaliceData.Serializer());

    protected LootFunctionCopyEntangledChaliceData(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack doApply(ItemStack itemStack, LootContext lootContext) {
        TileEntity tile = lootContext.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof TileEntangledChalice) {
            String tankId = ((TileEntangledChalice) tile).getWorldTankId();
            ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStack).orElse(null);
            fluidHandler.setTankID(tankId);
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

    public static class Serializer extends LootFunction.Serializer<LootFunctionCopyEntangledChaliceData> {

        @Override
        public void serialize(JsonObject jsonObject, LootFunctionCopyEntangledChaliceData lootFunctionCopyId, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public LootFunctionCopyEntangledChaliceData deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, ILootCondition[] conditionsIn) {
            return new LootFunctionCopyEntangledChaliceData(conditionsIn);
        }
    }

}
