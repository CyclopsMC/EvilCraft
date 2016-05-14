package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.fluid.Blood;

import java.util.Random;

/**
 * Config for the Dull Dust.
 * @author rubensworks
 *
 */
public class CondensedBloodConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static CondensedBloodConfig _instance;

    /**
     * Make a new instance.
     */
    public CondensedBloodConfig() {
        super(
                EvilCraft._instance,
        	true,
            "condensedBlood",
            null,
            null
        );
    }

    @Override
    public void onRegistered() {
        // Register fluid container
        FluidStack fluidStack = new FluidStack(Blood.getInstance(), 500);
        ItemStack filledContainer = new ItemStack(getItemInstance());
        ItemStack emptyContainer = filledContainer.copy();
        emptyContainer.stackSize--;
        FluidContainerRegistry.registerFluidContainer(fluidStack, filledContainer, emptyContainer);

        // Register in loot chests
        LootHelpers.addVanillaLootChestLootEntry(
                new LootEntryItem(getItemInstance(), 10, 0, new LootFunction[]{new LootFunction(new LootCondition[0]) {
                    @Override
                    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
                        stack.stackSize += rand.nextInt(32);
                        return stack;
                    }
                }}, new LootCondition[0],
                        getMod().getModId() + ":" + getSubUniqueName()));
    }
    
}
