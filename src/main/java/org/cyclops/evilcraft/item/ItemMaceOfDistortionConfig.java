package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;

/**
 * Config for the {@link ItemMaceOfDistortion}.
 * @author rubensworks
 *
 */
public class ItemMaceOfDistortionConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The attack speed modifier of this mace, which is added to the default player attack speed of 4.", isCommandable = true)
    public static double attackSpeed = -3.0;

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
