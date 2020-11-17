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
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;

/**
 * Config for the {@link ItemPromise}.
 * @author rubensworks
 *
 */
public class ItemPromiseConfig extends ItemConfig {

    public ItemPromiseConfig(Upgrades.Upgrade upgrade) {
        super(
                EvilCraft._instance,
            "promise_" + upgrade.getId() + "_" + upgrade.getTier(),
                eConfig -> new ItemPromise(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .maxStackSize(4), upgrade)
        );
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(FMLLoadCompleteEvent event) {
        Minecraft.getInstance().getItemColors().register(new ItemPromise.ItemColor(), getInstance());
    }
}
