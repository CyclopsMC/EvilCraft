package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritReanimator;

public class GameTestsSpiritReanimator {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 150)
    public void testSpiritReanimatorChicken(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_SPIRIT_REANIMATOR.get());
        BlockEntitySpiritReanimator reanimator = helper.getBlockEntity(POS, BlockEntitySpiritReanimator.class);
        reanimator.getInventory().setItem(BlockEntitySpiritReanimator.SLOT_BOX, GameTestsSpiritFurnace.createBox(helper, EntityTypes.CHICKEN));
        reanimator.getInventory().setItem(BlockEntitySpiritReanimator.SLOT_EGG, new ItemStack(Items.EGG));
        reanimator.getInventory().setItem(BlockEntitySpiritReanimator.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        // Add tank above with blood
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.above(), BlockEntityDarkTank.class);
        tank.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 8000));
        tank.setEnabled(true);

        helper.succeedWhen(() -> {
            helper.assertFalse(reanimator.getInventory().getItem(BlockEntitySpiritReanimator.SLOT_BOX).has(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA), Component.literal("Box has not been cleared"));
            helper.assertTrue(reanimator.getInventory().getItem(BlockEntitySpiritReanimator.SLOT_EGG).isEmpty(), Component.literal("Egg has not been consumed"));
            helper.assertFalse(reanimator.getInventory().getItem(BlockEntitySpiritReanimator.SLOTS_OUTPUT).isEmpty(), Component.literal("Output is not present"));
            helper.assertValueEqual(reanimator.getInventory().getItem(BlockEntitySpiritReanimator.SLOTS_OUTPUT).getItem(), Items.CHICKEN_SPAWN_EGG, Component.literal("Output is not a chicken spawn egg"));
        });
    }

}
