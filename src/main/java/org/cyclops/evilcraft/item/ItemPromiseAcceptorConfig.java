package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemPromiseAcceptor.ItemColor(), getInstance());
    }

}
