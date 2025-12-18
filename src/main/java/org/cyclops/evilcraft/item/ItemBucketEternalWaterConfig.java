package org.cyclops.evilcraft.item;

import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.cyclops.cyclopscore.capability.fluid.ResourceHandlerFluidSwapEmpty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Eternal Water Bucket.
 * @author rubensworks
 *
 */
public class ItemBucketEternalWaterConfig extends ItemConfigCommon<IModBase> {

    public ItemBucketEternalWaterConfig() {
        super(
                EvilCraft._instance,
            "bucket_eternal_water",
                (eConfig, properties) -> new ItemBucketEternalWater(properties)
        );
        EvilCraft._instance.getModEventBus().addListener(this::registerCapability);
    }

    protected void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.Fluid.ITEM, (stack, context) -> new ResourceHandlerFluidSwapEmpty(
                context,
                new FluidStack(Fluids.WATER, IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume()),
                ItemResource.of(getInstance()),
                ItemResource.of(getInstance())
        ), getInstance());
    }

}
