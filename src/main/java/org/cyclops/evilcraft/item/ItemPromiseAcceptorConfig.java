package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemPromiseAcceptor}.
 * @author rubensworks
 *
 */
public class ItemPromiseAcceptorConfig extends ItemConfig {

    public ItemPromiseAcceptorConfig(ItemPromiseAcceptor.Type type) {
        super(
                EvilCraft._instance,
                "promise_acceptor_" + type.getName(),
                eConfig -> new ItemPromiseAcceptor(new Item.Properties()
                        , type)
        );
        EvilCraft._instance.getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemPromiseAcceptor.ItemColor(), getInstance());
    }

}
