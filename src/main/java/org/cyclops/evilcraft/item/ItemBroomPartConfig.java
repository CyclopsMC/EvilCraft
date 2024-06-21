package org.cyclops.evilcraft.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;

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
                        )
        );
        if (MinecraftHelpers.isClientSide()) {
            EvilCraft._instance.getModEventBus().addListener(this::registerColors);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void registerColors(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemBroomPart.ItemColor(), getInstance());
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        ((ItemBroomPart) getInstance()).fillItemCategory(list);
        return list;
    }

}
