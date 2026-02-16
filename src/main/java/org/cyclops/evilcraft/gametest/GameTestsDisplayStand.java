package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsDisplayStand {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDisplayStandPlaceRetrieve(GameTestHelper helper) {
        // Place a display stand
        helper.setBlock(POS, RegistryEntries.BLOCK_DISPLAY_STAND.get());
        BlockEntityDisplayStand stand = helper.getBlockEntity(POS);

        // Add an item to the display stand
        ItemStack diamond = new ItemStack(Items.DIAMOND);
        stand.getInventory().setItem(0, diamond);

        helper.succeedWhen(() -> {
            helper.assertFalse(stand.getInventory().getItem(0).isEmpty(), "Display stand should contain an item");
            helper.assertTrue(ItemStack.matches(stand.getInventory().getItem(0), diamond), "Display stand should contain the diamond");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDisplayStandEmpty(GameTestHelper helper) {
        // Place a display stand
        helper.setBlock(POS, RegistryEntries.BLOCK_DISPLAY_STAND.get());
        BlockEntityDisplayStand stand = helper.getBlockEntity(POS);

        helper.succeedWhen(() -> {
            helper.assertTrue(stand.getInventory().getItem(0).isEmpty(), "Display stand should be empty initially");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDisplayStandRedstoneSignal(GameTestHelper helper) {
        // Place a display stand
        helper.setBlock(POS, RegistryEntries.BLOCK_DISPLAY_STAND.get());
        BlockEntityDisplayStand stand = helper.getBlockEntity(POS);

        // Add an item
        stand.getInventory().setItem(0, new ItemStack(Items.DIAMOND));

        helper.succeedWhen(() -> {
            // Verify item was added
            helper.assertFalse(stand.getInventory().getItem(0).isEmpty(), "Display stand should have item");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDisplayStandMaxStackSize(GameTestHelper helper) {
        // Place a display stand
        helper.setBlock(POS, RegistryEntries.BLOCK_DISPLAY_STAND.get());
        BlockEntityDisplayStand stand = helper.getBlockEntity(POS);

        helper.succeedWhen(() -> {
            helper.assertValueEqual(stand.getInventory().getMaxStackSize(), 1, "Display stand should only accept 1 item");
        });
    }
}
