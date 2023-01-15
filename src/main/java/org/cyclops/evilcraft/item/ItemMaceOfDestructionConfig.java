package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;

/**
 * Config for the {@link ItemMaceOfDestruction}.
 * @author rubensworks
 *
 */
public class ItemMaceOfDestructionConfig extends ItemConfig {

    public ItemMaceOfDestructionConfig() {
        super(
                EvilCraft._instance,
            "mace_of_destruction",
                eConfig -> new ItemMaceOfDestruction(new Item.Properties()
                        )
        );
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return ((ItemMaceOfDestruction) getInstance()).getDefaultCreativeTabEntries();
    }

}
