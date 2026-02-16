package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsDarkTank {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDarkTankEmpty(GameTestHelper helper) {
        // Place a dark tank
        helper.setBlock(POS, RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS);

        helper.succeedWhen(() -> {
            helper.assertTrue(tank.getTank().isEmpty(), "Dark tank should be empty initially");
            helper.assertValueEqual(tank.getTank().getFluidAmount(), 0, "Dark tank should have 0 fluid");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDarkTankFillBlood(GameTestHelper helper) {
        // Place a dark tank
        helper.setBlock(POS, RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS);

        // Fill with blood
        FluidStack blood = new FluidStack(RegistryEntries.FLUID_BLOOD, 1000);
        tank.getTank().fill(blood, IFluidHandler.FluidAction.EXECUTE);

        helper.succeedWhen(() -> {
            helper.assertFalse(tank.getTank().isEmpty(), "Dark tank should not be empty");
            helper.assertValueEqual(tank.getTank().getFluidAmount(), 1000, "Dark tank should have 1000 blood");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDarkTankDrain(GameTestHelper helper) {
        // Place a dark tank
        helper.setBlock(POS, RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS);

        // Fill with blood
        tank.getTank().fill(new FluidStack(RegistryEntries.FLUID_BLOOD, 1000), IFluidHandler.FluidAction.EXECUTE);

        // Drain 500
        tank.getTank().drain(500, IFluidHandler.FluidAction.EXECUTE);

        helper.succeedWhen(() -> {
            helper.assertValueEqual(tank.getTank().getFluidAmount(), 500, "Dark tank should have 500 blood remaining");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDarkTankCapacity(GameTestHelper helper) {
        // Place a dark tank
        helper.setBlock(POS, RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS);

        helper.succeedWhen(() -> {
            helper.assertTrue(tank.getTank().getCapacity() > 0, "Dark tank should have a positive capacity");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testDarkTankStacking(GameTestHelper helper) {
        // Place two dark tanks stacked
        helper.setBlock(POS, RegistryEntries.BLOCK_DARK_TANK.get());
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());

        BlockEntityDarkTank tank1 = helper.getBlockEntity(POS);
        BlockEntityDarkTank tank2 = helper.getBlockEntity(POS.above());

        // Fill bottom tank
        tank1.getTank().fill(new FluidStack(RegistryEntries.FLUID_BLOOD, 1000), IFluidHandler.FluidAction.EXECUTE);

        helper.succeedWhen(() -> {
            helper.assertValueEqual(tank1.getTank().getFluidAmount(), 1000, "Bottom tank should have fluid");
            // The tanks should be connected in a multiblock structure
            helper.assertTrue(tank1.getTank().getCapacity() > 0, "Tank should have capacity");
        });
    }
}
