package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
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
        if (MinecraftHelpers.isClientSide()) {
            EvilCraft._instance.getModEventBus().addListener(this::registerColors);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void registerColors(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemWerewolfFlesh.ItemColor(), getInstance());
    }

}
