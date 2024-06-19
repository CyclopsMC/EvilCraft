package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Eternal Water Bucket.
 * @author rubensworks
 *
 */
public class ItemBucketEternalWaterConfig extends ItemConfig {

    public ItemBucketEternalWaterConfig() {
        super(
                EvilCraft._instance,
            "bucket_eternal_water",
                eConfig -> new ItemBucketEternalWater(new Item.Properties()
                        )
        );
        EvilCraft._instance.getModEventBus().addListener(this::registerCapability);
    }

    protected void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> new FluidBucketWrapper(stack) {
            @Override
            protected void setFluid(FluidStack fluid) {
                // Do nothing: we want to keep the item in-place
            }
        }, getInstance());
    }

}
