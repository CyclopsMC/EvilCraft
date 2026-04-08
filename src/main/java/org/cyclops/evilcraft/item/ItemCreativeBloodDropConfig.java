package org.cyclops.evilcraft.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link ItemCreativeBloodDrop}.
 * @author rubensworks
 *
 */
public class ItemCreativeBloodDropConfig extends ItemConfigCommon<IModBase> {

    public ItemCreativeBloodDropConfig() {
        super(
                EvilCraft._instance,
            "creative_blood_drop",
                (eConfig, properties) -> new ItemCreativeBloodDrop(properties
                        // TODO: not needed?
                        // .component(RegistryEntries.COMPONENT_FLUID_CONTENT, SimpleFluidContent.copyOf(new FluidStack(org.cyclops.evilcraft.RegistryEntries.FLUID_BLOOD, Integer.MAX_VALUE)))
                )
        );
        EvilCraft._instance.getModEventBus().addListener(this::registerCapability);
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    public Collection<java.util.function.Supplier<ItemStack>> getDefaultCreativeTabEntries() {
        // Register items dynamically into tab, because when this is called, capabilities are not initialized yet.
        return Collections.emptyList();
    }

    protected void fillCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EvilCraft._instance.getDefaultCreativeTab()) {
            event.accept(dynamicCreativeTabEntries().stream().findFirst().get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    protected Collection<ItemStack> dynamicCreativeTabEntries() {
        return ((ItemCreativeBloodDrop) getInstance()).getDefaultCreativeTabEntries();
    }

    protected void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.Fluid.ITEM, (stack, context) -> new FluidHandlerItemCapacity(context, Integer.MAX_VALUE) {
            @Override
            public FluidResource getResource(int index) {
                return FluidResource.of(org.cyclops.evilcraft.RegistryEntries.FLUID_BLOOD.get());
            }

            @Override
            public long getAmountAsLong(int index) {
                return ItemCreativeBloodDrop.MB_FILL_PERTICK / 2;
            }

            @Override
            public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
                return amount;
            }

            @Override
            public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
                return amount;
            }
        }, getInstance());
    }

}
