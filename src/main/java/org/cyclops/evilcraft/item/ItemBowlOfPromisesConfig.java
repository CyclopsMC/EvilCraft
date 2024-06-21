package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
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
                        , type)
        );
        if (MinecraftHelpers.isClientSide()) {
            EvilCraft._instance.getModEventBus().addListener(this::registerColors);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void registerColors(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemBiomeExtract.ItemColor(), getInstance());
    }
}
