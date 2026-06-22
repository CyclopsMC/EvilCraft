package org.cyclops.evilcraft.gametest;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDarkBloodBrick;
import org.cyclops.evilcraft.block.BlockSpiritFurnace;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritFurnace;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

import java.util.Set;

public class GameTestsSpiritFurnace {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testSpiritFurnace3x3(GameTestHelper helper) {
        // Create valid furnace
        createFurnace(helper, POS, 3);

        helper.succeedWhen(() -> {
            assertFurnaceValid(helper, POS, 3);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testSpiritFurnace5x5(GameTestHelper helper) {
        // Create valid furnace
        createFurnace(helper, POS, 5);

        helper.succeedWhen(() -> {
            assertFurnaceValid(helper, POS, 5);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testSpiritFurnace3x3MissingBrick(GameTestHelper helper) {
        // Create invalid furnace
        Set<BlockPos> excluded = Sets.newHashSet(POS.above());
        createFurnace(helper, POS, 3, excluded);

        helper.succeedWhen(() -> {
            assertFurnaceInvalid(helper, POS, 3, excluded);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testSpiritFurnace3x3ZombieDisallowed(GameTestHelper helper) {
        BlockEntitySpiritFurnace furnace = createFurnace(helper, POS, 3);
        furnace.getInventory().setItem(BlockEntitySpiritFurnace.SLOT_BOX, createBox(helper, EntityTypes.ZOMBIE));

        helper.succeedWhen(() -> {
            helper.assertFalse(furnace.isSizeValidForEntity(), Component.literal("Furnace size should be invalid"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testSpiritFurnace3x3ZombieAllowed(GameTestHelper helper) {
        BlockEntitySpiritFurnace furnace = createFurnace(helper, POS, 4);
        furnace.getInventory().setItem(BlockEntitySpiritFurnace.SLOT_BOX, createBox(helper, EntityTypes.ZOMBIE));

        helper.succeedWhen(() -> {
            helper.assertTrue(furnace.isSizeValidForEntity(), Component.literal("Furnace size should be valid"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 150)
    public void testSpiritFurnace3x3ChickenHaltAndResumeWithHopper(GameTestHelper helper) {
        // Place the furnace one block higher so there is room for a hopper below it
        BlockPos furnacePos = POS.above();
        BlockEntitySpiritFurnace furnace = createFurnace(helper, furnacePos, 3);

        // Fill the blood tank directly so cooking can start immediately
        try (var tx = Transaction.openRoot()) {
            furnace.getTank().insert(0, FluidResource.of(RegistryEntries.FLUID_BLOOD.get()), furnace.getTank().getCapacity(), tx);
            tx.commit();
        }
        furnace.getInventory().setItem(BlockEntitySpiritFurnace.SLOT_BOX, createBox(helper, EntityTypes.CHICKEN));

        // Pre-fill all drop slots with cobblestone (not a chicken drop) using 63 items so
        // canTick() still returns true (count < maxStackSize), but any chicken drop will fail
        // to be placed (different item type) → forceHalt will be set after the first cycle.
        for (int slotId : BlockEntitySpiritFurnace.SLOTS_DROP) {
            furnace.getInventory().setItem(slotId, new ItemStack(Items.COBBLESTONE, 63));
        }

        // After a full chicken cook cycle (chicken has 4 HP → 40 ticks), it should have halted
        helper.runAfterDelay(80, () -> {
            helper.assertTrue(furnace.isForceHalt(), "Furnace should be force-halted because all drop slots are occupied by non-chicken items");

            // Place a hopper directly below the spirit furnace block.
            // A hopper below the furnace accesses the furnace's item capability with Direction.DOWN,
            // which exposes SLOTS_DROP. It always extracts from the first available slot (masked index 0,
            // which is SLOTS_DROP[0]). The resetWork call must fire for ALL masked slots, not just > 1.
            helper.setBlock(POS, Blocks.HOPPER);
        });

        // After the hopper has had time to extract one cobblestone from the first drop slot
        // (hoppers tick every 8 game ticks), forceHalt should have been cleared.
        // With the bug: extractions from masked slots 0 and 1 do NOT call resetWork, so the
        // furnace remains halted. With the fix: any extraction clears forceHalt.
        helper.runAfterDelay(100, () -> {
            helper.assertFalse(furnace.isForceHalt(), "Furnace should no longer be force-halted after hopper extracted items from the first drop slot");
            helper.succeed();
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 250)
    public void testSpiritFurnace3x3ChickenRun(GameTestHelper helper) {
        BlockEntitySpiritFurnace furnace = createFurnace(helper, POS, 3);
        furnace.getInventory().setItem(BlockEntitySpiritFurnace.SLOT_CONTAINER, new ItemStack(RegistryEntries.ITEM_CONDENSED_BLOOD, 64));
        furnace.getInventory().setItem(BlockEntitySpiritFurnace.SLOT_BOX, createBox(helper, EntityTypes.CHICKEN));

        helper.succeedWhen(() -> {
            helper.assertFalse(furnace.getInventory().getItem(BlockEntitySpiritFurnace.SLOTS_DROP[0]).isEmpty(), Component.literal("Furnace should produce drops"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 250)
    public void testSpiritFurnace4x4Player(GameTestHelper helper) {
        BlockEntitySpiritFurnace furnace = createFurnace(helper, POS, 4);
        furnace.getInventory().setItem(BlockEntitySpiritFurnace.SLOT_CONTAINER, new ItemStack(RegistryEntries.ITEM_CONDENSED_BLOOD, 64));
        furnace.getInventory().setItem(BlockEntitySpiritFurnace.SLOT_BOX, createBoxPlayer(helper));

        helper.succeedWhen(() -> {
            helper.assertFalse(furnace.getInventory().getItem(BlockEntitySpiritFurnace.SLOTS_DROP[0]).isEmpty(), Component.literal("Furnace should produce drops"));
        });
    }

    public static ItemStack createBox(GameTestHelper helper, EntityType<?> entityType) {
        ItemStack stack = new ItemStack(RegistryEntries.ITEM_BOX_OF_ETERNAL_CLOSURE);
        EntityVengeanceSpirit spiritDummy = new EntityVengeanceSpirit(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), helper.getLevel());
        spiritDummy.setInnerEntityType(entityType);
        stack.set(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA, IModHelpers.get().getMinecraftHelpers().valueOutputToNbt(spiritDummy.getData()::writeNBT));
        return stack;
    }

    public static ItemStack createBoxPlayer(GameTestHelper helper) {
        ItemStack stack = new ItemStack(RegistryEntries.ITEM_BOX_OF_ETERNAL_CLOSURE);
        EntityVengeanceSpirit spiritDummy = new EntityVengeanceSpirit(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), helper.getLevel());
        spiritDummy.setPlayerId("068d4de0-3a75-4c6a-9f01-8c37e16a394c");
        spiritDummy.setPlayerName("kroeserr");
        spiritDummy.setInnerEntityType(EntityTypes.ZOMBIE);
        stack.set(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA, IModHelpers.get().getMinecraftHelpers().valueOutputToNbt(spiritDummy.getData()::writeNBT));
        stack.set(RegistryEntries.COMPONENT_BOX_PLAYER_ID, "068d4de0-3a75-4c6a-9f01-8c37e16a394c");
        stack.set(RegistryEntries.COMPONENT_BOX_PLAYER_NAME, "kroeserr");
        return stack;
    }

    protected BlockEntitySpiritFurnace createFurnace(GameTestHelper helper, BlockPos pos, int dimension) {
        return createFurnace(helper, pos, dimension, Sets.newHashSet());
    }

    protected BlockEntitySpiritFurnace createFurnace(GameTestHelper helper, BlockPos pos, int dimension, Set<BlockPos> exclude) {
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                for (int z = 0; z < dimension; z++) {
                    BlockPos poso = pos.offset(x, y, z);
                    if (!exclude.contains(poso)) {
                        if (x == 0 && y == 0 && z == 0) {
                            helper.setBlock(poso, RegistryEntries.BLOCK_SPIRIT_FURNACE.get());
                        } else if (x == 0 || y == 0 || z == 0 || x == dimension - 1 || y == dimension - 1 || z == dimension - 1) {
                            helper.setBlock(poso, RegistryEntries.BLOCK_DARK_BLOOD_BRICK.get());
                        }
                    }
                }
            }
        }
        return exclude.contains(pos) ? null : helper.getBlockEntity(pos, BlockEntitySpiritFurnace.class);
    }

    protected void assertFurnaceValid(GameTestHelper helper, BlockPos pos, int dimension) {
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                for (int z = 0; z < dimension; z++) {
                    BlockPos poso = pos.offset(x, y, z);
                    if (x == 0 && y == 0 && z == 0) {
                        helper.assertBlockPresent(RegistryEntries.BLOCK_SPIRIT_FURNACE.get(), poso);
                        helper.assertBlockProperty(poso, BlockSpiritFurnace.ACTIVE, true);
                    } else if (x == 0 || y == 0 || z == 0 || x == dimension - 1 || y == dimension - 1 || z == dimension - 1) {
                        helper.assertBlockPresent(RegistryEntries.BLOCK_DARK_BLOOD_BRICK.get(), poso);
                        helper.assertBlockProperty(poso, BlockDarkBloodBrick.ACTIVE, true);
                    }
                }
            }
        }
    }

    protected void assertFurnaceInvalid(GameTestHelper helper, BlockPos pos, int dimension, Set<BlockPos> exclude) {
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                for (int z = 0; z < dimension; z++) {
                    BlockPos poso = pos.offset(x, y, z);
                    if (exclude.contains(poso)) {
                        helper.assertBlockPresent(Blocks.AIR, poso);
                    } else {
                        if (x == 0 && y == 0 && z == 0) {
                            helper.assertBlockPresent(RegistryEntries.BLOCK_SPIRIT_FURNACE.get(), poso);
                            helper.assertBlockProperty(poso, BlockSpiritFurnace.ACTIVE, false);
                        } else if (x == 0 || y == 0 || z == 0 || x == dimension - 1 || y == dimension - 1 || z == dimension - 1) {
                            helper.assertBlockPresent(RegistryEntries.BLOCK_DARK_BLOOD_BRICK.get(), poso);
                            helper.assertBlockProperty(poso, BlockDarkBloodBrick.ACTIVE, false);
                        }
                    }
                }
            }
        }
    }

}
