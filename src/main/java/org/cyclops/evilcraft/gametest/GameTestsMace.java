package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsMace {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testMaceOfDistortionCreation(GameTestHelper helper) {
        ItemStack mace = new ItemStack(RegistryEntries.ITEM_MACE_OF_DISTORTION);

        helper.succeedWhen(() -> {
            helper.assertFalse(mace.isEmpty(), "Mace of distortion should be created");
            helper.assertTrue(mace.getItem() instanceof org.cyclops.evilcraft.item.ItemMace,
                "Item should be a mace");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testMaceIsBloodContainer(GameTestHelper helper) {
        ItemStack mace = new ItemStack(RegistryEntries.ITEM_MACE_OF_DISTORTION);

        helper.succeedWhen(() -> {
            helper.assertTrue(mace.getItem() instanceof ItemBloodContainer,
                "Mace should be a blood container");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testMaceHasFluidHandler(GameTestHelper helper) {
        ItemStack mace = new ItemStack(RegistryEntries.ITEM_MACE_OF_DISTORTION);

        helper.succeedWhen(() -> {
            helper.assertTrue(FluidUtil.getFluidHandler(mace).isPresent(),
                "Mace should have fluid handler");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testMaceCanHoldBlood(GameTestHelper helper) {
        ItemStack mace = new ItemStack(RegistryEntries.ITEM_MACE_OF_DISTORTION);

        // Fill with blood
        FluidUtil.getFluidHandler(mace).ifPresent(handler -> {
            handler.fill(new FluidStack(RegistryEntries.FLUID_BLOOD, 1000),
                net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
        });

        helper.succeedWhen(() -> {
            FluidStack contained = FluidUtil.getFluidContained(mace).orElse(FluidStack.EMPTY);
            helper.assertFalse(contained.isEmpty(), "Mace should contain blood");
            helper.assertValueEqual(contained.getAmount(), 1000, "Mace should contain 1000 blood");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testMaceBloodCapacity(GameTestHelper helper) {
        ItemStack mace = new ItemStack(RegistryEntries.ITEM_MACE_OF_DISTORTION);

        helper.succeedWhen(() -> {
            FluidUtil.getFluidHandler(mace).ifPresent(handler -> {
                helper.assertTrue(handler.getTankCapacity(0) > 0,
                    "Mace should have positive blood capacity");
            });
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 100)
    public void testMaceUsableWithBlood(GameTestHelper helper) {
        ItemStack mace = new ItemStack(RegistryEntries.ITEM_MACE_OF_DISTORTION);

        // Fill with blood
        FluidUtil.getFluidHandler(mace).ifPresent(handler -> {
            handler.fill(new FluidStack(RegistryEntries.FLUID_BLOOD, 5000),
                net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
        });

        // Spawn zombie
        helper.spawn(EntityType.ZOMBIE, POS);

        helper.succeedWhen(() -> {
            // Mace should have blood
            FluidStack contained = FluidUtil.getFluidContained(mace).orElse(FluidStack.EMPTY);
            helper.assertTrue(contained.getAmount() > 0, "Mace should have blood");
        });
    }
}
