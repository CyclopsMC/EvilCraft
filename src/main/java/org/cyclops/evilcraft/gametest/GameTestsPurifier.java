package org.cyclops.evilcraft.gametest;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockPurifierConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;

import java.util.List;

public class GameTestsPurifier {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    private static ItemStack createEnchantedSword(HolderLookup.Provider holders, String enchantmentId, int level) {
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(holders.holderOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse(enchantmentId))), level);
        EnchantmentHelper.setEnchantments(sword, enchantments.toImmutable());
        return sword;
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testPurifierDisenchant(GameTestHelper helper) {
        HolderLookup.Provider holders = helper.getLevel().registryAccess();
        helper.setBlock(POS, RegistryEntries.BLOCK_PURIFIER.get());
        BlockEntityPurifier purifier = helper.getBlockEntity(POS, BlockEntityPurifier.class);

        // Add enchanted sword and blook
        purifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, createEnchantedSword(holders, "minecraft:sharpness", 1));
        purifier.getInventory().setItem(BlockEntityPurifier.SLOT_ADDITIONAL, new ItemStack(RegistryEntries.ITEM_BLOOK.get()));

        // Fill tank with max blood
        purifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume() * BlockEntityPurifier.MAX_BUCKETS));

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
        BlockEntityPurifier purifier = helper.getBlockEntity(POS, BlockEntityPurifier.class);

        // Add item with vanishing curse
        purifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, createEnchantedSword(holders, "minecraft:vanishing_curse", 1));

        // Fill tank with 1 bucket of blood (curse removal only needs > 0)
        purifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume()));

        helper.succeedWhen(() -> {
            ItemEnchantments enchantments = purifier.getInventory().getItem(BlockEntityPurifier.SLOT_PURIFY).get(DataComponents.ENCHANTMENTS);
            helper.assertTrue(enchantments == null || enchantments.isEmpty(), "Vanishing curse was not removed");
        });
    }

    /**
     * Tests that blacklisted enchantments are not disenchanted and blacklisted curses are not purified.
     * Runs in its own batch to avoid concurrent modification of the shared config field.
     */
    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testPurifierEnchantmentBlacklist(GameTestHelper helper) {
        HolderLookup.Provider holders = helper.getLevel().registryAccess();
        List<String> originalBlacklist = BlockPurifierConfig.enchantmentIdBlacklist;
        BlockPurifierConfig.enchantmentIdBlacklist =
                Lists.newArrayList("minecraft:sharpness", "minecraft:vanishing_curse");

        // Disenchant scenario: sword with sharpness at POS
        helper.setBlock(POS, RegistryEntries.BLOCK_PURIFIER.get());
        BlockEntityPurifier disenchantPurifier = helper.getBlockEntity(POS, BlockEntityPurifier.class);
        disenchantPurifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, createEnchantedSword(holders, "minecraft:sharpness", 1));
        disenchantPurifier.getInventory().setItem(BlockEntityPurifier.SLOT_ADDITIONAL, new ItemStack(RegistryEntries.ITEM_BLOOK.get()));
        disenchantPurifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume() * BlockEntityPurifier.MAX_BUCKETS));

        // Curse removal scenario: sword with vanishing curse at an adjacent position
        BlockPos pos2 = POS.offset(3, 0, 0);
        helper.setBlock(pos2, RegistryEntries.BLOCK_PURIFIER.get());
        BlockEntityPurifier cursePurifier = helper.getBlockEntity(pos2, BlockEntityPurifier.class);
        cursePurifier.getInventory().setItem(BlockEntityPurifier.SLOT_PURIFY, createEnchantedSword(holders, "minecraft:vanishing_curse", 1));
        cursePurifier.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume()));

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

}
