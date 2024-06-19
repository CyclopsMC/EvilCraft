package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemBowlOfPromises}.
 * @author rubensworks
 *
 */
public class ItemBowlOfPromisesConfig extends ItemConfig {

    public ItemBowlOfPromisesConfig(ItemBowlOfPromises.Type type) {
        super(
                EvilCraft._instance,
            "bowl_of_promises_" + type.getName(),
                eConfig -> new ItemBowlOfPromises(new Item.Properties()
                        , type)
        );
        EvilCraft._instance.getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemBiomeExtract.ItemColor(), getInstance());
    }
}
