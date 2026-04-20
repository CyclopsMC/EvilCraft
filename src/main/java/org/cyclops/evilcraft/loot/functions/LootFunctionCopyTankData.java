package org.cyclops.evilcraft.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
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

    protected LootFunctionCopyTankData(List<LootItemCondition> conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        if (lootContext.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof BlockEntityTankInventory tile) {
            SingleUseTank fluidHandlerTile = tile.getTank();
            ItemAccess itemAccess = ItemAccess.forStack(itemStack);
            return Optional.ofNullable(itemAccess.getCapability(Capabilities.Fluid.ITEM))
                    .map(fluidHandlerItem -> {
                        try (var tx = Transaction.openRoot()) {
                            if (fluidHandlerItem instanceof IFluidHandlerCapacity) {
                                ((IFluidHandlerCapacity) fluidHandlerItem).setTankCapacity(0, fluidHandlerTile.getTankCapacity(0), tx);
                            }
                            FluidResource resource = fluidHandlerTile.getResource(0);
                            if (!resource.isEmpty()) {
                                fluidHandlerItem.insert(resource, fluidHandlerTile.getFluidAmount(), tx);
                            }
                            tx.commit();
                        }
                        return itemAccess.getResource().toStack(itemAccess.getAmount());
                    }).orElse(itemStack);
        }
        return itemStack;
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }
}
