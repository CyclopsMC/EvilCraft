package org.cyclops.evilcraft.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

import java.util.List;

/**
 * Copies entangled chalice data to the item.
 * @author rubensworks
 */
public class LootFunctionCopyEntangledChaliceData extends LootItemConditionalFunction {
    public static final MapCodec<LootFunctionCopyEntangledChaliceData> CODEC = RecordCodecBuilder.mapCodec(
            builder -> commonFields(builder).apply(builder, LootFunctionCopyEntangledChaliceData::new)
    );

    protected LootFunctionCopyEntangledChaliceData(List<LootItemCondition> conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        BlockEntity tile = lootContext.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (tile instanceof BlockEntityEntangledChalice) {
            String tankId = ((BlockEntityEntangledChalice) tile).getWorldTankId();
            ItemAccess itemAccess = ItemAccess.forStack(itemStack);
            ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) itemStack.getCapability(Capabilities.Fluid.ITEM, itemAccess);
            try (var tx = Transaction.openRoot()) {
                fluidHandler.setTankID(tankId, tx);
                tx.commit();
            }
            return itemAccess.getResource().toStack(itemAccess.getAmount());
        }
        return itemStack;
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }
}
