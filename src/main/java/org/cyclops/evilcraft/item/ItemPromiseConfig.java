package org.cyclops.evilcraft.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;

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
                        .component(DataComponents.RARITY, upgrade.getTier() > 0 ? Rarity.RARE : Rarity.UNCOMMON)
                        .stacksTo(4), upgrade)
        );
        if (MinecraftHelpers.isClientSide()) {
            EvilCraft._instance.getModEventBus().addListener(this::registerColors);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void registerColors(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemPromise.ItemColor(), getInstance());
    }
}
