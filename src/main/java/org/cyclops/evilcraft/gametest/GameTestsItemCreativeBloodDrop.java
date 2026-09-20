package org.cyclops.evilcraft.gametest;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

public class GameTestsItemCreativeBloodDrop {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";

    /**
     * Guards against the creative fluid handler losing to the regular finite one that CyclopsCore registers
     * for every damage-indicated fluid container: that one reports an empty container on a fresh stack,
     * which silently turns the creative blood drop into a useless item.
     */
    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemCreativeBloodDropIsInfinite(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_CREATIVE_BLOOD_DROP);
            ResourceHandler<FluidResource> handler = itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));

            helper.assertTrue(handler != null, Component.literal("Fluid capability is missing"));
            helper.assertValueEqual(RegistryEntries.FLUID_BLOOD.value(), handler.getResource(0).getFluid(), Component.literal("Contained fluid"));
            helper.assertTrue(handler.getAmountAsInt(0) > 0, Component.literal("Contained amount is zero"));

            int extracted;
            try (Transaction tx = Transaction.openRoot()) {
                extracted = handler.extract(FluidResource.of(RegistryEntries.FLUID_BLOOD.value()), 10000, tx);
                tx.commit();
            }
            helper.assertValueEqual(10000, extracted, Component.literal("Extracted amount"));
            helper.assertTrue(handler.getAmountAsInt(0) > 0, Component.literal("Contained amount after extraction"));
        });
    }

}
