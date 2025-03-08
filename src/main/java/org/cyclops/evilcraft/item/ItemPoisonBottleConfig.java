package org.cyclops.evilcraft.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link ItemPoisonBottle}.
 * @author rubensworks
 *
 */
public class ItemPoisonBottleConfig extends ItemConfigCommon<IModBase> {

    public ItemPoisonBottleConfig() {
        super(
                EvilCraft._instance,
            "poison_bottle",
                (eConfig, properties) -> new ItemPoisonBottle(properties
                        .stacksTo(1))
        );
        EvilCraft._instance.getModEventBus().addListener(this::registerCapability);
    }

    protected void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> {
            FluidHandlerItemStackSimple.SwapEmpty capabilityProvider = new FluidHandlerItemStackSimple.SwapEmpty(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_FLUID_CONTENT, stack, new ItemStack(Items.GLASS_BOTTLE), IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume());
            capabilityProvider.fill(new FluidStack(RegistryEntries.FLUID_POISON, IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume()), IFluidHandler.FluidAction.EXECUTE);
            return capabilityProvider;
        }, getInstance());
    }
}
