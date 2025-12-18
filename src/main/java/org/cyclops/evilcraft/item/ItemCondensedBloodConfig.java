package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.cyclops.cyclopscore.capability.fluid.ResourceHandlerFluidSwapEmpty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the condensed blood.
 * @author rubensworks
 *
 */
public class ItemCondensedBloodConfig extends ItemConfigCommon<IModBase> {

    public ItemCondensedBloodConfig() {
        super(
                EvilCraft._instance,
            "condensed_blood",
                (eConfig, properties) -> new Item(properties)
        );
        EvilCraft._instance.getModEventBus().addListener(this::registerCapability);
    }

    protected void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.Fluid.ITEM, (stack, context) -> new ResourceHandlerFluidSwapEmpty(
                context,
                new FluidStack(RegistryEntries.FLUID_BLOOD, 500),
                ItemResource.of(getInstance()),
                ItemResource.EMPTY
        ), getInstance());
    }

}
