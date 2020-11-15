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
                        .group(EvilCraft._instance.getDefaultItemGroup()), type)
        );
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(FMLLoadCompleteEvent event) {
        Minecraft.getInstance().getItemColors().register(new ItemBiomeExtract.ItemColor(), getInstance());
    }
}
