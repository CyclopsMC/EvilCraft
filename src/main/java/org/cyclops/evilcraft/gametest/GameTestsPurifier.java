package org.cyclops.evilcraft.gametest;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockPurifierConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;

import java.util.List;
import java.util.Map;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsPurifier {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    private static ItemStack createEnchantedSword(HolderLookup.Provider holders, String enchantmentId, int level) {
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(holders.holderOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(enchantmentId))), level);
        EnchantmentHelper.setEnchantments(sword, enchantments.toImmutable());
        return sword;
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testPurifierDisenchant(GameTestHelper helper) {
        HolderLookup.Provider holders = helper.getLevel().registryAccess();
        helper.setBlock(POS, RegistryEntries.BLOCK_PURIFIER.get());
        BlockEntityPurifier purifier = helper.getBlockEntity(POS);

        // Add enchanted sword and blook
        purifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, createEnchantedSword(holders, "minecraft:sharpness", 1));
        purifier.getInventory().setItem(BlockEntityPurifier.SLOT_ADDITIONAL, new ItemStack(RegistryEntries.ITEM_BLOOK.get()));

        // Fill tank with max blood
        purifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, FluidHelpers.BUCKET_VOLUME * BlockEntityPurifier.MAX_BUCKETS));

        helper.succeedWhen(() -> {
            ItemEnchantments enchantments = purifier.getInventory().getItem(BlockEntityPurifier.SLOT_PURIFY).get(DataComponents.ENCHANTMENTS);
            helper.assertTrue(enchantments == null || enchantments.isEmpty(), "Sword sharpness was not removed");
            helper.assertFalse(purifier.getInventory().getItem(BlockEntityPurifier.SLOT_ADDITIONAL).isEmpty(), "Enchanted book was not produced");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testPurifierCurseRemoval(GameTestHelper helper) {
        HolderLookup.Provider holders = helper.getLevel().registryAccess();
        helper.setBlock(POS, RegistryEntries.BLOCK_PURIFIER.get());
        BlockEntityPurifier purifier = helper.getBlockEntity(POS);

        // Add item with vanishing curse
        purifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, createEnchantedSword(holders, "minecraft:vanishing_curse", 1));

        // Fill tank with 1 bucket of blood (curse removal only needs > 0)
        purifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, FluidHelpers.BUCKET_VOLUME));

        helper.succeedWhen(() -> {
            ItemEnchantments enchantments = purifier.getInventory().getItem(BlockEntityPurifier.SLOT_PURIFY).get(DataComponents.ENCHANTMENTS);
            helper.assertTrue(enchantments == null || enchantments.isEmpty(), "Vanishing curse was not removed");
        });
    }

    /**
     * Tests that blacklisted enchantments are not disenchanted and blacklisted curses are not purified.
     * Runs in its own batch to avoid concurrent modification of the shared config field.
     */
    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200, batch = "purifier_blacklist_0")
    public void testPurifierEnchantmentBlacklist(GameTestHelper helper) {
        HolderLookup.Provider holders = helper.getLevel().registryAccess();
        List<String> originalBlacklist = BlockPurifierConfig.enchantmentIdBlacklist;
        BlockPurifierConfig.enchantmentIdBlacklist =
                Lists.newArrayList("minecraft:sharpness", "minecraft:vanishing_curse");

        // Disenchant scenario: sword with sharpness at POS
        helper.setBlock(POS, RegistryEntries.BLOCK_PURIFIER.get());
        BlockEntityPurifier disenchantPurifier = helper.getBlockEntity(POS);
        disenchantPurifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, createEnchantedSword(holders, "minecraft:sharpness", 1));
        disenchantPurifier.getInventory().setItem(BlockEntityPurifier.SLOT_ADDITIONAL, new ItemStack(RegistryEntries.ITEM_BLOOK.get()));
        disenchantPurifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, FluidHelpers.BUCKET_VOLUME * BlockEntityPurifier.MAX_BUCKETS));

        // Curse removal scenario: sword with vanishing curse at an adjacent position
        BlockPos pos2 = POS.offset(3, 0, 0);
        helper.setBlock(pos2, RegistryEntries.BLOCK_PURIFIER.get());
        BlockEntityPurifier cursePurifier = helper.getBlockEntity(pos2);
        cursePurifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, createEnchantedSword(holders, "minecraft:vanishing_curse", 1));
        cursePurifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, FluidHelpers.BUCKET_VOLUME));

        // After enough ticks for the purifiers to have acted (if not blacklisted), verify nothing was removed
        helper.runAfterDelay(150, () -> {
            BlockPurifierConfig.enchantmentIdBlacklist = originalBlacklist;
            ItemEnchantments disenchantEnchants = disenchantPurifier.getInventory().getItem(BlockEntityPurifier.SLOT_PURIFY).get(DataComponents.ENCHANTMENTS);
            helper.assertTrue(disenchantEnchants != null && !disenchantEnchants.isEmpty(), "Sword sharpness was incorrectly removed despite enchantment blacklist");
            ItemEnchantments curseEnchants = cursePurifier.getInventory().getItem(BlockEntityPurifier.SLOT_PURIFY).get(DataComponents.ENCHANTMENTS);
            helper.assertTrue(curseEnchants != null && !curseEnchants.isEmpty(), "Vanishing curse was incorrectly removed despite enchantment blacklist");
            helper.succeed();
        });
    }

    /**
     * Tests that mob heads are reverted to the previous step of the Blood Infuser head progression,
     * at the cost of a full tank of blood.
     */
    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testPurifierMobHeadDowngrade(GameTestHelper helper) {
        Map<Item, Item> expectedDowngrades = ImmutableMap.of(
                Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD,
                Items.CREEPER_HEAD, Items.ZOMBIE_HEAD,
                Items.ZOMBIE_HEAD, Items.SKELETON_SKULL
        );

        // One purifier per progression step, so that all steps are covered within a single test
        Map<BlockPos, Item> purifierPositions = Maps.newLinkedHashMap();
        int offset = 0;
        for (Map.Entry<Item, Item> entry : expectedDowngrades.entrySet()) {
            BlockPos pos = POS.offset(offset, 0, 0);
            helper.setBlock(pos, RegistryEntries.BLOCK_PURIFIER.get());
            BlockEntityPurifier purifier = helper.getBlockEntity(pos);
            purifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, new ItemStack(entry.getKey()));
            purifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, FluidHelpers.BUCKET_VOLUME * BlockEntityPurifier.MAX_BUCKETS));
            purifierPositions.put(pos, entry.getKey());
            offset += 2;
        }

        helper.succeedWhen(() -> {
            for (Map.Entry<BlockPos, Item> entry : purifierPositions.entrySet()) {
                BlockEntityPurifier purifier = helper.getBlockEntity(entry.getKey());
                Item expected = expectedDowngrades.get(entry.getValue());
                ItemStack result = purifier.getInventory().getItem(BlockEntityPurifier.SLOT_PURIFY);
                helper.assertTrue(result.is(expected), "Expected " + entry.getValue() + " to be purified into " + expected + ", but got " + result.getItem());
                helper.assertTrue(purifier.getTank().isEmpty(), "Purifying " + entry.getValue() + " did not consume the blood");
            }
        });
    }

    /**
     * Tests that heads without a previous progression step are left alone.
     */
    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testPurifierMobHeadDowngradeEndOfProgression(GameTestHelper helper) {
        List<Item> unchangedHeads = Lists.newArrayList(Items.SKELETON_SKULL, Items.PLAYER_HEAD, Items.DRAGON_HEAD);

        Map<BlockPos, Item> purifierPositions = Maps.newLinkedHashMap();
        int offset = 0;
        for (Item head : unchangedHeads) {
            BlockPos pos = POS.offset(offset, 0, 0);
            helper.setBlock(pos, RegistryEntries.BLOCK_PURIFIER.get());
            BlockEntityPurifier purifier = helper.getBlockEntity(pos);
            purifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, new ItemStack(head));
            purifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, FluidHelpers.BUCKET_VOLUME * BlockEntityPurifier.MAX_BUCKETS));
            purifierPositions.put(pos, head);
            offset += 2;
        }

        // After enough ticks for a purification to have happened, verify that nothing changed
        helper.runAfterDelay(150, () -> {
            for (Map.Entry<BlockPos, Item> entry : purifierPositions.entrySet()) {
                BlockEntityPurifier purifier = helper.getBlockEntity(entry.getKey());
                ItemStack result = purifier.getInventory().getItem(BlockEntityPurifier.SLOT_PURIFY);
                helper.assertTrue(result.is(entry.getValue()), "Expected " + entry.getValue() + " to be left alone, but got " + result.getItem());
                helper.assertFalse(purifier.getTank().isEmpty(), "Blood was incorrectly consumed for " + entry.getValue());
            }
            helper.succeed();
        });
    }

}
