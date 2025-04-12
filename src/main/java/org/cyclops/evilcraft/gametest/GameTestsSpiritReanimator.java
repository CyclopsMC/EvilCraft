package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritReanimator;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsSpiritReanimator {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 150)
    public void testSpiritReanimatorChicken(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_SPIRIT_REANIMATOR.get());
        BlockEntitySpiritReanimator reanimator = helper.getBlockEntity(POS);
        reanimator.getInventory().setItem(BlockEntitySpiritReanimator.SLOT_BOX, GameTestsSpiritFurnace.createBox(helper, EntityType.CHICKEN));
        reanimator.getInventory().setItem(BlockEntitySpiritReanimator.SLOT_EGG, new ItemStack(Items.EGG));
        reanimator.getInventory().setItem(BlockEntitySpiritReanimator.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        // Add tank above with blood
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.above());
        tank.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 8000));
        tank.setEnabled(true);

        helper.succeedWhen(() -> {
            helper.assertFalse(reanimator.getInventory().getItem(BlockEntitySpiritReanimator.SLOT_BOX).has(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA), "Box has not been cleared");
            helper.assertTrue(reanimator.getInventory().getItem(BlockEntitySpiritReanimator.SLOT_EGG).isEmpty(), "Egg has not been consumed");
            helper.assertFalse(reanimator.getInventory().getItem(BlockEntitySpiritReanimator.SLOTS_OUTPUT).isEmpty(), "Output is not present");
            helper.assertValueEqual(reanimator.getInventory().getItem(BlockEntitySpiritReanimator.SLOTS_OUTPUT).getItem(), Items.CHICKEN_SPAWN_EGG, "Output is not a chicken spawn egg");
        });
    }

}
