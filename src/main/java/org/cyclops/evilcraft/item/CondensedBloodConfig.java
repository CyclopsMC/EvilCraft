package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.fluid.Blood;

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
        MinecraftHelpers.addVanillaLootChestLootEntry(
                new LootEntryItem(getItemInstance(), 10, 10, new LootFunction[0], new LootCondition[0],
                        getMod().getModId() + ":" + getSubUniqueName()));
    }
    
}
