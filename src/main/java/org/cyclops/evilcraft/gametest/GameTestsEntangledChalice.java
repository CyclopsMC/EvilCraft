package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

import java.util.List;

public class GameTestsEntangledChalice {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntangledChaliceGroupedFillBlock(GameTestHelper helper) {
        // Add chalice 1
        helper.setBlock(POS, RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        BlockEntityEntangledChalice chalice1 = helper.getBlockEntity(POS, BlockEntityEntangledChalice.class);
        chalice1.setWorldTankId("1");

        // Add chalice 2
        helper.setBlock(POS.south().south(), RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        BlockEntityEntangledChalice chalice2 = helper.getBlockEntity(POS.south().south(), BlockEntityEntangledChalice.class);
        chalice2.setWorldTankId("1");

        // Add chalice 3 at different group
        helper.setBlock(POS.south().south().south(), RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        BlockEntityEntangledChalice chalice3 = helper.getBlockEntity(POS.south().south().south(), BlockEntityEntangledChalice.class);
        chalice3.setWorldTankId("2");

        // Get connected chalice as item
        ItemStack chaliceItemConnected = new ItemStack(RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        chaliceItemConnected.set(RegistryEntries.COMPONENT_WORLD_SHARED_TANK_ID, "1");

        // Get disconnected chalice as item
        ItemStack chaliceItemDisconnected = new ItemStack(RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        chaliceItemDisconnected.set(RegistryEntries.COMPONENT_WORLD_SHARED_TANK_ID, "2");

        // Add tank above with blood
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.above(), BlockEntityDarkTank.class);
        tank.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 8000));
        tank.setEnabled(true);

        helper.succeedWhen(() -> {
            helper.assertValueEqual(4000, chalice1.getTank().getFluid().getAmount(), Component.literal("Original chalice amount"));
            helper.assertValueEqual(RegistryEntries.FLUID_BLOOD.value(), chalice1.getTank().getFluid().getFluid(), Component.literal("Original chalice fluid type"));

            helper.assertValueEqual(4000, chalice2.getTank().getFluid().getAmount(), Component.literal("Connected chalice amount"));
            helper.assertValueEqual(RegistryEntries.FLUID_BLOOD.value(), chalice2.getTank().getFluid().getFluid(), Component.literal("Connected chalice fluid type"));

            helper.assertValueEqual(0, chalice3.getTank().getFluid().getAmount(), Component.literal("Disconnected chalice amount"));
            helper.assertValueEqual(true, chalice3.getTank().getFluid().isEmpty(), Component.literal("Disconnected chalice fluid type"));

            ResourceHandler<FluidResource> fluidHandler = chaliceItemConnected.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(chaliceItemConnected));
            helper.assertValueEqual(4000, fluidHandler.getAmountAsInt(0), Component.literal("Connected chalice item amount"));
            helper.assertValueEqual(RegistryEntries.FLUID_BLOOD.value(), fluidHandler.getResource(0).getFluid(), Component.literal("Connected chalice item fluid type"));

            ResourceHandler<FluidResource> fluidHandler2 = chaliceItemDisconnected.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(chaliceItemDisconnected));
            helper.assertValueEqual(0, fluidHandler2.getAmountAsInt(0), Component.literal("Disconnected chalice item amount"));
            helper.assertValueEqual(true, fluidHandler2.getResource(0).isEmpty(), Component.literal("Disconnected chalice item fluid type"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntangledChaliceGroupedFillItem(GameTestHelper helper) {
        // Add chalice 1
        helper.setBlock(POS, RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        BlockEntityEntangledChalice chalice1 = helper.getBlockEntity(POS, BlockEntityEntangledChalice.class);
        chalice1.setWorldTankId("1b");

        // Add chalice 2
        helper.setBlock(POS.south().south(), RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        BlockEntityEntangledChalice chalice2 = helper.getBlockEntity(POS.south().south(), BlockEntityEntangledChalice.class);
        chalice2.setWorldTankId("1b");

        // Add chalice 3 at different group
        helper.setBlock(POS.south().south().south(), RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        BlockEntityEntangledChalice chalice3 = helper.getBlockEntity(POS.south().south().south(), BlockEntityEntangledChalice.class);
        chalice3.setWorldTankId("2b");

        // Get connected chalice as item
        ItemStack chaliceItemConnected = new ItemStack(RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        chaliceItemConnected.set(RegistryEntries.COMPONENT_WORLD_SHARED_TANK_ID, "1b");

        // Get disconnected chalice as item
        ItemStack chaliceItemDisconnected = new ItemStack(RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        chaliceItemDisconnected.set(RegistryEntries.COMPONENT_WORLD_SHARED_TANK_ID, "2b");

        // Fill item with blood
        try (var tx = Transaction.openRoot()) {
            chaliceItemConnected.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(chaliceItemConnected))
                    .insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), 4000, tx);
            tx.commit();
        }

        helper.succeedWhen(() -> {
            helper.assertValueEqual(4000, chalice1.getTank().getFluid().getAmount(), Component.literal("Original chalice amount"));
            helper.assertValueEqual(RegistryEntries.FLUID_BLOOD.value(), chalice1.getTank().getFluid().getFluid(), Component.literal("Original chalice fluid type"));

            helper.assertValueEqual(4000, chalice2.getTank().getFluid().getAmount(), Component.literal("Connected chalice amount"));
            helper.assertValueEqual(RegistryEntries.FLUID_BLOOD.value(), chalice2.getTank().getFluid().getFluid(), Component.literal("Connected chalice fluid type"));

            helper.assertValueEqual(0, chalice3.getTank().getFluid().getAmount(), Component.literal("Disconnected chalice amount"));
            helper.assertValueEqual(true, chalice3.getTank().getFluid().isEmpty(), Component.literal("Disconnected chalice fluid type"));

            ResourceHandler<FluidResource> fluidHandler = chaliceItemConnected.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(chaliceItemConnected));
            helper.assertValueEqual(4000, fluidHandler.getAmountAsInt(0), Component.literal("Connected chalice item amount"));
            helper.assertValueEqual(RegistryEntries.FLUID_BLOOD.value(), fluidHandler.getResource(0).getFluid(), Component.literal("Connected chalice item fluid type"));

            ResourceHandler<FluidResource> fluidHandler2 = chaliceItemDisconnected.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(chaliceItemDisconnected));
            helper.assertValueEqual(0, fluidHandler2.getAmountAsInt(0), Component.literal("Disconnected chalice item amount"));
            helper.assertValueEqual(true, fluidHandler2.getResource(0).isEmpty(), Component.literal("Disconnected chalice item fluid type"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntangledChaliceBreakFull(GameTestHelper helper) {
        // Place chalice with blood in tank group "3c"
        helper.setBlock(POS, RegistryEntries.BLOCK_ENTANGLED_CHALICE.get());
        BlockEntityEntangledChalice chalice = helper.getBlockEntity(POS, BlockEntityEntangledChalice.class);
        chalice.setWorldTankId("3c");

        // Fill the shared tank with blood
        WorldSharedTankCache.getInstance().setTankContent("3c", new FluidStack(RegistryEntries.FLUID_BLOOD, 4000));

        // Simulate breaking the block by calling getDrops with the loot table
        BlockPos absPos = helper.absolutePos(POS);
        LootParams.Builder lootBuilder = new LootParams.Builder((ServerLevel) helper.getLevel())
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(absPos))
                .withParameter(LootContextParams.BLOCK_STATE, helper.getBlockState(POS))
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, chalice)
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY);
        List<ItemStack> drops = helper.getBlockState(POS).getDrops(lootBuilder);

        helper.succeedWhen(() -> {
            helper.assertValueEqual(1, drops.size(), Component.literal("Breaking a full chalice should drop 1 item"));

            ItemStack drop = drops.get(0);
            helper.assertValueEqual(false, drop.isEmpty(), Component.literal("Dropped item should not be empty"));

            // Check the drop has the correct tank ID (the loot function must have copied it)
            ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) drop.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(drop));
            helper.assertValueEqual(true, fluidHandler != null, Component.literal("Drop should have fluid handler"));
            helper.assertValueEqual("3c", fluidHandler.getTankID(), Component.literal("Drop should have tank ID 3c"));

            // Check the drop gives access to the blood via the shared tank
            helper.assertValueEqual(4000, fluidHandler.getAmountAsInt(0), Component.literal("Drop should have 4000 blood accessible"));
            helper.assertValueEqual(RegistryEntries.FLUID_BLOOD.value(), fluidHandler.getResource(0).getFluid(), Component.literal("Drop fluid type should be blood"));
        });
    }
}
