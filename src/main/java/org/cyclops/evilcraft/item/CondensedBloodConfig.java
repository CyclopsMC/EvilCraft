package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.fluid.Blood;

import javax.annotation.Nullable;
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
            "condensed_blood",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this) {
            @Override
            public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
                return new FluidWrapper(stack);
            }
        };
    }

    @Override
    public void onRegistered() {
        // Register in loot chests
        LootHelpers.addVanillaLootChestLootEntry(
                new LootEntryItem(getItemInstance(), 5, 1, new LootFunction[]{new LootFunction(new LootCondition[0]) {
                    @Override
                    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
                        stack.grow(rand.nextInt(32));
                        return stack;
                    }
                }}, new LootCondition[0],
                        getMod().getModId() + ":" + getSubUniqueName()));
    }

    public static class FluidWrapper extends FluidBucketWrapper {

        public FluidWrapper(ItemStack container) {
            super(container);
        }

        protected int getVolume() {
            return 500;
        }

        @Nullable
        @Override
        public FluidStack getFluid() {
            return new FluidStack(Blood.getInstance(), getVolume());
        }

        @Override
        protected void setFluid(Fluid fluid) {
            if (fluid == null) {
                container = container.copy();
                container.shrink(1);
            }
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (resource == null || resource.amount < getVolume()) {
                return null;
            }
            FluidStack fluidStack = getFluid();
            if (fluidStack != null && fluidStack.isFluidEqual(resource)) {
                if (doDrain) {
                    setFluid(null);
                }
                return fluidStack;
            }
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            if (maxDrain < getVolume()) {
                return null;
            }
            FluidStack fluidStack = getFluid();
            if (fluidStack != null) {
                if (doDrain) {
                    setFluid(null);
                }
                return fluidStack;
            }
            return null;
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            return new FluidTankProperties[] { new FluidTankProperties(getFluid(), getVolume()) };
        }
    }
    
}
