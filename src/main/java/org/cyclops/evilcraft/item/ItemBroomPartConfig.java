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
 * Config for the {@link ItemBroomPart}.
 * @author rubensworks
 *
 */
public class ItemBroomPartConfig extends ItemConfig {

    public ItemBroomPartConfig() {
        super(
            EvilCraft._instance,
            "broom_part",
                eConfig -> new ItemBroomPart(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemBroomPart.ItemColor(), getInstance());
    }

}
