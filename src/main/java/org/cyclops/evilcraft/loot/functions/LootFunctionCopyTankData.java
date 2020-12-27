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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerMutable;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.Reference;

/**
 * Copies fluid tank data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyTankData extends LootFunction {
    public static final LootFunctionType TYPE = LootHelpers.registerFunction(new ResourceLocation(Reference.MOD_ID, "copy_tank_data"), new LootFunctionCopyTankData.Serializer());

    protected LootFunctionCopyTankData(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack doApply(ItemStack itemStack, LootContext lootContext) {
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

    @Override
    public LootFunctionType getFunctionType() {
        return TYPE;
    }

    public static void load() {
        // Dummy call, to enforce class loading
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionCopyTankData> {

        @Override
        public void serialize(JsonObject jsonObject, LootFunctionCopyTankData lootFunctionCopyId, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public LootFunctionCopyTankData deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, ILootCondition[] conditionsIn) {
            return new LootFunctionCopyTankData(conditionsIn);
        }
    }

}
