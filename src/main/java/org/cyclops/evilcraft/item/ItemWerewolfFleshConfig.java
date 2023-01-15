package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemWerewolfFlesh}
 * @author rubensworks
 *
 */
public class ItemWerewolfFleshConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "Humanoid flesh will drop in a 1/X chance.", isCommandable = true)
    public static int humanoidFleshDropChance = 5;

    public ItemWerewolfFleshConfig(boolean humanoid) {
        super(
                EvilCraft._instance,
                humanoid ? "flesh_humanoid" : "flesh_werewolf",
                eConfig -> new ItemWerewolfFlesh(new Item.Properties()

                        .stacksTo(16), humanoid)
        );
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemWerewolfFlesh.ItemColor(), getInstance());
    }

}
