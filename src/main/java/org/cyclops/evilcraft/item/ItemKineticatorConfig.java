package org.cyclops.evilcraft.item;

import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.List;

/**
 * Config for the {@link ItemKineticator}.
 * @author rubensworks
 *
 */
public class ItemKineticatorConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "If the Kineticator should also attract XP orbs.", isCommandable = true)
    public static boolean moveXP = true;

    @ConfigurableProperty(category = "item", comment = "The amount of ticks inbetween each area checking for items.", isCommandable = true)
    public static int tickHoldoff = 1;

    @ConfigurableProperty(category = "item", comment = "The amount of ticks in between each blood consumption when there are valid items in the area.", isCommandable = true)
    public static int consumeHoldoff = 20;

    @ConfigurableProperty(category = "mob",
            comment = "The blacklisted items which should not be influenced by the Kineticator, by unique item/blockState name.")
    public static List<String> kineticateBlacklist = Lists.newArrayList(
            "appliedenergistics2:item.ItemCrystalSeed"
    );

    public ItemKineticatorConfig(boolean repelling) {
        super(
                EvilCraft._instance,
                "kineticator" + (repelling ? "_repelling" : ""),
                eConfig -> new ItemKineticator(new Item.Properties()
                        , repelling)
        );
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return ((ItemKineticator) getInstance()).getDefaultCreativeTabEntries();
    }

}
