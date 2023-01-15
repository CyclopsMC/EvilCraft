package org.cyclops.evilcraft.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerMutable;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTankInventory;

/**
 * Copies fluid tank data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyTankData extends LootItemConditionalFunction {
    public static final LootItemFunctionType TYPE = LootHelpers.registerFunction(new ResourceLocation(Reference.MOD_ID, "copy_tank_data"), new LootFunctionCopyTankData.Serializer());

    protected LootFunctionCopyTankData(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        BlockEntityTankInventory tile = (BlockEntityTankInventory) lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        SingleUseTank fluidHandlerTile = tile.getTank();
        itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                .ifPresent(fluidHandlerItem -> {
                    if (fluidHandlerItem instanceof IFluidHandlerMutable) {
                        ((IFluidHandlerMutable) fluidHandlerItem).setFluidInTank(0, fluidHandlerTile.getFluidInTank(0));
                    }
                    if (fluidHandlerItem instanceof IFluidHandlerItemCapacity) {
                        ((IFluidHandlerItemCapacity) fluidHandlerItem).setCapacity(fluidHandlerTile.getTankCapacity(0));
                    }
                });
        return itemStack;
    }

    @Override
    public LootItemFunctionType getType() {
        return TYPE;
    }

    public static void load() {
        // Dummy call, to enforce class loading
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<LootFunctionCopyTankData> {

        @Override
        public void serialize(JsonObject jsonObject, LootFunctionCopyTankData lootFunctionCopyId, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public LootFunctionCopyTankData deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] conditionsIn) {
            return new LootFunctionCopyTankData(conditionsIn);
        }
    }

}
