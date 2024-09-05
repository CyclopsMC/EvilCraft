package org.cyclops.evilcraft.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerMutable;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTankInventory;

import java.util.List;
import java.util.Optional;

/**
 * Copies fluid tank data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyTankData extends LootItemConditionalFunction {
    public static final MapCodec<LootFunctionCopyTankData> CODEC = RecordCodecBuilder.mapCodec(
            builder -> commonFields(builder).apply(builder, LootFunctionCopyTankData::new)
    );
    public static final LootItemFunctionType TYPE = new LootItemFunctionType(LootFunctionCopyTankData.CODEC);

    protected LootFunctionCopyTankData(List<LootItemCondition> conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        if (lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY) instanceof BlockEntityTankInventory tile) {
            SingleUseTank fluidHandlerTile = tile.getTank();
            Optional.ofNullable(itemStack.getCapability(Capabilities.FluidHandler.ITEM))
                    .ifPresent(fluidHandlerItem -> {
                        if (fluidHandlerItem instanceof IFluidHandlerMutable) {
                            ((IFluidHandlerMutable) fluidHandlerItem).setFluidInTank(0, fluidHandlerTile.getFluidInTank(0));
                        }
                        if (fluidHandlerItem instanceof IFluidHandlerItemCapacity) {
                            ((IFluidHandlerItemCapacity) fluidHandlerItem).setCapacity(fluidHandlerTile.getTankCapacity(0));
                        }
                    });
        }
        return itemStack;
    }

    @Override
    public LootItemFunctionType getType() {
        return TYPE;
    }
}
