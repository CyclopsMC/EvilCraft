package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link ItemPoisonBottle}.
 * @author rubensworks
 *
 */
public class ItemPoisonBottleConfig extends ItemConfig {

    public ItemPoisonBottleConfig() {
        super(
                EvilCraft._instance,
            "poison_bottle",
                eConfig -> new ItemPoisonBottle(new Item.Properties()

                        .stacksTo(1))
        );
        if (MinecraftHelpers.isClientSide()) {
            EvilCraft._instance.getModEventBus().addListener(this::registerColors);
        }
        EvilCraft._instance.getModEventBus().addListener(this::registerCapability);
    }

    @OnlyIn(Dist.CLIENT)
    public void registerColors(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemPoisonBottle.ItemColor(), getInstance());
    }

    protected void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> {
            FluidHandlerItemStackSimple.SwapEmpty capabilityProvider = new FluidHandlerItemStackSimple.SwapEmpty(stack, new ItemStack(Items.GLASS_BOTTLE), FluidHelpers.BUCKET_VOLUME);
            capabilityProvider.fill(new FluidStack(RegistryEntries.FLUID_POISON, FluidHelpers.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
            return capabilityProvider;
        }, getInstance());
    }
}
