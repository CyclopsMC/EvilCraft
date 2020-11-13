package org.cyclops.evilcraft.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
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
                        .group(EvilCraft._instance.getDefaultItemGroup()), type)
        );
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(FMLLoadCompleteEvent event) {
        Minecraft.getInstance().getItemColors().register(new ItemPromiseAcceptor.ItemColor(), getInstance());
    }

    /*
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        for(int tier = 0; tier < PromiseAcceptor.COLORS.size(); tier++) {
            OreDictionary.registerOre("materialPromiseAcceptor", new ItemStack(PromiseAcceptor.getInstance(), 1, tier));
        } // TODO
    }*/
}
