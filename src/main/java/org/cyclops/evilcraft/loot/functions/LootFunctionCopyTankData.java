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

/**
 * Copies fluid tank data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyTankData implements ILootFunction {

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        TileEntity tile = lootContext.get(LootParameters.BLOCK_ENTITY);
        tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                .ifPresent(fluidHandlerTile -> {
                    itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                            .ifPresent(fluidHandlerItem -> {
                                if (fluidHandlerItem instanceof IFluidHandlerMutable) {
                                    ((IFluidHandlerMutable) fluidHandlerItem).setFluidInTank(0, fluidHandlerTile.getFluidInTank(0));
                                }
                                if (fluidHandlerItem instanceof IFluidHandlerItemCapacity) {
                                    ((IFluidHandlerItemCapacity) fluidHandlerItem).setCapacity(fluidHandlerTile.getTankCapacity(0));
                                }
                            });
                });
        return itemStack;
    }

    public static class Serializer extends ILootFunction.Serializer<LootFunctionCopyTankData> {

        public Serializer() {
            super(new ResourceLocation(Reference.MOD_ID, "copy_tank_data"), LootFunctionCopyTankData.class);
        }

        @Override
        public void serialize(JsonObject jsonObject, LootFunctionCopyTankData lootFunctionCopyId, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public LootFunctionCopyTankData deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return new LootFunctionCopyTankData();
        }
    }

}
