package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.List;

public class GameTestsUndeadLogTags {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    private static final TagKey<Block> BLOCK_C_LOGS_THAT_BURN = TagKey.create(Registries.BLOCK, Identifier.parse("c:logs_that_burn"));
    private static final TagKey<Item> ITEM_C_LOGS_THAT_BURN = TagKey.create(Registries.ITEM, Identifier.parse("c:logs_that_burn"));

    private static List<Block> getUndeadLogs() {
        return List.of(
                RegistryEntries.BLOCK_UNDEAD_LOG.get(),
                RegistryEntries.BLOCK_UNDEAD_LOG_STRIPPED.get(),
                RegistryEntries.BLOCK_UNDEAD_WOOD.get(),
                RegistryEntries.BLOCK_UNDEAD_WOOD_STRIPPED.get()
        );
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testUndeadLogsBlockTags(GameTestHelper helper) {
        for (Block block : getUndeadLogs()) {
            helper.setBlock(POS, block);
            BlockState state = helper.getBlockState(POS);
            helper.assertTrue(state.is(BlockTags.LOGS), Component.literal(block + " is not in minecraft:logs"));
            helper.assertTrue(state.is(BlockTags.LOGS_THAT_BURN), Component.literal(block + " is not in minecraft:logs_that_burn"));
            helper.assertTrue(state.is(BLOCK_C_LOGS_THAT_BURN), Component.literal(block + " is not in c:logs_that_burn"));
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testUndeadLogsItemTagsAndFuel(GameTestHelper helper) {
        for (Block block : getUndeadLogs()) {
            ItemStack itemStack = new ItemStack(block);
            helper.assertTrue(itemStack.is(ItemTags.LOGS), Component.literal(block + " item is not in minecraft:logs"));
            helper.assertTrue(itemStack.is(ItemTags.LOGS_THAT_BURN), Component.literal(block + " item is not in minecraft:logs_that_burn"));
            helper.assertTrue(itemStack.is(ITEM_C_LOGS_THAT_BURN), Component.literal(block + " item is not in c:logs_that_burn"));
            helper.assertTrue(helper.getLevel().fuelValues().burnDuration(itemStack) > 0, Component.literal(block + " item is not a furnace fuel"));
        }
        helper.succeed();
    }

}
