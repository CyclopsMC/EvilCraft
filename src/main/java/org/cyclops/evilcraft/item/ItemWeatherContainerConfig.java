package org.cyclops.evilcraft.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;

/**
 * Config for the {@link ItemWeatherContainer}.
 * @author rubensworks
 *
 */
public class ItemWeatherContainerConfig extends ItemConfig {

    @ConfigurableProperty(category = "general", comment = "If shapeless crafting of the higher tiers of weather containers should be enabled.", requiresMcRestart = true)
    public static boolean shapelessRecipes = true;

    public ItemWeatherContainerConfig() {
        super(
                EvilCraft._instance,
            "weather_container",
                eConfig -> new ItemWeatherContainer(new Item.Properties()
                        )
        );
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemWeatherContainer.ItemColor(), getInstance());
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        ((ItemWeatherContainer) getInstance()).fillItemCategory(list);
        return list;
    }
}
