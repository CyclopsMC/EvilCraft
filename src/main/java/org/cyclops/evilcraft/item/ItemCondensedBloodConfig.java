package org.cyclops.evilcraft.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nullable;

/**
 * Config for the condensed blood.
 * @author rubensworks
 *
 */
public class ItemCondensedBloodConfig extends ItemConfig {

    public ItemCondensedBloodConfig() {
        super(
                EvilCraft._instance,
            "condensed_blood",
                eConfig -> new Item(new Item.Properties()
                        ) {
                    @Override
                    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
                        return new FluidWrapper(stack);
                    }
                }
        );
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
