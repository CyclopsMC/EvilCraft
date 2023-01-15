package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;

/**
 * Config for the {@link ItemMaceOfDistortion}.
 * @author rubensworks
 *
 */
public class ItemMaceOfDistortionConfig extends ItemConfig {

    public ItemMaceOfDistortionConfig() {
        super(
                EvilCraft._instance,
            "mace_of_distortion",
                eConfig -> new ItemMaceOfDistortion(new Item.Properties()
                        )
        );
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return ((ItemMaceOfDistortion) getInstance()).getDefaultCreativeTabEntries();
    }

}
