package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntitySanguinaryEnvironmentalAccumulator;

import java.util.List;

public class GameTestsSanguinaryEnvironmentalAccumulator {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreakDropsSingleInventoryItem(GameTestHelper helper) {
        helper.setBlock(POS, RegistryEntries.BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR.get());
        BlockEntitySanguinaryEnvironmentalAccumulator accumulator = helper.getBlockEntity(POS, BlockEntitySanguinaryEnvironmentalAccumulator.class);
        accumulator.getInventory().setItem(BlockEntitySanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE, new ItemStack(Items.DIAMOND));

        BlockPos absPos = helper.absolutePos(POS);
        helper.getLevel().destroyBlock(absPos, true);

        helper.succeedWhen(() -> {
            AABB area = new AABB(absPos).inflate(1);
            List<ItemEntity> items = helper.getLevel().getEntitiesOfClass(ItemEntity.class, area);
            int diamondCount = items.stream()
                    .filter(entity -> entity.getItem().is(Items.DIAMOND))
                    .mapToInt(entity -> entity.getItem().getCount())
                    .sum();
            helper.assertValueEqual(diamondCount, 1, Component.literal("Breaking the accumulator should drop a single diamond"));
        });
    }
}
