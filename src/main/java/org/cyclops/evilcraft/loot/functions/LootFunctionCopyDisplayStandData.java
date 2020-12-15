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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerMutable;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.tileentity.TileDisplayStand;

/**
 * Copies display stand data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyDisplayStandData implements ILootFunction {

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        TileEntity tile = lootContext.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof TileDisplayStand) {
            ItemStack type = ((TileDisplayStand) tile).getDisplayStandType();
            BlockDisplayStand.setDisplayStandType(itemStack, type);
        }
        return itemStack;
    }

    public static class Serializer extends ILootFunction.Serializer<LootFunctionCopyDisplayStandData> {

        public Serializer() {
            super(new ResourceLocation(Reference.MOD_ID, "copy_display_stand_data"), LootFunctionCopyDisplayStandData.class);
        }

        @Override
        public void serialize(JsonObject jsonObject, LootFunctionCopyDisplayStandData lootFunctionCopyId, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public LootFunctionCopyDisplayStandData deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return new LootFunctionCopyDisplayStandData();
        }
    }

}
