package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
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
        if (MinecraftHelpers.isClientSide()) {
            EvilCraft._instance.getModEventBus().addListener(this::registerColors);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void registerColors(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemPromiseAcceptor.ItemColor(), getInstance());
    }

}
