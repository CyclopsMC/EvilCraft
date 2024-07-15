package org.cyclops.evilcraft.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link ItemCreativeBloodDrop}.
 * @author rubensworks
 *
 */
public class ItemCreativeBloodDropConfig extends ItemConfig {

    public ItemCreativeBloodDropConfig() {
        super(
                EvilCraft._instance,
            "creative_blood_drop",
                eConfig -> new ItemCreativeBloodDrop(new Item.Properties())
        );
        EvilCraft._instance.getModEventBus().addListener(this::registerCapability);
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        // Register items dynamically into tab, because when this is called, capabilities are not initialized yet.
        return Collections.emptyList();
    }

    protected void fillCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EvilCraft._instance.getDefaultCreativeTab()) {
            for (ItemStack itemStack : dynamicCreativeTabEntries()) {
                event.accept(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

    protected Collection<ItemStack> dynamicCreativeTabEntries() {
        return ((ItemCreativeBloodDrop) getInstance()).getDefaultCreativeTabEntries();
    }

    protected void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> new FluidHandlerItemCapacity(stack, ItemCreativeBloodDrop.MB_FILL_PERTICK) {
            @Override
            public FluidStack getFluid() {
                return new FluidStack(((ItemCreativeBloodDrop) stack.getItem()).getFluid(), ItemCreativeBloodDrop.MB_FILL_PERTICK / 2);
            }

            @Nonnull
            @Override
            public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
                return new FluidStack(getFluid().getFluid(), maxDrain);
            }

            @Override
            public int fill(FluidStack resource, IFluidHandler.FluidAction doFill) {
                return resource.getAmount();
            }
        }, getInstance());
    }

}
