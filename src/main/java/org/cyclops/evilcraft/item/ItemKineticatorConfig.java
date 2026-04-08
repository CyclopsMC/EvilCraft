package org.cyclops.evilcraft.item;

import com.google.common.collect.Lists;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Config for the {@link ItemKineticator}.
 * @author rubensworks
 *
 */
public class ItemKineticatorConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "If the Kineticator should also attract XP orbs.", isCommandable = true)
    public static boolean moveXP = true;

    @ConfigurablePropertyCommon(category = "item", comment = "The amount of ticks inbetween each area checking for items.", isCommandable = true)
    public static int tickHoldoff = 1;

    @ConfigurablePropertyCommon(category = "item", comment = "The amount of ticks in between each blood consumption when there are valid items in the area.", isCommandable = true)
    public static int consumeHoldoff = 20;

    @ConfigurablePropertyCommon(category = "mob",
            comment = "The blacklisted items which should not be influenced by the Kineticator, by unique item/blockState name.")
    public static List<String> kineticateBlacklist = Lists.newArrayList(
            "appliedenergistics2:item.ItemCrystalSeed"
    );

    public ItemKineticatorConfig(boolean repelling) {
        super(
                EvilCraft._instance,
                "kineticator" + (repelling ? "_repelling" : ""),
                (eConfig, properties) -> new ItemKineticator(properties, repelling)
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    public Collection<java.util.function.Supplier<ItemStack>> getDefaultCreativeTabEntries() {
        // Register items dynamically into tab, because when this is called, capabilities are not initialized yet.
        return Collections.emptyList();
    }

    protected void fillCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EvilCraft._instance.getDefaultCreativeTab()) {
            for (ItemStack itemStack : dynamicCreativeTabEntries()) {
                event.accept(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

    protected Collection<ItemStack> dynamicCreativeTabEntries() {
        return ((ItemKineticator) getInstance()).getDefaultCreativeTabEntries();
    }

}
