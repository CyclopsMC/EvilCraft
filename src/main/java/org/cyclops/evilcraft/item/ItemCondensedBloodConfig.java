package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nullable;

/**
 * Config for the condensed blood.
 * @author rubensworks
 *
 */
public class ItemCondensedBloodConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "If this item should be injected in loot tables..", requiresMcRestart = true)
    public static boolean injectLootTables = true;

    public ItemCondensedBloodConfig() {
        super(
                EvilCraft._instance,
            "condensed_blood",
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup())) {
                    @Override
                    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
                        return new FluidWrapper(stack);
                    }
                }
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        if (injectLootTables) {
            LootHelpers.injectLootTable(new ResourceLocation(Reference.MOD_ID, "inject/chests/condensed_blood"),
                    LootTables.CHESTS_SPAWN_BONUS_CHEST,
                    LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH,
                    LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH,
                    LootTables.CHESTS_VILLAGE_VILLAGE_SHEPHERD,
                    LootTables.CHESTS_NETHER_BRIDGE,
                    LootTables.CHESTS_SIMPLE_DUNGEON,
                    LootTables.CHESTS_ABANDONED_MINESHAFT,
                    LootTables.CHESTS_JUNGLE_TEMPLE);
        }
    }

    public static class FluidWrapper extends FluidBucketWrapper {

        public FluidWrapper(ItemStack container) {
            super(container);
        }

        protected int getVolume() {
            return 500;
        }

        @Override
        public FluidStack getFluid() {
            return new FluidStack(RegistryEntries.FLUID_BLOOD, getVolume());
        }

        @Override
        protected void setFluid(@Nullable FluidStack fluidStack) {
            if (fluidStack == null) {
                container = container.copy();
                container.shrink(1);
            }
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || resource.getAmount() < getVolume()) {
                return FluidStack.EMPTY;
            }
            FluidStack fluidStack = getFluid();
            if (!fluidStack.isEmpty() && fluidStack.isFluidEqual(resource)) {
                if (action.execute()) {
                    setFluid((FluidStack) null);
                }
                return fluidStack;
            }
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (maxDrain < getVolume()) {
                return FluidStack.EMPTY;
            }
            FluidStack fluidStack = getFluid();
            if (!fluidStack.isEmpty()) {
                if (action.execute()) {
                    setFluid((FluidStack) null);
                }
                return fluidStack;
            }
            return FluidStack.EMPTY;
        }
    }
    
}
