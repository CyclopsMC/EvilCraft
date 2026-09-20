package org.cyclops.evilcraft.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsItemCreativeBloodDrop {

    public static final String TEMPLATE_EMPTY = "empty10";

    /**
     * Guards against the creative fluid handler losing to the regular finite one that CyclopsCore registers
     * for every damage-indicated fluid container: that one reports an empty container on a fresh stack,
     * which silently turns the creative blood drop into a useless item.
     */
    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemCreativeBloodDropIsInfinite(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_CREATIVE_BLOOD_DROP);
            IFluidHandlerItem handler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);

            helper.assertTrue(handler != null, "Fluid capability is missing");
            FluidStack contained = handler.getFluidInTank(0);
            helper.assertTrue(contained.getFluid() == RegistryEntries.FLUID_BLOOD.get(), "Contained fluid is not blood");
            helper.assertTrue(contained.getAmount() > 0, "Contained amount is zero");

            FluidStack drained = handler.drain(10000, IFluidHandler.FluidAction.EXECUTE);
            helper.assertTrue(drained.getAmount() == 10000, "Drained amount is incorrect");
            helper.assertTrue(handler.getFluidInTank(0).getAmount() > 0, "Contained amount after draining is zero");
        });
    }

}
