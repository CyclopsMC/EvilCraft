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
                        .tab(EvilCraft._instance.getDefaultItemGroup())
                        .stacksTo(1))
        );
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(FMLLoadCompleteEvent event) {
        Minecraft.getInstance().getItemColors().register(new ItemPoisonBottle.ItemColor(), getInstance());
    }
}
