package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntitySanguinaryEnvironmentalAccumulator;

public class GameTestsSanguinaryEnvironmentalAccumulator {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    private static int countDroppedItems(GameTestHelper helper, Item item) {
        return helper.getEntities(EntityType.ITEM, POS, 3).stream()
                .filter(e -> e.getItem().is(item))
                .mapToInt(e -> e.getItem().getCount())
                .sum();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDropsItemsOnceWhenBroken(GameTestHelper helper) {
        helper.setBlock(POS, RegistryEntries.BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR.get());
        BlockEntitySanguinaryEnvironmentalAccumulator tile = helper.getBlockEntity(POS, BlockEntitySanguinaryEnvironmentalAccumulator.class);
        tile.getInventory().setItem(BlockEntitySanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE, new ItemStack(Items.STICK));
        tile.getInventory().setItem(BlockEntitySanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE_RESULT, new ItemStack(Items.DIAMOND));

        helper.destroyBlock(POS);

        helper.assertValueEqual(1, countDroppedItems(helper, Items.STICK), Component.literal("Dropped sticks"));
        helper.assertValueEqual(1, countDroppedItems(helper, Items.DIAMOND), Component.literal("Dropped diamonds"));
        helper.succeed();
    }

}
