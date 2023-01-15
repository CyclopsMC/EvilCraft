package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;

/**
 * Config for the {@link ItemCreativeBloodDrop}.
 * @author rubensworks
 *
 */
public class ItemCreativeBloodDropConfig extends ItemConfig {

    public ItemCreativeBloodDropConfig() {
        super(
                EvilCraft._instance,
            "creative_blood_drop",
                eConfig -> new ItemCreativeBloodDrop(new Item.Properties()
                        )
        );
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return ((ItemCreativeBloodDrop) getInstance()).getDefaultCreativeTabEntries();
    }

}
